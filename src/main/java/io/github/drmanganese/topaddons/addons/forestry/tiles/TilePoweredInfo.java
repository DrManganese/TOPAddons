package io.github.drmanganese.topaddons.addons.forestry.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableMap;
import forestry.core.tiles.TilePowered;
import forestry.factory.tiles.TileCarpenter;
import forestry.factory.tiles.TileFermenter;
import forestry.factory.tiles.TileStill;

public class TilePoweredInfo implements ITileInfo<TilePowered> {

    private static final ImmutableMap<Class<? extends TilePowered>, Tuple<Integer, Integer>> TILE_COLOR_MAP = ImmutableMap.of(
            TileFermenter.class, new Tuple<>(0xff99e605, 0xff91d905),
            TileCarpenter.class, new Tuple<>(0xffe3e605, 0xffd6d905),
            TileStill.class, new Tuple<>(0xff35e600, 0xff32d900)
    );

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TilePowered tile) {
        if (tile.hasWork() && tile.getWorkCounter() > 0) {
            final int workPct = 100 * tile.getWorkCounter() / tile.getTicksPerWorkCycle();
            Tuple<Integer, Integer> colors = TILE_COLOR_MAP.getOrDefault(tile.getClass(), new Tuple<>(0xffaaaaaa, 0xff888888));
            probeInfo.progress(workPct, 100, Styles.machineProgress(player)
                    .filledColor(colors.getFirst())
                    .alternateFilledColor(colors.getSecond()));
        }
    }
}
