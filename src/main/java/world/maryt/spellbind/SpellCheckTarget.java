package world.maryt.spellbind;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Optional;
import java.util.function.Predicate;

public class SpellCheckTarget {
    public static Optional<Object> checkTheTarget(PlayerEntity player, double distance) {
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
