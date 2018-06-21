package io.github.drmanganese.topaddons.config;

import io.github.drmanganese.topaddons.AddonLoader;
import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.reference.Reference;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ModConfig {

    public static final Pattern ARGB_PATTERN = Pattern.compile("#\\p{XDigit}{8}");

    @SubscribeEvent
    public static void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MOD_ID)) {
            TOPAddons.PROXY.updateConfigs(TOPAddons.CONFIG, AddonLoader.CFG_ADDONS);

            if (TOPAddons.CONFIG.hasChanged()) {
                TOPAddons.CONFIG.save();
            }
        }
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
