package io.github.drmanganese.topaddons.styles;

import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;

public class ProgressStyleForestryMultiColored extends ProgressStyle {

    private final int percentage;

    public ProgressStyleForestryMultiColored(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public int getFilledColor() {
        if (percentage <= 5) {
            return 0xffd90f00;
        } else if (percentage <= 30) {
            return 0xffe68800;
        } else if (percentage <= 60) {
            return 0xffe3e605;
        } else if (percentage <= 90) {
            return 0xff99e605;
        } else {
            return 0xff35e600;
        }
    }

    @Override
    public int getAlternatefilledColor() {
        if (percentage <= 5) {
            return 0xffcc0e00;
        } else if (percentage <= 30) {
            return 0xffd98100;
        } else if (percentage <= 60) {
            return 0xffd6d905;
        } else if (percentage <= 90) {
            return 0xff91d905;
        } else {
            return 0xff32d900;
        }
    }

    @Override
    public int getBackgroundColor() {
        return 0x99000011;
    }

    @Override
    public String getSuffix() {
        return "%";
    }

    @Override
    public int getWidth() {
        return 81;
    }
}
