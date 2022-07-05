package world.maryt.spellbind;

import net.minecraftforge.common.ForgeConfigSpec;

public class SpellbindConfig {
    public static ForgeConfigSpec CONFIG;
    public static ForgeConfigSpec.BooleanValue PRINT_ALL_ACTIVE_RULES;
    public static ForgeConfigSpec.BooleanValue NBT_CHECKER_INFO;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Debug Configurations").push("debug");
        PRINT_ALL_ACTIVE_RULES = builder.comment("Print all active rules after all they are all loaded")
                .define("printAllActiveRules", false);
        NBT_CHECKER_INFO = builder.comment("Print NBT-Checker's all debug info").define("nbtCheckerInfo",false);
        builder.pop();
        CONFIG = builder.build();
    }
}
