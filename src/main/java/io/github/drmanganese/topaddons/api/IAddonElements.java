package io.github.drmanganese.topaddons.api;

import mcjty.theoneprobe.api.ITheOneProbe;

/**
 * Interface to implement by addons that have custom {@link mcjty.theoneprobe.api.IElement}s.
 */
public interface IAddonElements {

    /**
     * Register elements with The One Probe in this method using {@link ITheOneProbe#registerElementFactory}.
     * <pre>CUSTOM_ELEMENT = probe.registerElementFactory(ElementCustom::new);</pre>
     *
     * @param probe Instance of main TOP interface
     */
    void registerElements(ITheOneProbe probe);
}
