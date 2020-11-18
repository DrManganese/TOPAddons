package io.github.drmanganese.topaddons.api;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Addons which implement this interface provide configuration options.
 */
public interface IAddonConfig {

    void buildConfig(ForgeConfigSpec.Builder builder, ModConfig.Type type);

    default List<ForgeConfigSpec.ConfigValue<?>> getClientConfigValuesToSync() {
        return new ArrayList<>();
    }
}
