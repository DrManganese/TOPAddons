package io.github.drmanganese.topaddons.addons.vanilla.blocks;

import io.github.drmanganese.topaddons.api.IBlockInfo;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import static mcjty.theoneprobe.api.TextStyleClass.*;

public class CocoaInfo implements IBlockInfo {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData) {
        int age = blockState.getValue(BlockCocoa.AGE);
        int maxAge = 2;
        if (age == maxAge) {
            probeInfo.text(OK + "Fully grown");
        } else {
            probeInfo.text(LABEL + "Growth: " + WARNING + (age * 100) / maxAge + "%");
        }
    }
}
