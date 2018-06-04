package io.github.drmanganese.topaddons.addons.industrialforegoing.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.buuz135.industrial.tile.misc.DyeMixerTile;

/**
 * Percentage for each RGB dye.
 */
public class DyeMixerInfo implements ITileInfo<DyeMixerTile> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, DyeMixerTile tile) {
        probeInfo.vertical(probeInfo.defaultLayoutStyle().spacing(-2))
                .progress(tile.getR() / 3, 100, probeInfo.defaultProgressStyle()
                        .prefix("R: ").suffix("%")
                        .filledColor(0xffd92a2a)
                        .alternateFilledColor(0xffa41d1d)
                        .borderColor(0)
                        .backgroundColor(0x551f0000))
                .progress(tile.getG() / 3, 100, probeInfo.defaultProgressStyle()
                        .prefix("G: ").suffix("%")
                        .filledColor(0xff50db34)
                        .alternateFilledColor(0xff37b01f)
                        .borderColor(0)
                        .backgroundColor(0x55001f00))
                .progress(tile.getB() / 3, 100, probeInfo.defaultProgressStyle()
                        .prefix("B: ").suffix("%")
                        .filledColor(0xff5d93f2)
                        .alternateFilledColor(0xff2263c3)
                        .borderColor(0)
                        .backgroundColor(0x5500001f));
    }
}
