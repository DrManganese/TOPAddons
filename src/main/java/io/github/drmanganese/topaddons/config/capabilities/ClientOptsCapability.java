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
    private final Map<String, Integer> elements = new HashMap<>();

    @Override
    public boolean getBoolean(String option) {
        return options.containsKey(option) && options.get(option) != 0;
    }

    @Override
    public int getInt(String option) {
        return options.getOrDefault(option, 0);
    }

    @Override
    public Map<String, Integer> getAllOptions() {
        return options;
    }

    @Override
    public void setAllOptions(@Nonnull Map<String, Integer> newOptions) {
        newOptions.forEach(options::put);
    }

    @Override
    public void setAllElementIds(@Nonnull Map<String, Integer> elementIds) {
        elementIds.forEach(elements::put);
    }

    @Override
    public Map<String, Integer> getAllElementIds() {
        return elements;
    }

    @Override
    public int getElementId(String name) {
        return elements.get(name);
    }

    public static class Storage implements Capability.IStorage<IClientOptsCapability> {

        @Override
        public NBTTagCompound writeNBT(Capability<IClientOptsCapability> capability, IClientOptsCapability instance, EnumFacing side) {
            NBTTagCompound tag = new NBTTagCompound();
            instance.getAllOptions().forEach(tag::setInteger);
            instance.getAllElementIds().forEach(tag::setInteger);
            return null;
        }

        @Override
        public void readNBT(Capability<IClientOptsCapability> capability, IClientOptsCapability instance, EnumFacing side, NBTBase nbt) {

        }
    }
}
