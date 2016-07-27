package io.github.drmanganese.topaddons;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {

    public static class BloodMagic {
        public static boolean requireSigil = true;
        static final String CATEGORY = "bloodmagic";
    }

    public static class Forestry {
        static final String CATEGORY = "forestry";
    }

    public static class Forge {
        public static boolean showTankGauge = true;
        public static boolean alwaysFullGauge = false;
        static final String CATEGORY = "forge";
    }

    public static class TinkersConstruct {
        public static boolean alwaysFullGauge = false;
        static final String CATEGORY = "tinkersconstruct";
    }

    public static void init(File file) {
        Configuration config = new Configuration(file);
        config.load();

        BloodMagic.requireSigil = config.getBoolean("requireSigil", BloodMagic.CATEGORY, true, "Is holding a divination sigil required to see certain information.");

        Forge.showTankGauge = config.getBoolean("showTankGauge", Forge.CATEGORY, true, "Show tank gauge for internal tanks on most Tile Entities.");
        Forge.alwaysFullGauge = config.getBoolean("alwaysFullGauge", Forge.CATEGORY, false, "Show the extended tank display in NORMAL mode (not sneaking).");

        TinkersConstruct.alwaysFullGauge = config.getBoolean("alwaysFullGauge", TinkersConstruct.CATEGORY, false, "Show the extended fluid display in NORMAL mode (not sneaking)");

        if (config.hasChanged())
            config.save();
    }
}
