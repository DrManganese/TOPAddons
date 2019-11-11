package io.github.drmanganese.topaddons.styles;

import mcjty.theoneprobe.api.IProgressStyle;
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

    public static IProgressStyle machineProgress() {
        return machineProgress("Progress");
    }
}
