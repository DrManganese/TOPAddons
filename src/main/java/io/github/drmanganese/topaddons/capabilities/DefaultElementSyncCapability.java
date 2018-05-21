package io.github.drmanganese.topaddons.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class DefaultElementSyncCapability implements IElementSyncCapability {
    private final Map<String, Integer> elementIdMap = new HashMap<>();

    @Override
    public int getElementId(String name) {
        return this.elementIdMap.getOrDefault(name, -1);
    }

    @Override
    public void setElementIds(Map<String, Integer> elementIds) {
        this.elementIdMap.clear();
        this.elementIdMap.putAll(elementIds);
    }

    @Override
    public Map<String, Integer> getAllElementIds() {
        return this.elementIdMap;
    }

    public static class Storage implements Capability.IStorage<IElementSyncCapability> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IElementSyncCapability> capability, IElementSyncCapability instance, EnumFacing side) {
            NBTTagCompound tag = new NBTTagCompound();
            instance.getAllElementIds().forEach(tag::setInteger);
            return null;
        }

        @Override
        public void readNBT(Capability<IElementSyncCapability> capability, IElementSyncCapability instance, EnumFacing side, NBTBase nbt) {

        }
    }
}
