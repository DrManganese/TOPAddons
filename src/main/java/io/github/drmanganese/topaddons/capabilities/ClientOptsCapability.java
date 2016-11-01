package io.github.drmanganese.topaddons.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class ClientOptsCapability implements IClientOptsCapability {

    private static Map<String, Integer> options = new HashMap<>();

    @Override
    public boolean getBoolean(String option) {
        return options.containsKey(option) && options.get(option) != 0;
    }

    @Override
    public int getInt(String option) {
        return options.containsKey(option) ? options.get(option) : 0;
    }

    @Override
    public void setOption(String option, boolean value) {
        if (options.containsKey(option))
            options.put(option, value ? 1 : 0);
    }

    @Override
    public void setOption(String option, int value) {
        if (options.containsKey(option))
            options.put(option, value);
    }

    @Override
    public void setAll(@Nullable Map<String, Integer> newOptions) {
        options = newOptions;
    }

    @Override
    public Map<String, Integer> getAll() {
        return options;
    }

    public static class ClientOptsCapStorage implements Capability.IStorage<IClientOptsCapability> {

        @Override
        public NBTBase writeNBT(Capability<IClientOptsCapability> capability, IClientOptsCapability instance, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT(Capability<IClientOptsCapability> capability, IClientOptsCapability instance, EnumFacing side, NBTBase nbt) {

        }
    }
}
