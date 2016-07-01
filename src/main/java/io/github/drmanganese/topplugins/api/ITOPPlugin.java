package io.github.drmanganese.topplugins.api;

import java.util.List;

import mcjty.theoneprobe.api.IProbeConfigProvider;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.IProbeInfoProvider;

/**
 * Interface each plugin has to implement (through extending {@link io.github.drmanganese.topplugins.plugins.PluginBlank}
 * or directly), the {@link io.github.drmanganese.topplugins.plugins.PluginManager} will look for it
 * if it finds an {@link TOPPlugin} annotation.
 */
public interface ITOPPlugin extends IProbeInfoProvider, IProbeInfoEntityProvider, IProbeConfigProvider {

    /**
     * @return <i>true</i> if the plugin adds helmets
     */
    boolean hasHelmets();

    /**
     * @return a {@link List} containing {@link ItemArmorProbed} classes
     */
    List<Class<? extends ItemArmorProbed>> getHelmets();

    void registerElements();

}
