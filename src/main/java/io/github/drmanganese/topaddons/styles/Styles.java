package io.github.drmanganese.topaddons.styles;

import io.github.drmanganese.topaddons.capabilities.IClientCfgCapability;
import io.github.drmanganese.topaddons.util.PlayerHelper;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.ILayoutStyle;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.entity.player.EntityPlayer;

public final class Styles {

    public static IProgressStyle machineProgress(EntityPlayer player) {
        return machineProgress(player, "Progress");
    }

    public static IProgressStyle machineProgress(EntityPlayer player, String prefixWord) {
        final IClientCfgCapability playerCap = PlayerHelper.getSync(player);
        final int borderColor = playerCap.getInt("machineProgressBorderColor");
        final int backgroundColor = playerCap.getInt("machineProgressBackgroundColor");
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
