package io.github.drmanganese.topaddons.addons.forestry.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import mcjty.theoneprobe.api.IProgressStyle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import forestry.factory.tiles.TileFabricator;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

public class TileThermionicFabInfo implements ITileInfo<TileFabricator> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileFabricator tile) {
        final int heat = tile.getHeatScaled(5000);
        final int melt = tile.getMeltingPointScaled(5000);
        if (heat > 0) {
            try {
                final IProgressStyle defaultProgressStyle = Styles.machineProgress(player);
                probeInfo.progress(heat, melt == 0 ? 5000 : melt, defaultProgressStyle
                        .showText(false)
                        .filledColor(0xffd90f00).alternateFilledColor(0xffcc0e00).height(8)
                        .borderColor(heat >= melt ? 0xff373215 : defaultProgressStyle.getBorderColor()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
