package world.maryt.spellbind;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Finish;
import net.minecraft.util.math.EntityRayTraceResult;

import java.util.Optional;
import java.util.function.Predicate;

import world.maryt.spellbind.actions.ApplyPotionEffect;

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

    private Optional<Object> spell(Finish event) {
        if(event.getEntity() instanceof PlayerEntity) { // If a player fires LivingEntityUseItemEvent.Finish
            PlayerEntity player = (PlayerEntity)(event.getEntity());
            Optional<Object> target = checkTheTarget(player, 10.0); // Check if there is an entity spotted
            if(target.isPresent()) {
                Entity targetEntity = (Entity)(target.get());
                if(targetEntity instanceof LivingEntity) {
                    LivingEntity targetLivingEntity = (LivingEntity)(targetEntity);
                    if(!player.world.isRemote) {
                        boolean potionSuccess = new ApplyPotionEffect().applyPotionEffect(targetLivingEntity,1,1000,1);
                        player.sendMessage(ITextComponent.getTextComponentOrEmpty(targetEntity.getEntityString() + " is spotted, has applied potion effect: " + potionSuccess), null);
                    }
                }
            }
        }
        return null;
    }
    
    private Optional<Object> checkTheTarget(PlayerEntity player, double distance) {
        Vector3d vector3d = player.getEyePosition(1.0F);
        Vector3d vector3d1 = player.getLook(1.0F).scale(distance);
        Vector3d vector3d2 = vector3d.add(vector3d1);
        AxisAlignedBB axisalignedbb = player.getBoundingBox().expand(vector3d1).grow(1.0D);
        double squareDistance = distance * distance;
        Predicate<Entity> predicate = (p_217727_0_) -> {
            return !p_217727_0_.isSpectator() && p_217727_0_.canBeCollidedWith();
        };
        EntityRayTraceResult entityraytraceresult = ProjectileHelper.rayTraceEntities(player, vector3d, vector3d2, axisalignedbb, predicate, squareDistance);
        if (entityraytraceresult == null) {
            return Optional.empty();
        }
        else {
            return vector3d.squareDistanceTo(entityraytraceresult.getHitVec()) > squareDistance ? Optional.empty() : Optional.of(entityraytraceresult.getEntity());
        }
    }
}
