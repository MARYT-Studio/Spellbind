package world.maryt.spellbind;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Finish;

// Actions
import static world.maryt.spellbind.actions.ApplyPotionEffect.applyPotionEffect;
import static world.maryt.spellbind.actions.ExecuteCommand.executeCommandAt;
import static world.maryt.spellbind.actions.ModifyAttributes.modifyAttributes;
import static world.maryt.spellbind.actions.ManipulateNBTOf.manipulateNBTOf;

// Criteria
import static world.maryt.spellbind.criteria.ItemTypeCheck.itemTypeCheck;
import static world.maryt.spellbind.criteria.DistanceCheck.distanceCheck;
import static world.maryt.spellbind.criteria.EntityTypeCheck.entityTypeCheck;
import static world.maryt.spellbind.criteria.EntityNBTCheck.entityNBTCheck;

@Mod("spellbind")
public class Spellbind {

//    public static final String MOD_ID = "spellbind";
    public static final String MOD_NAME = "Spellbind";
    private static final Logger LOGGER = LogManager.getLogger();

    public Spellbind() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        modBus.addListener(this::setup);
        forgeBus.addListener(this::spell);
    }

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

    // Mod main logic

    // Criteria are divided into 3 parts:
    // 0. Item player are using. This decides if the spell is valid, so it should be handled first.
    // 1. Distance. It may return Empty Object, so it should be handled separately.
    // 2. Criteria based on an LivingEntity. They return only booleans and do not need to be handled separately.

    private void spell(Finish event) {
        if(event.getEntity() instanceof PlayerEntity) { // If a player fires LivingEntityUseItemEvent.Finish
            PlayerEntity player = (PlayerEntity)(event.getEntity());
            // First Criterion: Item
            if(itemTypeCheck(event.getItem(), "minecraft:apple")) {
                // Second Criterion: Distance
                Optional<Object> target = distanceCheck(player, 10.0); // Check if there is an entity spotted
                // Data Type Magic
                if (target.isPresent()) {
                    Entity targetEntity = (Entity) (target.get());
                    if (targetEntity instanceof LivingEntity) {
                        LivingEntity targetLivingEntity = (LivingEntity) (targetEntity);
                        // Other criteria
                        if (!player.world.isRemote) { // Real Process logic, client side only
                            boolean typeCheck = entityTypeCheck(targetLivingEntity, "minecraft:pig");
                            boolean nbtCheck = entityNBTCheck(targetLivingEntity, "NoAI");
                            if (typeCheck && nbtCheck) { // All criteria are AND logic, will be put in the if-expression belows
                                applyPotionEffect(targetLivingEntity, 1, 1000, 1);
                                executeCommandAt(targetLivingEntity, "tp #target Dev");
                                modifyAttributes(targetLivingEntity, "minecraft","generic.max_health", "add", 50.0D);
                                manipulateNBTOf(targetLivingEntity, "NoAI: 0b}");
                                player.sendMessage(ITextComponent.getTextComponentOrEmpty(targetEntity.getEntityString() + " is spotted"), null);
                            }
                            // For debug
                            else {
                                player.sendMessage(ITextComponent.getTextComponentOrEmpty(targetEntity.getEntityString() + " has not the specified NBT"), null);
                            }
                        }
                    }
                }
            }
        }
    }
}
