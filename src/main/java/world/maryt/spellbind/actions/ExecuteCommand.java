package world.maryt.spellbind.actions;

import net.minecraft.entity.LivingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExecuteCommand {
    private static final Logger LOGGER = LogManager.getLogger();
    public static void executeCommandAt(LivingEntity target, String command) {
        String uuid = target.getUniqueID().toString();
        String parsedCommand = command.replace("#target", uuid);
        try {
            target.getEntityWorld().getServer().getCommandManager().handleCommand(target.getEntityWorld().getServer().getCommandSource(), parsedCommand);
        } catch(NullPointerException e) {
            LOGGER.error("Failed to execute command: NullPointerException");
            e.printStackTrace();
            e.getCause().printStackTrace();
        }
    }
}
