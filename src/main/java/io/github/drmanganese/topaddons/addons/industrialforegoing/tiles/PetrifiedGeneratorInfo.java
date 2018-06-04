package io.github.drmanganese.topaddons.addons.industrialforegoing.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.util.Formatting;
import io.github.drmanganese.topaddons.util.InfoHelper;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.buuz135.industrial.tile.generator.PetrifiedFuelGeneratorTile;

/**
 * Burning time.
 */
public class PetrifiedGeneratorInfo implements ITileInfo<PetrifiedFuelGeneratorTile> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, PetrifiedFuelGeneratorTile tile) {
        if (!tile.isPaused() && tile.getGeneratedPowerReleaseRate() != 0) {
            InfoHelper.textPrefixed(probeInfo, "Burning time", Formatting.timeInMinutesAndOrSeconds((int) (tile.getGeneratedPowerStored() / 2 / tile.getGeneratedPowerReleaseRate()), Formatting.TimeUnit.TICKS));
        }
    }
}
