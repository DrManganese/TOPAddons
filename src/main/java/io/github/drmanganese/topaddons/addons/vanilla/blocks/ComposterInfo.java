package io.github.drmanganese.topaddons.addons.vanilla.blocks;

import io.github.drmanganese.topaddons.api.IBlockInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;

import mcjty.theoneprobe.api.*;

public class ComposterInfo implements IBlockInfo {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData) {
        final int compostLevel = blockState.getValue(ComposterBlock.LEVEL);
        final IProgressStyle progressStyle =  Styles.machineProgress(player)
            .filledColor(0xff523c18)
            .alternateFilledColor(0xff4a3018)
            .suffix("/8").prefix("")
            .alignment(ElementAlignment.ALIGN_CENTER);
        probeInfo.progress(compostLevel, 8,progressStyle);
    }
}
