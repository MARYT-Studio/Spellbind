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

@Mod("spellbind")
public class Spellbind {

//    public static final String MOD_ID = "spellbind";
//    public static final String MOD_NAME = "Spellbind";
    private static final Logger LOGGER = LogManager.getLogger();

    public Spellbind() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        modBus.addListener(this::setup);
        forgeBus.addListener(this::action);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("*---*---*---*---*---*---*---*---*");
        LOGGER.info("Spellbind Mod by RisingInIris2017");
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

    private Optional<Object> action(Finish event) {
        if(event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (event.getEntity());
            // 这里检测实体的地方需要重写
            if(checkTheTarget(player, 10).get() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) (checkTheTarget(player, 10).get());
                if(!player.world.isRemote) {
                    player.sendMessage(ITextComponent.getTextComponentOrEmpty(target.getEntityString() + " are spotted"), null);
                }
            }
        }
        return null;
    }
    
    private Optional<Object> checkTheTarget(PlayerEntity player, int distance) {
        Vector3d vector3d = player.getEyePosition(1.0F);
        Vector3d vector3d1 = player.getLook(1.0F).scale((double)distance);
        Vector3d vector3d2 = vector3d.add(vector3d1);
        AxisAlignedBB axisalignedbb = player.getBoundingBox().expand(vector3d1).grow(1.0D);
        int i = distance * distance;
        Predicate<Entity> predicate = (p_217727_0_) -> {
            return !p_217727_0_.isSpectator() && p_217727_0_.canBeCollidedWith();
        };
        EntityRayTraceResult entityraytraceresult = ProjectileHelper.rayTraceEntities(player, vector3d, vector3d2, axisalignedbb, predicate, (double)i);
        if (entityraytraceresult == null) {
            return Optional.empty();
        }
        else {
            return vector3d.squareDistanceTo(entityraytraceresult.getHitVec()) > (double)i ? Optional.empty() : Optional.of(entityraytraceresult.getEntity());
        }
    }
}
