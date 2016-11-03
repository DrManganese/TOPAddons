package io.github.drmanganese.topaddons.config.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class ClientOptsCapability implements IClientOptsCapability {

    private final Map<String, Integer> options = new HashMap<>();

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
    public Map<String, Integer> getAll() {
        return options;
    }

    @Override
    public void setAll(@Nonnull Map<String, Integer> newOptions) {
        newOptions.forEach(options::put);
    }

    public static class Storage implements Capability.IStorage<IClientOptsCapability> {

        @Override
        public NBTTagCompound writeNBT(Capability<IClientOptsCapability> capability, IClientOptsCapability instance, EnumFacing side) {
            NBTTagCompound tag = new NBTTagCompound();
            instance.getAll().forEach(tag::setInteger);
            return null;
            //return tag;
        }

        @Override
        public void readNBT(Capability<IClientOptsCapability> capability, IClientOptsCapability instance, EnumFacing side, NBTBase nbt) {

        }
    }
}
