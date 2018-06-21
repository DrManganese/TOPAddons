package io.github.drmanganese.topaddons.proxy;

import io.github.drmanganese.topaddons.api.IAddonConfig;

import net.minecraftforge.common.config.Configuration;

import java.util.List;

public interface IProxy {

    /**
     * Called during the <tt>preInit</tt> phase and when the config gui is closed.
     *
     * @param config The mod's configuration object
     * @param addons List of registered addons which provide configuration options
     */
    void updateConfigs(Configuration config, List<IAddonConfig> addons);
}
