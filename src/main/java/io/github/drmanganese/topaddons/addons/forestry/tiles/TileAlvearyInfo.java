package io.github.drmanganese.topaddons.addons.forestry.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import forestry.apiculture.multiblock.TileAlveary;

public class TileAlvearyInfo implements ITileInfo<TileAlveary> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileAlveary tile) {
        TileBeeHousingInfo.getInfo(probeInfo, tile.getBeeInventory(), tile.getBeekeepingLogic());
    }
}
