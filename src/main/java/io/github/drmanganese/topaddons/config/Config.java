package io.github.drmanganese.topaddons.config;

import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import io.github.drmanganese.topaddons.TOPAddons;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Config {

    public static class BloodMagic {
        public static boolean requireSigil = true;
        static final String CATEGORY = "Blood Magic";
    }

    public static class Forge {
        public static boolean showTankGauge = true;
        static final String CATEGORY = "Forge";
    }

    public static class Vanilla {
        public static boolean noteBlock = true;
        static final String CATEGORY = "Vanilla";
    }

    public static class Helmets {
        public static boolean allHelmetsProbable = true;
        private static String[] helmetBlacklist = {
                "minecraft:diamond_helmet",
                "minecraft:golden_helmet",
                "minecraft:iron_helmet",
                "theoneprobe:diamond_helmet_probe",
                "theoneprobe:gold_helmet_probe",
                "theoneprobe:iron_helmet_probe",
                "enderio:darkSteel_helmet"};

        public static Set<ResourceLocation> helmetBlacklistSet = new LinkedHashSet<>();
        static final String CATEGORY = "Helmets";
    }

    public static void init(Configuration config) {
        config.load();

        BloodMagic.requireSigil = config.getBoolean("requireSigil", BloodMagic.CATEGORY, true, "Is holding a divination sigil required to see certain information.");
        Forge.showTankGauge = config.getBoolean("showTankGauge", Forge.CATEGORY, true, "Show tank gauge for internal tanks on most Tile Entities.");
        Vanilla.noteBlock = config.getBoolean("noteBlockPitch", Vanilla.CATEGORY, true, "Show note block pitch and instrument.");
        Helmets.allHelmetsProbable = config.getBoolean("allHelmetsProbable", Helmets.CATEGORY, true, "All non-blacklisted helmets can be combined with a probe.\n!!! When this is false you can't uncraft previously probified helmets !!!");
        Helmets.helmetBlacklist = config.getStringList("helmetBlacklist", Helmets.CATEGORY, Helmets.helmetBlacklist, "Put registry names of helmets that shouldn't be combinable with a probe\n(don't remove the default ones)");


        if (config.hasChanged()) {
            config.save();
        }

        if (Helmets.allHelmetsProbable) {
            updateHelmetBlacklist();
            TOPAddons.LOGGER.info("Config: allHelmetProbable is enabled, edit blacklist to restrict.");
        } else {
            TOPAddons.LOGGER.info("Config: allHelmetProbable is disabled, only supported helmets will be \"probable\".");
        }
    }

    public static void updateHelmetBlacklistConfig() {
        String[] helmetBlacklist = new String[Helmets.helmetBlacklistSet.size()];
        Iterator itr = Helmets.helmetBlacklistSet.iterator();
        int i = 0;
        while (itr.hasNext()) {
            helmetBlacklist[i] = itr.next().toString();
            i++;
        }
        Helmets.helmetBlacklist = helmetBlacklist;
        TOPAddons.config.get(Helmets.CATEGORY, "helmetBlacklist", helmetBlacklist).set(helmetBlacklist);
        TOPAddons.config.save();
    }

    public static void updateHelmetBlacklist() {
        for (String s : Helmets.helmetBlacklist) {
            ResourceLocation r = new ResourceLocation(s);
            if (ForgeRegistries.ITEMS.containsKey(r)) {
                if (ForgeRegistries.ITEMS.getValue(r) instanceof ItemArmor) {
                    Helmets.helmetBlacklistSet.add(r);
                } else {
                    TOPAddons.LOGGER.info("Config: " + s + " is not a helmet.");
                }
            } else {
                TOPAddons.LOGGER.info("Config: " + s + " is not present in the registry.");
            }
        }
        TOPAddons.LOGGER.info("Config: Added " + Helmets.helmetBlacklistSet.size() + " helmets to blacklist.");
    }
}

