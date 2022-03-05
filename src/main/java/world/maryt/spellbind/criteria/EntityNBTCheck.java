package world.maryt.spellbind.criteria;

import net.minecraft.entity.LivingEntity;

public class EntityNBTCheck {
    public static boolean entityNBTCheck(LivingEntity target, String nbtKey) {
        if(!nbtKey.equals("#i_dont_care#")) {
            return target.serializeNBT().contains(nbtKey);
        }
        return true;
    }
}
