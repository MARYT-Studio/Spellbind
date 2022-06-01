package world.maryt.spellbind;

import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Finish;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static world.maryt.spellbind.CreateRuleEntry.createRuleEntry;

import static world.maryt.spellbind.actions.ApplyPotionEffect.applyPotionEffect;
import static world.maryt.spellbind.actions.ExecuteCommand.executeCommandAt;
import static world.maryt.spellbind.actions.ManipulateNBTOf.manipulateNBTOf;
import static world.maryt.spellbind.actions.ModifyAttributes.modifyAttributes;
import static world.maryt.spellbind.actions.SetOnFire.setOnFire;

import static world.maryt.spellbind.criteria.DistanceCheck.distanceCheck;
import static world.maryt.spellbind.criteria.EntityNBTCheck.entityNBTCheck;
import static world.maryt.spellbind.criteria.EntityTypeCheck.entityTypeCheck;
import static world.maryt.spellbind.criteria.ItemTypeCheck.itemTypeCheck;

@Mod("spellbind")
public class Spellbind {

    public static final String MOD_ID = "spellbind";
    public static final String MOD_NAME = "Spellbind";
    private static final Logger LOGGER = LogManager.getLogger();

    // Currently, each rule contains 10 params.
    // Index 0: item id.
    // Index 1: distance criterion.
    // Index 2: entity ID criterion.
    // Index 3: NBT criterion.
    // Index 4: action type.
    // 0-4 are necessary.
    // Index 5-9: action params. Params unused will stay null.
    public static final int RULE_PARAM_COUNT = 10;

    public Spellbind() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        modBus.addListener(this::setup);
        forgeBus.addListener(this::spell);
        forgeBus.addListener(this::jsonRulesReader);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SpellbindConfig.CONFIG);
    }

    protected static ArrayList<String[]> ALL_CUSTOM_RULES = new ArrayList<>();

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
        event.addListener(new JsonReloadListener((new GsonBuilder()).create(), MOD_ID + "_spells") {
            @Override
            protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
                objectIn.forEach((resourceLocation, ruleJsonElement) -> {
                    ALL_CUSTOM_RULES.clear();
                    try {
                        JsonArray allSpells = JSONUtils.getJsonObject(ruleJsonElement, "spells").get("spells").getAsJsonArray();
                        for (JsonElement spellElement : allSpells) {
                            JsonObject spellObject = (JsonObject) spellElement;
                            createRuleEntry(spellObject, RULE_PARAM_COUNT);
                        }
                        LOGGER.info("Rules loading finishes. Now there are " + ALL_CUSTOM_RULES.size() + " rules active.");
                        if(SpellbindConfig.DEBUG.get()) {
                            LOGGER.warn("Spellbind debug mode is on.");
                            LOGGER.warn("All active rules are printed below.");
                            LOGGER.warn("You will see the same print in debug.log");
                            LOGGER.debug("Spellbind debug mode is on.");
                            LOGGER.debug("All active rules are printed below.");
                            LOGGER.debug("You will see the same print in console and latest.log");
                            int index = 1;
                            for (String[] rule: ALL_CUSTOM_RULES) {
                                String ruleEntry = "";
                                for (String ruleParam: rule) {
                                    if(ruleParam != null) {
                                        ruleEntry += ruleParam + ", ";
                                    }
                                    else break;
                                }
                                ruleEntry = "Rule " + index + ": " + ruleEntry.substring(0, ruleEntry.length()-2);
                                LOGGER.warn(ruleEntry);
                                LOGGER.debug(ruleEntry);
                                index++;
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
            for (String[] ruleEntry: ALL_CUSTOM_RULES) {
                // First Criterion: Item
                if(itemTypeCheck(event.getItem(), ruleEntry[0])) {
                    // Second Criterion: Distance
                    Optional<Object> target = distanceCheck(player, Double.parseDouble(ruleEntry[1]));
                    // Data Type Magic
                    if (target.isPresent()) {
                        Entity targetEntity = (Entity) (target.get());
                        if (targetEntity instanceof LivingEntity) {
                            LivingEntity targetLivingEntity = (LivingEntity) (targetEntity);
                            if(!targetLivingEntity.getEntityWorld().isRemote()) {
                                if (entityTypeCheck(targetLivingEntity, ruleEntry[2]) && entityNBTCheck(targetLivingEntity, ruleEntry[3])) {
                                    switch (ruleEntry[4]) {
                                        case "potion":
                                            try {
                                                applyPotionEffect(targetLivingEntity, Integer.parseInt(ruleEntry[5]), Integer.parseInt(ruleEntry[6]), Integer.parseInt(ruleEntry[7]));
                                            } catch (NumberFormatException e) {
                                                LOGGER.error("Invalid potion effect parameter.");
                                                e.printStackTrace();
                                                return;
                                            }
                                            break;
                                        case "exec_command":
                                            executeCommandAt(targetLivingEntity, ruleEntry[5]);
                                            break;
                                        case "nbt":
                                            manipulateNBTOf(targetLivingEntity, ruleEntry[5]);
                                            break;
                                        case "attribute":
                                            try {
                                                modifyAttributes(targetLivingEntity, ruleEntry[5], ruleEntry[6], ruleEntry[7], Double.parseDouble(ruleEntry[8]), Boolean.parseBoolean(ruleEntry[9]));
                                            } catch (NumberFormatException e) {
                                                LOGGER.error("Invalid attribute parameter.");
                                                e.printStackTrace();
                                                return;
                                            }
                                            break;
                                        case "set_on_fire":
                                            try {
                                                setOnFire(targetLivingEntity, Integer.parseInt(ruleEntry[5]));
                                            } catch (NumberFormatException e) {
                                                LOGGER.error("Invalid attribute parameter.");
                                                e.printStackTrace();
                                                return;
                                            }
                                            break;
                                        default:
                                            LOGGER.error("No action type matches.");
                                            return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
