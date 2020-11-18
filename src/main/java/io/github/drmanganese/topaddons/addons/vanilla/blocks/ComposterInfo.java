package io.github.drmanganese.topaddons.addons.vanilla.blocks;

import io.github.drmanganese.topaddons.api.IBlockInfo;
import io.github.drmanganese.topaddons.styles.Styles;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.ProbeMode;

public class ComposterInfo implements IBlockInfo {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData) {
        final int pct = (100 * blockState.get(ComposterBlock.LEVEL)) / 8;
        InfoHelper.progressCentered(probeInfo, player, pct, 100, progressStyle(player), pct == 100 ? "topaddons.vanilla:composter_ready" : null);
    }

    private static IProgressStyle progressStyle(PlayerEntity player) {
        return Styles.machineProgress(player)
            .filledColor(0xff523c18)
            .alternateFilledColor(0xff4a3018)
            .prefix("")
            .suffix("%");
    }
}
