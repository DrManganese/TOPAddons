package io.github.drmanganese.topaddons.styles;

import io.github.drmanganese.topaddons.addons.forge.ForgeAddon;
import io.github.drmanganese.topaddons.config.Config;

import net.minecraft.entity.player.PlayerEntity;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.ILayoutStyle;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;

public final class Styles {

    public static IProgressStyle machineProgress(PlayerEntity player) {
        return machineProgress(player, "Progress");
    }

    public static IProgressStyle machineProgress(PlayerEntity player, String prefixWord) {
        final int borderColor = Config.getSyncedColor(player, ForgeAddon.machineProgressBorderColor);
        final int backgroundColor = Config.getSyncedColor(player, ForgeAddon.machineProgressBackgroundColor);
        return new ProgressStyle()
            .borderColor(borderColor)
            .backgroundColor(backgroundColor)
            .prefix(prefixWord + ": ")
            .suffix("%");
    }

    public static ILayoutStyle horiCentered() {
        return new LayoutStyle().alignment(ElementAlignment.ALIGN_CENTER);
    }
}
