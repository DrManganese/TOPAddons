package io.github.drmanganese.topaddons.addons.industrialforegoing.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.buuz135.industrial.tile.world.LaserBaseTile;

/**
 * Work percentage.
 */
public class LaserBaseInfo implements ITileInfo<LaserBaseTile> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, LaserBaseTile tile) {
        probeInfo.progress(tile.getCurrentWork(), tile.getMaxWork(), probeInfo.defaultProgressStyle()
                .prefix("Work: ").suffix("%")
                .filledColor(0xffb5b204)
                .alternateFilledColor(0xff8e8c03));
    }
}
