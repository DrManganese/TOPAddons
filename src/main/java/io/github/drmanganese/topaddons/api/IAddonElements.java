package io.github.drmanganese.topaddons.api;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import mcjty.theoneprobe.api.IElement;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Function;

/**
 * Interface to implement by addons that have custom {@link mcjty.theoneprobe.api.IElement}s.
 */
public interface IAddonElements {

    /**
     * Provide a list of elements and their ID to register elements with The One Probe in this method.
     * <pre>return Collections.singletonList(Pair.of(new ResourceLocation("id"), ElementCustom::new))</pre>
     *
     * @return Collection of ResourceLocation-Element pairs.
     */
    List<Pair<ResourceLocation, Function<FriendlyByteBuf, IElement>>> getElements();
}
