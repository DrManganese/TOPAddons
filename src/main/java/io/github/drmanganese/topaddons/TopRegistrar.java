package io.github.drmanganese.topaddons;

import io.github.drmanganese.topaddons.addons.TopAddon;
import io.github.drmanganese.topaddons.api.IAddonConfigProviders;
import io.github.drmanganese.topaddons.api.IAddonElements;
import io.github.drmanganese.topaddons.elements.top.ElementItemStackBackground;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IElementFactory;
import mcjty.theoneprobe.api.ITheOneProbe;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

/**
 * This class is sent to The One Probe during the IMC event. It registers all the providers.
 */
final class TopRegistrar implements Function<ITheOneProbe, Void> {

    static ITheOneProbe probe;

    @Nullable
    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        probe = theOneProbe;
        // Block info providers
        AddonRegistry.getAddonStream()
            .map(TopAddon::asBlockInfoProvider)
            .flatMap(Optional::stream)
            .forEachOrdered(probe::registerProvider);
        // Entity info providers
        AddonRegistry.getAddonStream()
            .map(TopAddon::asEntityInfoProvider)
            .flatMap(Optional::stream)
            .forEachOrdered(probe::registerEntityProvider);
        // Custom elements
        AddonRegistry.getAddonStream()
            .filter(IAddonElements.class::isInstance)
            .map(IAddonElements.class::cast)
            .flatMap(a -> a.getElements().stream())
            .forEachOrdered(this::registerElement);
        // Probe config setters
        AddonRegistry.getAddonStream()
            .filter(IAddonConfigProviders.class::isInstance)
            .map(IAddonConfigProviders.class::cast)
            .forEachOrdered(probe::registerProbeConfigProvider);

        registerElement(Pair.of(new ResourceLocation(TopAddons.MOD_ID, "itemstack_background"), ElementItemStackBackground::new));
        return null;
    }

    private IElementFactory elementFactory(Pair<ResourceLocation, Function<FriendlyByteBuf, IElement>> pair) {
        return new IElementFactory() {
            @Override
            public IElement createElement(FriendlyByteBuf friendlyByteBuf) {
                return pair.getRight().apply(friendlyByteBuf);
            }

            @Override
            public ResourceLocation getId() {
                return pair.getLeft();
            }
        };
    }

    private void registerElement(Pair<ResourceLocation, Function<FriendlyByteBuf, IElement>> pair) {
        probe.registerElementFactory(elementFactory(pair));
    }
}
