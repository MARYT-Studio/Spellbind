package world.maryt.spellbind.actions;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ManipulateNBTOf {
    private static final Logger LOGGER = LogManager.getLogger();
    public static void manipulateNBTOf(LivingEntity target, String nbtString) {
        CompoundNBT nbtToAdd = null;
        try {
            nbtToAdd = JsonToNBT.getTagFromJson(nbtString);
            CompoundNBT targetNBT = target.serializeNBT();
            targetNBT.merge(nbtToAdd);
            LOGGER.debug("NBT Merge fired.");
        } catch (CommandSyntaxException e) {
            LOGGER.error("Failed to parse Spell JSON");
            e.printStackTrace();
        }
    }
}
