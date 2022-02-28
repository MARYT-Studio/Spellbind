package world.maryt.spellbind.actions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.*;

import javax.annotation.Nullable;

public class ApplyPotionEffect {
    public static boolean applyPotionEffect(LivingEntity target, int potionID, int duration, int amplifier) {
        if(Effect.get(potionID) == null) {return false;}
        else {
            Effect effect = Effect.get(potionID);
            return target.addPotionEffect(new EffectInstance(effect, duration, amplifier));
        }
    }
}
