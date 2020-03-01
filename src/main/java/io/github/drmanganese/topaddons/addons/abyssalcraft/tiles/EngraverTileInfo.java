package io.github.drmanganese.topaddons.addons.abyssalcraft.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.shinoow.abyssalcraft.common.blocks.tile.TileEntityEngraver;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.ProbeMode;

public class EngraverTileInfo implements ITileInfo<TileEntityEngraver> {

    private IProgressStyle progressStyle(EntityPlayer player) {
        return Styles.machineProgress(player, "Engraving").filledColor(0xff9D9A97).alternateFilledColor(0xffB1AFAC);
    }

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileEntityEngraver tile) {
        if (tile.engraverProcessTime > 0)
            probeInfo.progress(tile.getProcessProgressScaled(100), 100, progressStyle(player));
    }
}
