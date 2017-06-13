package io.github.drmanganese.topaddons.config;

import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import io.github.drmanganese.topaddons.TOPAddons;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HelmetConfig {

    public static List<String> neverCraftList = Arrays.asList("neotech:electricArmorHelmet",
            "minecraft:diamond_helmet", "minecraft:golden_helmet", "minecraft:iron_helmet",
            "theoneprobe:diamond_helmet_probe", "theoneprobe:gold_helmet_probe", "theoneprobe:iron_helmet_probe",
            "enderio:darkSteel_helmet");

    public static boolean allHelmetsProbable = true;
    public static Set<ResourceLocation> helmetBlacklistSet = new LinkedHashSet<>();

    public static void loadHelmetBlacklist(Configuration config) {
        allHelmetsProbable = config.get("helmets", "allHelmetsProbable", true, "All non-blacklisted helmets can be combined with a probe.\n!!! When this is false you can't uncraft previously probified helmets !!!").setLanguageKey("topaddons.config:helmets_probable").getBoolean();

        if (allHelmetsProbable) {
            TOPAddons.LOGGER.info("Config: allHelmetProbable is enabled, edit blacklist to restrict.");

            helmetBlacklistSet = Arrays.stream(getBlacklistProperty(config).getStringList())
                    .map(ResourceLocation::new)
                    .filter(HelmetConfig::isHelmet)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            if (helmetBlacklistSet.size() > 0) {
                TOPAddons.LOGGER.info("Config: " + helmetBlacklistSet.size() + " helmets added to the blacklist");
            }
        } else {
            TOPAddons.LOGGER.info("Config: allHelmetProbable is disabled, only supported helmets will be \"probable\".");
        }

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void saveHelmetBlacklist(Configuration config) {
       getBlacklistProperty(config).set(helmetBlacklistSet.stream().map(ResourceLocation::toString).toArray(String[]::new));

        if (config.hasChanged()) {
            config.save();
        }
    }

    private static Property getBlacklistProperty(Configuration config) {
        return config.get("helmets", "helmetBlacklist", new String[]{}, "Registry names of helmets that shouldn't be combinable with a probe").setLanguageKey("topaddons.config:helmets_blacklist");
    }

    private static boolean isHelmet(ResourceLocation r) {
        if (ForgeRegistries.ITEMS.containsKey(r)) {
            if (ForgeRegistries.ITEMS.getValue(r) instanceof ItemArmor) {
                return true;
            } else {
                TOPAddons.LOGGER.info("Config: " + r.toString() + " is not a helmet.");
            }
        } else {
            TOPAddons.LOGGER.info("Config: " + r.toString() + " is not a valid registry name.");
        }

        return false;
    }
}
