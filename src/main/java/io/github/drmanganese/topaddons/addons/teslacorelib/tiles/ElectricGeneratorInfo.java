package io.github.drmanganese.topaddons.addons.teslacorelib.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.util.InfoHelper;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import net.ndrei.teslacorelib.tileentities.ElectricGenerator;

/**
 * Rf production.
 */
public class ElectricGeneratorInfo implements ITileInfo<ElectricGenerator> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, ElectricGenerator tile) {
        if (!tile.isPaused()) {
            InfoHelper.generatorProducing(probeInfo, tile.getGeneratedPowerStored() > 0 ? (int) tile.getGeneratedPowerReleaseRate() : 0);
        }
    }
}
