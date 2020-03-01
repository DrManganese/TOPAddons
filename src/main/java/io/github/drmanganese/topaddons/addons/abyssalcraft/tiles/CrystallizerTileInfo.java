package io.github.drmanganese.topaddons.addons.abyssalcraft.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.shinoow.abyssalcraft.common.blocks.tile.TileEntityCrystallizer;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.ProbeMode;

public class CrystallizerTileInfo implements ITileInfo<TileEntityCrystallizer> {

    private IProgressStyle fuelStyle(EntityPlayer player) {
        return Styles.machineProgress(player, "Fuel")
                .filledColor(0xfff0ca00)
                .alternateFilledColor(0xffe5a800)
                .suffix("s");
    }

    private IProgressStyle progressStyle(EntityPlayer player) {
        return Styles.machineProgress(player, "Crystallizing").filledColor(0xffdf5151).alternateFilledColor(0xffaf2929);
    }

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileEntityCrystallizer tile) {
        if (tile.isCrystallizing()) {
            if (tile.crystallizerFormTime > 0) {
                probeInfo.progress(tile.getFormProgressScaled(100), 100, progressStyle(player));
            }
            final int itemShapeTime = tile.crystallizerShapeTime / 20;
            probeInfo.progress(tile.getShapeTimeRemainingScaled(itemShapeTime), itemShapeTime, fuelStyle(player));
        }
    }
}
