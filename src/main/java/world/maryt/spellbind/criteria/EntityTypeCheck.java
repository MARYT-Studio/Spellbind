package world.maryt.spellbind.criteria;
import net.minecraft.entity.LivingEntity;

public class EntityTypeCheck {
    public static boolean entityTypeCheck(LivingEntity target, String type) {
        return target.getEntityString().equals(type) ? true : false;
    }
}
