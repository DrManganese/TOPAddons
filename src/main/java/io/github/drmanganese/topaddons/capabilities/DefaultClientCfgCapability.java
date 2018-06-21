package io.github.drmanganese.topaddons.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class DefaultClientCfgCapability implements IClientCfgCapability {

    private final Map<String, String> valueMap = new HashMap<>();

    @Override
    public Map<String, String> getAll() {
        return this.valueMap;
    }

    @Override
    public void setAll(Map<String, String> newMap) {
        this.valueMap.clear();
        this.valueMap.putAll(newMap);
    }

    @Override
    public String getString(String key) {
        return valueMap.get(key);
    }

    @Override
    public int getInt(String key) {
        return Integer.valueOf(valueMap.get(key));
    }

    @Override
    public boolean getBool(String key) {
        return this.valueMap.get(key).equals("true");
    }

    public static class Storage implements Capability.IStorage<IClientCfgCapability> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IClientCfgCapability> capability, IClientCfgCapability instance, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT(Capability<IClientCfgCapability> capability, IClientCfgCapability instance, EnumFacing side, NBTBase nbt) {

        }
    }
}
