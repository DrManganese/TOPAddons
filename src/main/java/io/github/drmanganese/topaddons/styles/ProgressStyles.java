package io.github.drmanganese.topaddons.styles;

import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;

public final class ProgressStyles {

    public static final IProgressStyle SIMPLE_PROGRESS = new ProgressStyle()
            .width(100)
            .height(12)
            .borderColor(0xffffffff)
            .backgroundColor(0xff000000)
            .filledColor(0xffaaaaaa)
            .alternateFilledColor(0xffaaaaaa);
}
