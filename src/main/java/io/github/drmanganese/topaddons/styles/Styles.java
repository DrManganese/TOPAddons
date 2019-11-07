package io.github.drmanganese.topaddons.styles;

import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;

public final class Styles {

    public static IProgressStyle machineProgress(String prefix) {
        return new ProgressStyle().borderColor(0xff555555).backgroundColor(0x99000011).prefix(prefix + ": ").suffix("%");
    }

    public static IProgressStyle machineProgress() {
        return machineProgress("Progress");
    }
}
