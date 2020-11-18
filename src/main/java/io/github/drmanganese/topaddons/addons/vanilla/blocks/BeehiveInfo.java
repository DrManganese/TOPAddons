package io.github.drmanganese.topaddons.addons.vanilla.blocks;

import io.github.drmanganese.topaddons.api.IBlockInfo;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.BeehiveTileEntity;
import net.minecraft.world.World;

import mcjty.theoneprobe.api.*;

import javax.annotation.Nonnull;

import static mcjty.theoneprobe.api.TextStyleClass.INFO;

public class BeehiveInfo implements IBlockInfo, ITileInfo<BeehiveTileEntity> {

    public static final BeehiveInfo INSTANCE = new BeehiveInfo();

    public static final int MAX_HONEY_LEVEL = BlockStateProperties.HONEY_LEVEL.getAllowedValues().stream().max(Integer::compareTo).orElse(5);

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData) {
        final int pct = (100 * blockState.get(BlockStateProperties.HONEY_LEVEL)) / MAX_HONEY_LEVEL;
        InfoHelper.progressCentered(probeInfo, player, pct, 100, progressStyle(player), pct == 100 ? "topaddons.vanilla:beehive_ready" : null);
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData, @Nonnull BeehiveTileEntity tile) {
        probeInfo
            .horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
            .entity("minecraft:bee", probeInfo.defaultEntityStyle())
            .text(
                CompoundText.create()
                    .label("topaddons.vanilla:bees")
                    .text(": ")
                    .style(INFO)
                    .text(String.valueOf(tile.getBeeCount()))
            );
    }

    private static IProgressStyle progressStyle(PlayerEntity player) {
        return Styles.machineProgress(player)
            .filledColor(0xfffbdc75)
            .alternateFilledColor(0xfff7ce46)
            .prefix("Honey: ")
            .suffix("%");
    }
}
