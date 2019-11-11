package io.github.drmanganese.topaddons.addons.forestry.tiles;

import io.github.drmanganese.topaddons.styles.Styles;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import forestry.core.tiles.TilePowered;
import forestry.factory.tiles.TileFermenter;

public class TileFermenterInfo extends TilePoweredInfo {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TilePowered tile) {
        super.getInfo(probeMode, probeInfo, player, world, blockState, hitData, tile);
        if (probeMode == ProbeMode.EXTENDED) {
            final TileFermenter fermenter = (TileFermenter) tile;
            final int burnTime = fermenter.getBurnTimeRemainingScaled(100);
            if (burnTime > 0) {
                IProbeInfo hori = probeInfo.horizontal(Styles.horiCentered());
                if (!fermenter.getStackInSlot(1).isEmpty()) {
                    hori.item(fermenter.getStackInSlot(1));
                }
                hori.progress(burnTime, 100, Styles.machineProgress(player).filledColor(0xffd90f00).alternateFilledColor(0xffcc0e00).width(81).showText(false));
            }
        }
    }
}
