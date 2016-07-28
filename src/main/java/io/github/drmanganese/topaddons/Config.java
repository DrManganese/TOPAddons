package io.github.drmanganese.topaddons;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {

    public static class BloodMagic {
        public static boolean requireSigil = true;
        static final String CATEGORY = "Blood Magic";
    }

    public static class Forge {
        public static boolean showTankGauge = true;
        static final String CATEGORY = "Forge";
    }

    public static void init(File file) {
        Configuration config = new Configuration(file);
        config.load();

        BloodMagic.requireSigil = config.getBoolean("requireSigil", BloodMagic.CATEGORY, true, "Is holding a divination sigil required to see certain information.");

        Forge.showTankGauge = config.getBoolean("showTankGauge", Forge.CATEGORY, true, "Show tank gauge for internal tanks on most Tile Entities.");
        if (config.hasChanged())
            config.save();
    }
}
