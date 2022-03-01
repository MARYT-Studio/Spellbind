package world.maryt.spellbind.criteria;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Optional;
import java.util.function.Predicate;

public class DistanceCheck {
    public static Optional<Object> distanceCheck(PlayerEntity player, double distance) {
        Vector3d eyePosition = player.getEyePosition(1.0F);
        Vector3d lookingAtDirection = player.getLook(1.0F).scale(distance);
        Vector3d lookingAtPosition = eyePosition.add(lookingAtDirection);
        AxisAlignedBB axisalignedbb = player.getBoundingBox().expand(lookingAtDirection).grow(1.0D);
        double squareDistance = distance * distance;
        // Magic predicate, maybe someday I can understand
        Predicate<Entity> predicate = (p_217727_0_) -> !p_217727_0_.isSpectator() && p_217727_0_.canBeCollidedWith();
        EntityRayTraceResult entityraytraceresult = ProjectileHelper.rayTraceEntities(player, eyePosition, lookingAtPosition, axisalignedbb, predicate, squareDistance);
        if (entityraytraceresult == null) {
            return Optional.empty();
        }
        else {
            return eyePosition.squareDistanceTo(entityraytraceresult.getHitVec()) > squareDistance ? Optional.empty() : Optional.of(entityraytraceresult.getEntity());
        }
    }
}
