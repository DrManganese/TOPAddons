package io.github.drmanganese.topaddons.addons.abyssalcraft.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.shinoow.abyssalcraft.common.blocks.tile.TileEntityTransmutator;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.ProbeMode;

public class TransmutatorTileInfo implements ITileInfo<TileEntityTransmutator> {

    private IProgressStyle fuelStyle(EntityPlayer player) {
        return Styles.machineProgress(player, "Fuel")
                .filledColor(0xff00b0b0)
                .alternateFilledColor(0xff02cfcf)
                .suffix("s");
    }

    private IProgressStyle progressStyle(EntityPlayer player) {
        return Styles.machineProgress(player).filledColor(0xff8aa8a5).alternateFilledColor(0xff6b7d7b).prefix("Transmuting");
    }

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileEntityTransmutator tile) {
        if (tile.isTransmuting()) {
            if (tile.transmutatorProcessTime > 0) {
                probeInfo.progress(tile.getProcessProgressScaled(100), 100, progressStyle(player));
            }
            final int itemBurnTime = tile.currentItemBurnTime / 20;
            probeInfo.progress(tile.getBurnTimeRemainingScaled(itemBurnTime), itemBurnTime, fuelStyle(player));
        }
    }
}
