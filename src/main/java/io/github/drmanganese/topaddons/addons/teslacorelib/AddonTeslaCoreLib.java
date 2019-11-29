package io.github.drmanganese.topaddons.addons.teslacorelib;

import io.github.drmanganese.topaddons.addons.teslacorelib.tiles.ElectricGeneratorInfo;
import io.github.drmanganese.topaddons.addons.teslacorelib.tiles.ElectricMachineInfo;
import io.github.drmanganese.topaddons.addons.teslacorelib.tiles.SidedTileEntityInfo;
import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.api.TOPAddon;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.tileentity.TileEntity;
import net.ndrei.teslacorelib.tileentities.ElectricGenerator;
import net.ndrei.teslacorelib.tileentities.ElectricMachine;
import net.ndrei.teslacorelib.tileentities.SidedTileEntity;

import javax.annotation.Nonnull;

@TOPAddon(dependency = "teslacorelib", order = 9)
public class AddonTeslaCoreLib implements IAddonBlocks {

    private final ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> TILES;

    public AddonTeslaCoreLib() {
        ImmutableMultimap.Builder<Class<? extends TileEntity>, ITileInfo> mapBuilder = ImmutableMultimap.builder();
        mapBuilder.put(SidedTileEntity.class, new SidedTileEntityInfo());
        mapBuilder.put(ElectricGenerator.class, new ElectricGeneratorInfo());
        mapBuilder.put(ElectricMachine.class, new ElectricMachineInfo());
        TILES = mapBuilder.build();
    }

    @Nonnull
    @Override
    public ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> getTiles() {
        return TILES;
    }
}
