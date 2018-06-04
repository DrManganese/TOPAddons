package io.github.drmanganese.topaddons.addons.teslacorelib;

import io.github.drmanganese.topaddons.addons.teslacorelib.tiles.ElectricGeneratorInfo;
import io.github.drmanganese.topaddons.addons.teslacorelib.tiles.ElectricMachineInfo;
import io.github.drmanganese.topaddons.addons.teslacorelib.tiles.SidedTileEntityInfo;
import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.reference.Reference;

import net.minecraft.tileentity.TileEntity;

import com.google.common.collect.ImmutableMap;
import net.ndrei.teslacorelib.tileentities.ElectricGenerator;
import net.ndrei.teslacorelib.tileentities.ElectricMachine;
import net.ndrei.teslacorelib.tileentities.SidedTileEntity;

import javax.annotation.Nonnull;

@TOPAddon(dependency = "teslacorelib", order = 9)
public class AddonTeslaCoreLib implements IAddonBlocks {

    private final ImmutableMap<Class<? extends TileEntity>, ITileInfo> TILES;

    public AddonTeslaCoreLib() {
        ImmutableMap.Builder<Class<? extends TileEntity>, ITileInfo> mapBuilder = ImmutableMap.builder();
        mapBuilder.put(SidedTileEntity.class, new SidedTileEntityInfo());
        mapBuilder.put(ElectricGenerator.class, new ElectricGeneratorInfo());
        mapBuilder.put(ElectricMachine.class, new ElectricMachineInfo());
        TILES = mapBuilder.build();
    }

    @Nonnull
    @Override
    public String getID() {
        return Reference.MOD_ID + ":teslacorelib";
    }

    @Nonnull
    @Override
    public ImmutableMap<Class<? extends TileEntity>, ITileInfo> getTiles() {
        return TILES;
    }
}
