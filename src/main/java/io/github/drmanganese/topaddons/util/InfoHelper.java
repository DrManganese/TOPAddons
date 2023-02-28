package io.github.drmanganese.topaddons.util;

import io.github.drmanganese.topaddons.addons.top.elements.ElementItemStackBackground;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.energy.IEnergyStorage;

import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.config.Config;

@SuppressWarnings("UnusedReturnValue")
public final class InfoHelper {

    /**
     * "<tt>prefix</tt>: <tt>info</tt>"
     */
    public static IProbeInfo textPrefixed(IProbeInfo probeInfo, String prefix, String info) {
        return probeInfo.text(CompoundText.createLabelInfo(prefix + ":", " " + info));
    }

    public static IProbeInfo itemStackBackground(IProbeInfo probeInfo, Player player, ItemStack itemStack, int color) {
        return probeInfo.element(new ElementItemStackBackground(itemStack, color, new ItemStyle()));
    }

    public static IProbeInfo energyBar(IProbeInfo probeInfo, IEnergyStorage iEnergyStorage) {
        final int energy = iEnergyStorage.getEnergyStored();
        final int maxEnergy = iEnergyStorage.getMaxEnergyStored();
        if (Config.getDefaultConfig().getRFMode() == 1) {
            return probeInfo.progress(
                energy,
                maxEnergy,
                probeInfo.defaultProgressStyle()
                    .suffix("FE")
                    .filledColor(Config.rfbarFilledColor)
                    .alternateFilledColor(Config.rfbarAlternateFilledColor)
                    .borderColor(Config.rfbarBorderColor)
                    .numberFormat(Config.rfFormat.get()));
        } else {
            final CompoundText compoundText = CompoundText.create().style(TextStyleClass.PROGRESS);
            final Component content = ElementProgress.format(energy, Config.rfFormat.get(), Component.literal("FE"));
            return probeInfo.text(compoundText.text("FE: " + content));
        }
    }

    public static NonNullConsumer<IEnergyStorage> energyBar(IProbeInfo probeInfo) {
        return iEnergyStorage -> energyBar(probeInfo, iEnergyStorage);
    }
}
