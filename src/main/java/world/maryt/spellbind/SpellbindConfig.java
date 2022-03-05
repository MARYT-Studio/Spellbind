package world.maryt.spellbind;

import net.minecraftforge.common.ForgeConfigSpec;

public class SpellbindConfig {
    public static ForgeConfigSpec CONFIG;
    public static ForgeConfigSpec.BooleanValue DEBUG;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Debug Mode").push("debug");
        DEBUG = builder.comment("Print all active rules after all they are all loaded")
                .define("printAllActiveRules", false);
        builder.pop();
        CONFIG = builder.build();
    }
}
