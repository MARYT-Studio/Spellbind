package world.maryt.spellbind;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Finish;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static world.maryt.spellbind.actions.ApplyPotionEffect.applyPotionEffect;
import static world.maryt.spellbind.actions.ExecuteCommand.executeCommandAt;
import static world.maryt.spellbind.actions.ManipulateNBTOf.manipulateNBTOf;
import static world.maryt.spellbind.actions.ModifyAttributes.modifyAttributes;
import static world.maryt.spellbind.criteria.DistanceCheck.distanceCheck;
import static world.maryt.spellbind.criteria.EntityNBTCheck.entityNBTCheck;
import static world.maryt.spellbind.criteria.EntityTypeCheck.entityTypeCheck;
import static world.maryt.spellbind.criteria.ItemTypeCheck.itemTypeCheck;

@Mod("spellbind")
public class Spellbind {

    public static final String MOD_ID = "spellbind";
    public static final String MOD_NAME = "Spellbind";
    private static final Logger LOGGER = LogManager.getLogger();

    public Spellbind() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        modBus.addListener(this::setup);
        forgeBus.addListener(this::spell);
        forgeBus.addListener(this::jsonRulesReader);
    }

    public ArrayList<ArrayList<String>> ALL_CUSTOM_RULES = new ArrayList<>();

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("*---*---*---*---*---*---*---*---*");
        LOGGER.info(MOD_NAME + " Mod by RisingInIris2017");
        LOGGER.info("Proudly Presented by MARYT Studio");
        LOGGER.info("*---*---*---*---*---*---*---*---*");
        LOGGER.info(" _______  _______  _______  _        _        ______  _________ _        ______  ".replace('A','\\'));
        LOGGER.info("(  ____ A(  ____ )(  ____ A( A      ( A      (  ___ A A__   __/( (    /|(  __  A ".replace('A','\\'));
        LOGGER.info("| (    A/| (    )|| (    A/| (      | (      | (   ) )   ) (   |  A  ( || (  A  )".replace('A','\\'));
        LOGGER.info("| (_____ | (____)|| (__    | |      | |      | (__/ /    | |   |   A | || |   ) |".replace('A','\\'));
        LOGGER.info("(_____  )|  _____)|  __)   | |      | |      |  __ (     | |   | (A A) || |   | |".replace('A','\\'));
        LOGGER.info("      ) || (      | (      | |      | |      | (  A A    | |   | | A   || |   ) |".replace('A','\\'));
        LOGGER.info("/A____) || )      | (____/A| (____/A| (____/A| )___) )___) (___| )  A  || (__/  )".replace('A','\\'));
        LOGGER.info("A_______)|/       (_______/(_______/(_______/|/ A___/ A_______/|/    )_)(______/ ".replace('A','\\'));
    }

    // Read JSON logic
    // Read JSON from datapack, so that modpack makers can set their own "spells".

    private void jsonRulesReader (AddReloadListenerEvent event) {
        event.addListener(new JsonReloadListener((new GsonBuilder()).create(), MOD_ID + "_rules") {
            @Override
            protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
                objectIn.forEach((resourceLocation, ruleJsonElement) -> {
                    try {
                        LOGGER.warn("Try is fired");
                        JsonArray allRules = JSONUtils.getJsonObject(ruleJsonElement, "rule").get("rule").getAsJsonArray();
                        for (JsonElement ruleElement : allRules) {
                            JsonObject rule = (JsonObject) ruleElement;
                            ArrayList<String> ruleEntry = new ArrayList<>();
                            String itemID, distance, entityType;
                            if (rule.has("item")
                                    && rule.has("distance")
                                    && rule.has("entity")) {
                                itemID = rule.get("item").getAsString();
                                distance = rule.get("distance").getAsString();
                                entityType = rule.get("entity").getAsString();

                                // NBT Key criterion is optional.
                                String nbtKey = "";
                                if (rule.has("nbt_to_check")) {
                                    nbtKey = rule.get("nbt_to_check").getAsString();
                                }

                                // Form a criterion entry
                                ruleEntry.add(itemID);
                                ruleEntry.add(distance);
                                ruleEntry.add(entityType);
                                ruleEntry.add(nbtKey);
                                if (rule.has("actions")) {
                                    try {
                                        JsonArray allActions = rule.get("actions").getAsJsonArray();
                                        for (JsonElement actionElement : allActions) {
                                            JsonObject action = (JsonObject) actionElement;
                                            if (action.has("action_type")) {
                                                String actionType = action.get("action_type").getAsString();
                                                switch (actionType) {
                                                    case "potion":
                                                        if (action.has("potion_id") && action.has("duration") && action.has("amplifier")) {
                                                            // Form an action entry
                                                            ruleEntry.add(action.get("potion_id").getAsString());
                                                            ruleEntry.add(action.get("duration").getAsString());
                                                            ruleEntry.add(action.get("amplifier").getAsString());
                                                        } else {
                                                            LOGGER.error("Invalid potion action");
                                                            return;
                                                        }
                                                        break;
                                                    case "exec_command":
                                                        if (action.has("command")) {
                                                            // Form an action entry
                                                            ruleEntry.add(action.get("command").getAsString());
                                                        } else {
                                                            LOGGER.error("Invalid command execution action");
                                                            return;
                                                        }
                                                        break;
                                                    case "nbt":
                                                        if (action.has("nbt_string")) {
                                                            // Form an action entry
                                                            ruleEntry.add(action.get("nbt_string").getAsString());
                                                        } else {
                                                            LOGGER.error("Invalid NBT manipulation action");
                                                            return;
                                                        }
                                                        break;
                                                    case "attribute":
                                                        if (action.has("namespace")
                                                                && action.has("attribute_id")
                                                                && action.has("operation")
                                                                && action.has("amount")
                                                                && action.has("stackable")) {
                                                            // Form an action entry
                                                            ruleEntry.add(action.get("namespace").getAsString());
                                                            ruleEntry.add(action.get("attribute_id").getAsString());
                                                            ruleEntry.add(action.get("operation").getAsString());
                                                            ruleEntry.add(action.get("amount").getAsString());
                                                            ruleEntry.add(action.get("stackable").getAsString());
                                                        } else {
                                                            LOGGER.error("Invalid Attribute modification action");
                                                            return;
                                                        }
                                                        break;
                                                    default:
                                                        LOGGER.error("None of action types match.");
                                                        return;
                                                }
                                                LOGGER.warn(ruleEntry.toString());
                                                ALL_CUSTOM_RULES.add(ruleEntry);
                                            }
                                        }
                                        return;
                                    } catch (IllegalArgumentException | JsonParseException e) {
                                        LOGGER.error("Parsing error loading actions. Message: {}", e.getMessage());
                                        return;
                                    }
                                }
                            } else {
                                LOGGER.error("Invalid rule");
                                return;
                            }
                        }
                    } catch (IllegalArgumentException | JsonParseException e) {
                        LOGGER.error("Parsing error loading custom rules. Message: {}", e.getMessage());
                    }
                });
            }
        });
    }

    // Mod main logic

    // Criteria are divided into 3 parts:
    // 0. Item player are using. This decides if the spell is valid, so it should be handled first.
    // 1. Distance. It may return Empty Object, so it should be handled separately.
    // 2. Criteria based on an LivingEntity. They return only booleans and do not need to be handled separately.

    private void spell(Finish event) {
        if(event.getEntity() instanceof PlayerEntity) { // If a player fires LivingEntityUseItemEvent.Finish
            PlayerEntity player = (PlayerEntity)(event.getEntity());
/*
            // First Criterion: Item
            if(itemTypeCheck(event.getItem(), "minecraft:apple")) {
                // Second Criterion: Distance
                Optional<Object> target = distanceCheck(player, 10.0); // Check if there is an entity spotted
                // Data Type Magic
                if (target.isPresent()) {
                    Entity targetEntity = (Entity) (target.get());
                    if (targetEntity instanceof LivingEntity) {
                        LivingEntity targetLivingEntity = (LivingEntity) (targetEntity);
                        if (!player.world.isRemote) { // Real Process logic, client side only

                            // From here, should be replaced with JSON-driven logic
                            // Other criteria
                            boolean typeCheck = entityTypeCheck(targetLivingEntity, "minecraft:pig");
                            boolean nbtCheck = entityNBTCheck(targetLivingEntity, "NoAI");

                            if (typeCheck && nbtCheck) { // All criteria are AND logic, will be put in the if-expression belows
                                applyPotionEffect(targetLivingEntity, 1, 1000, 1);
                                executeCommandAt(targetLivingEntity, "tp #target Dev");
                                modifyAttributes(targetLivingEntity, "minecraft","generic.max_health", "multiply_total", 100.0D, false);
                                manipulateNBTOf(targetLivingEntity, "{testWDNMD: 1b}");
                                player.sendMessage(ITextComponent.getTextComponentOrEmpty(targetEntity.getEntityString() + " is spotted"), null);
                            }
                            // To here
                        }
                    }
                }
            }
*/
            if(ALL_CUSTOM_RULES.isEmpty()) {
                LOGGER.warn("rule map is empty.");
            }
            else {
                LOGGER.warn(ALL_CUSTOM_RULES.toString());
            }
        }
    }
}
