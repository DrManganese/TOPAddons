package io.github.drmanganese.topaddons.capabilities;

import io.github.drmanganese.topaddons.TopAddons;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElementSyncCapabilityProvider implements ICapabilityProvider {

    private final ElementSyncCapability elementSyncCapability = new ElementSyncCapability();

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == TopAddons.ELT_SYNC_CAP ? LazyOptional.of(() -> (T) this.elementSyncCapability) : LazyOptional.empty();
    }
}
