package world.maryt.spellbind.actions;

import net.minecraft.entity.LivingEntity;

public class SetOnFire {
    public static void setOnFire(LivingEntity target, int seconds) {
        target.setFire(seconds);
    }
}
