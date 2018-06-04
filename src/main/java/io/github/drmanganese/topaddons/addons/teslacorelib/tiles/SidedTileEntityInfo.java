package io.github.drmanganese.topaddons.addons.teslacorelib.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import net.ndrei.teslacorelib.tileentities.SidedTileEntity;

/**
 * Paused status.
 */
public class SidedTileEntityInfo implements ITileInfo<SidedTileEntity> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, SidedTileEntity tile) {
        if (tile.isPaused()) {
            probeInfo.text(TextFormatting.LIGHT_PURPLE + "\u2759\u2759 " + TextStyleClass.LABEL + "Paused");
        }
    }
}
