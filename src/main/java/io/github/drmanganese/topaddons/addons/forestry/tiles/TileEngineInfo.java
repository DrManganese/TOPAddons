package io.github.drmanganese.topaddons.addons.forestry.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import forestry.core.tiles.TileEngine;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

import java.text.DecimalFormat;

public class TileEngineInfo implements ITileInfo<TileEngine> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileEngine tile) {
        InfoHelper.generatorProducing(probeInfo, tile.getCurrentOutput());
        if (probeMode == ProbeMode.EXTENDED) {
            InfoHelper.textPrefixed(probeInfo, "Heat", new DecimalFormat("#.0").format((double) tile.getHeat() / 10 + 20) + "°C");
        }
    }
}
