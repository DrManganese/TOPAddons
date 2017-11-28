package io.github.drmanganese.topaddons.styles;

import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;

public class ProgressStyleGeneticsMachine extends ProgressStyle {

    private final boolean phase;
    private String suffix = "%";
    private String prefix = "Progress: ";

    public ProgressStyleGeneticsMachine(boolean phase) {
        this.phase = phase;
        this.suffix = "";
        this.prefix = "In Progress";
        numberFormat(NumberFormat.NONE);

    }

    public ProgressStyleGeneticsMachine() {
        this.phase = true;
    }

    @Override
    public int getBorderColor() {
        return 0xff1a233c;
    }

    @Override
    public int getFilledColor() {
        return phase ? 0xff414349 : 0xff53555b;
    }

    @Override
    public int getAlternatefilledColor() {
        return !phase ? 0xff414349 : 0xff53555b;
    }

    @Override
    public int getBackgroundColor() {
        return 0x881a233c;
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }
}
