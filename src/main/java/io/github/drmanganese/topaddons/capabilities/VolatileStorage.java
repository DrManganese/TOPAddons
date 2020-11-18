package io.github.drmanganese.topaddons.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class VolatileStorage<T> implements Capability.IStorage<T> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<T> capability, T instance, Direction side) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
        throw new UnsupportedOperationException();
    }
}
