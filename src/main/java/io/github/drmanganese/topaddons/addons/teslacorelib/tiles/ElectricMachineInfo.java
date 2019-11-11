package io.github.drmanganese.topaddons.addons.teslacorelib.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;

import io.github.drmanganese.topaddons.styles.Styles;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.config.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import net.ndrei.teslacorelib.tileentities.ElectricMachine;

/**
 * Work buffer.
 */
public class ElectricMachineInfo implements ITileInfo<ElectricMachine> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, ElectricMachine tile) {

        if (!tile.isPaused() && tile.getHasWorkBuffer()) {
            if (probeMode == ProbeMode.NORMAL) {
                probeInfo.horizontal()
                        .text(TextStyleClass.LABEL + "Buffer:")
                        .progress(tile.getWorkEnergyStored(), tile.getWorkEnergyCapacity(),
                                probeInfo.defaultProgressStyle()
                                        .filledColor(Config.rfbarFilledColor)
                                        .alternateFilledColor(Config.rfbarAlternateFilledColor)
                                        .borderColor(Config.rfbarBorderColor)
                                        .numberFormat(NumberFormat.NONE)
                                        .width(50)
                                        .height(8));
            } else {
                probeInfo.horizontal(Styles.horiCentered())
                        .text(TextStyleClass.LABEL + "Buffer: ")
                        .progress(tile.getWorkEnergyStored(), tile.getWorkEnergyCapacity(),
                                probeInfo.defaultProgressStyle()
                                        .filledColor(Config.rfbarFilledColor)
                                        .alternateFilledColor(Config.rfbarAlternateFilledColor)
                                        .borderColor(Config.rfbarBorderColor)
                                        .numberFormat(Config.rfFormat)
                                        .width(50)
                                        .suffix("RF"));
            }
        }
    }
}
