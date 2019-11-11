package io.github.drmanganese.topaddons.addons.forestry.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.ProgressStyleForestryMultiColored;

import io.github.drmanganese.topaddons.styles.Styles;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import forestry.core.tiles.TileAnalyzer;

public class TileAnalyzerInfo implements ITileInfo<TileAnalyzer> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileAnalyzer tile) {
        if (tile.hasWork()) {
            final int workPct = 100 * tile.getWorkCounter() / tile.getTicksPerWorkCycle();
            probeInfo.horizontal(Styles.horiCentered())
                    .item(tile.getIndividualOnDisplay())
                    .progress(workPct, 100, new ProgressStyleForestryMultiColored(workPct)
                    .showText(true)
                    .prefix("Analysis: "));
        }
    }
}
