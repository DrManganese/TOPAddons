package io.github.drmanganese.topaddons;

import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.IAddonConfigProviders;
import io.github.drmanganese.topaddons.api.IAddonElements;
import io.github.drmanganese.topaddons.api.IAddonEntities;
import io.github.drmanganese.topaddons.capabilities.ElementSync;
import io.github.drmanganese.topaddons.elements.top.ElementItemStackBackground;
import io.github.drmanganese.topaddons.elements.top.ElementProgressCentered;

import com.google.common.base.Function;
import mcjty.theoneprobe.api.ITheOneProbe;

import javax.annotation.Nullable;

/**
 * This class is sent to The One Probe during the IMC event. It registers all the providers.
 */
final class TopRegistrar implements Function<ITheOneProbe, Void> {

    static ITheOneProbe probe;

    @Nullable
    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        probe = theOneProbe;
        AddonRegistry.getAddonStream().filter(IAddonBlocks.class::isInstance).forEachOrdered(probe::registerProvider);
        AddonRegistry.getAddonStream().filter(IAddonEntities.class::isInstance).map(IAddonEntities.class::cast).forEachOrdered(probe::registerEntityProvider);
        AddonRegistry.getAddonStream().filter(IAddonElements.class::isInstance).map(IAddonElements.class::cast).forEachOrdered(a -> a.registerElements(probe));
        AddonRegistry.getAddonStream().filter(IAddonConfigProviders.class::isInstance).map(IAddonConfigProviders.class::cast).forEachOrdered(probe::registerProbeConfigProvider);

        ElementSync.registerElement(probe, "centered_progress", ElementProgressCentered::new);
        ElementSync.registerElement(probe, "itemstack_background", ElementItemStackBackground::new);
        return null;
    }
}
