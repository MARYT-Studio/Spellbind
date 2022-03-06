package world.maryt.spellbind.criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class EntityTypeCheck {
    public static boolean entityTypeCheck(LivingEntity target, String type) {
        if(!type.equals("#i_dont_care#")) {
            if(target instanceof PlayerEntity) {
                return type.equals("#player#");
            }
            else {
                return target.getEntityString() != null && target.getEntityString().equals(type);
            }
        }
        return true;
    }
}
