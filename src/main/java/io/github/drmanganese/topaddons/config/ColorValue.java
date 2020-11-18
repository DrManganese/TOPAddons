package io.github.drmanganese.topaddons.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.regex.Pattern;

public class ColorValue {

    private static final Pattern HEX_PATTERN = Pattern.compile("#\\p{XDigit}{8}");
    public final ForgeConfigSpec.ConfigValue<String> configValue;

    public ColorValue(ForgeConfigSpec.ConfigValue<String> configValue) {
        this.configValue = configValue;
    }

    public static int decodeString(String s) {
        return Long.decode(s).intValue();
    }

    public static boolean test(Object v) {
        return v != null && HEX_PATTERN.matcher((String) v).matches();
    }

    public int getInt() {
        return decodeString(configValue.get());
    }
}
