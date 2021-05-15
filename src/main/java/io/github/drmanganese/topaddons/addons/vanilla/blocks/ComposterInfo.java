package io.github.drmanganese.topaddons.addons.vanilla.blocks;

import io.github.drmanganese.topaddons.api.IBlockInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import mcjty.theoneprobe.api.*;

public class ComposterInfo implements IBlockInfo {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData) {
        final int compostLevel = blockState.get(ComposterBlock.LEVEL);
        final IProgressStyle progressStyle =  Styles.machineProgress(player)
            .filledColor(0xff523c18)
            .alternateFilledColor(0xff4a3018)
            .suffix("/8").prefix("")
            .alignment(ElementAlignment.ALIGN_CENTER);
        probeInfo.progress(compostLevel, 8,progressStyle);
    }
}
