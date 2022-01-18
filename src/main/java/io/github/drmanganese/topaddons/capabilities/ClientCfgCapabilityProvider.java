package io.github.drmanganese.topaddons.capabilities;

import io.github.drmanganese.topaddons.TopAddons;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ClientCfgCapabilityProvider implements ICapabilityProvider {

    private final ClientCfgCapability clientCfgCapability = new ClientCfgCapability();

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == TopAddons.CLIENT_CFG_CAP ? LazyOptional.of(() -> (T) this.clientCfgCapability) : LazyOptional.empty();
    }
}
