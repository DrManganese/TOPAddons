package io.github.drmanganese.topaddons.addons.vanilla.blocks;

import io.github.drmanganese.topaddons.api.IBlockInfo;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import mcjty.theoneprobe.api.*;

import javax.annotation.Nonnull;

import static mcjty.theoneprobe.api.TextStyleClass.INFO;

public class BeehiveInfo implements IBlockInfo, ITileInfo<BeehiveBlockEntity> {

    public static final BeehiveInfo INSTANCE = new BeehiveInfo();

    public static final int MAX_HONEY_LEVEL = BlockStateProperties.LEVEL_HONEY.getPossibleValues()
        .stream()
        .max(Integer::compareTo)
        .orElse(5);

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData) {
        final int honeyLevel = blockState.getValue(BlockStateProperties.LEVEL_HONEY);
        final IProgressStyle progressStyle = Styles.machineProgress(player, "Honey")
            .filledColor(0xfffbdc75)
            .alternateFilledColor(0xfff7ce46)
            .alignment(ElementAlignment.ALIGN_CENTER)
            .suffix("/" + MAX_HONEY_LEVEL);
        probeInfo.progress(honeyLevel, MAX_HONEY_LEVEL, progressStyle);
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull BeehiveBlockEntity tile) {
        probeInfo
            .horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
            .entity("minecraft:bee", probeInfo.defaultEntityStyle())
            .text(
                CompoundText.create()
                    .label("topaddons.vanilla:bees")
                    .text(": ")
                    .style(INFO)
                    .text(String.valueOf(tile.getOccupantCount()))
            );
    }
}
