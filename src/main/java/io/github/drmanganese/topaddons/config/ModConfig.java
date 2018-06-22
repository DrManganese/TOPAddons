package io.github.drmanganese.topaddons.config;

import io.github.drmanganese.topaddons.AddonLoader;
import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.reference.Reference;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ModConfig {

    public static final Pattern ARGB_PATTERN = Pattern.compile("#\\p{XDigit}{8}");

    public static final List<String> NEVER_PROBIFY = Arrays.asList("neotech:electricArmorHelmet",
            "minecraft:diamond_helmet", "minecraft:golden_helmet", "minecraft:iron_helmet",
            "theoneprobe:diamond_helmet_probe", "theoneprobe:gold_helmet_probe", "theoneprobe:iron_helmet_probe",
            "enderio:darkSteel_helmet");
    public static boolean allHelmetsProbifiable;
    public static List<String> helmetBlacklist;

    @SubscribeEvent
    public static void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MOD_ID)) {
            TOPAddons.PROXY.updateConfigs(TOPAddons.CONFIG, AddonLoader.CFG_ADDONS);
        }
    }

    public static void updateHelmetConfig(Configuration config) {
        allHelmetsProbifiable = config.get("helmets", "allHelmetsProbifiable", true, "").getBoolean();
        helmetBlacklist = Arrays.asList(config.get("helmets", "helmetBlacklist", new String[]{}, "", Property.Type.STRING).getStringList());
    }

    public static int getColor(Configuration config, String category, String key, String defaultValue, String comment, Pattern regex) {
        return Long.decode(config.get(category, key, defaultValue, comment, regex).getString()).intValue();
    }

    public static Map<String, Object> updateSync(Configuration config) {
        return AddonLoader.CFG_ADDONS.stream()
                .flatMap(a -> a.updateSyncedConfigs(config).entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
