package io.github.drmanganese.topaddons;

import net.minecraft.util.Tuple;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.github.drmanganese.topaddons.reference.Names;

import java.util.HashMap;
import java.util.Map;

/**
 * Client-side only configs, all properties are integers because of capability implementation
 */
@SideOnly(Side.CLIENT)
public class ConfigClient {

    public static final Map<String, Integer> VALUES = new HashMap<>();
    private static final Map<String, Tuple<Integer, String>> DEFAULTS = new HashMap<>();

    static {
        DEFAULTS.put("fluidGauge", new Tuple<>(1, "Display fluid gauge for internal tanks on tiles (0 to disable)."));
        DEFAULTS.put("forestryReasonCrouch", new Tuple<>(0, "Only show Forestry machines' important failure reasons when crouching (1 to enable)."));
    }

    public static void init(Configuration config) {
        config.load();

        VALUES.put("fluidGauge", config.getInt("fluidGauge", "Options", DEFAULTS.get("fluidGauge").getFirst(), 0, 1, DEFAULTS.get("fluidGauge").getSecond()));
        VALUES.put("forestryReasonCrouch", config.getInt("forestryReasonCrouch", "Options", DEFAULTS.get("forestryReasonCrouch").getFirst(), 0, 1, DEFAULTS.get("forestryReasonCrouch").getSecond()));

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void set(String key, int value) {
        TOPAddons.configClient.get("Options",
                key,
                DEFAULTS.get(key).getFirst(),
                DEFAULTS.get(key).getSecond(),
                0,
                Names.clientConfigOptions.get(key) == Boolean.TYPE ? 1 : Integer.MAX_VALUE)
            .set(value);
        VALUES.put(key, value);
        TOPAddons.configClient.save();
    }

}
