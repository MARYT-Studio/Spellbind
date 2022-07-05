package world.maryt.spellbind.criteria;

import net.minecraft.entity.LivingEntity;
import world.maryt.spellbind.SpellbindConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityNBTCheck {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final boolean debug = SpellbindConfig.NBT_CHECKER_INFO.get();
    public static boolean entityNBTCheck(LivingEntity target, String nbtKey) {
        if(nbtKey.equals("#i_dont_care#")) {
            if(debug) {LOGGER.warn("#i_dont_care param is passed in, return true");}
            return true;
        }
        if(target.serializeNBT().contains(nbtKey)) {
            if(debug) {LOGGER.warn("NBT to check is a Vanilla one, founded");}
            return true;
        }
        if(target.serializeNBT().contains("ForgeData")) {
            if(debug) {LOGGER.warn("NBT to check may exist, let's check");}
            if(target.serializeNBT().getCompound("ForgeData").contains(nbtKey)) {
                if(debug) {LOGGER.warn("NBT to check is a Modded one, found");}
                return true;
            }
        }
        if(debug) {LOGGER.warn("NBT to check is not found");}
        return false;
    }
}
