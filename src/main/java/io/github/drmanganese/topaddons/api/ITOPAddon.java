package io.github.drmanganese.topaddons.api;

import io.github.drmanganese.topaddons.addons.AddonBlank;
import io.github.drmanganese.topaddons.addons.AddonManager;

import java.util.List;

import mcjty.theoneprobe.api.IProbeConfigProvider;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.IProbeInfoProvider;

/**
 * Interface each addon has to implement (through extending {@link AddonBlank}
 * or directly), the {@link AddonManager} will look for it
 * if it finds a {@link TOPAddon} annotation.
 */
public interface ITOPAddon extends IProbeInfoProvider, IProbeInfoEntityProvider, IProbeConfigProvider {

    /**
     * @return <i>true</i> if the addon adds helmets
     */
    boolean hasHelmets();

    /**
     * @return a {@link List} containing {@link ItemArmorProbed} classes
     */
    List<Class<? extends ItemArmorProbed>> getHelmets();

    /**
     * Custom {@link mcjty.theoneprobe.api.IElement}s have to be registered here.<p>
     *
     * <pre>
     * Example: {@code
     * ELEMENT_CUSTOM = TOPRegistrar.GetTheOneProbe.probe.registerElementFactory(ElementCustom::new);
     * }
     * </pre>
     * Where <i>ELEMENT_CUSTOM</i> is a static integer stored in the addon's class.
     */
    void registerElements();

}
