package io.github.drmanganese.topaddons.util;

import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.TextStyleClass;

@SuppressWarnings("UnusedReturnValue")
public final class InfoHelper {

    /**
     * "Producing <tt>rate</tt> Rf/t"
     */
    public static IProbeInfo generatorProducing(IProbeInfo probeInfo, int rate) {
        final TextStyleClass rateColor = rate > 0 ? TextStyleClass.OK : TextStyleClass.WARNING;
        return probeInfo.text(TextStyleClass.LABEL + "Producing " + rateColor + rate + TextStyleClass.LABEL + " RF/t");
    }

    /**
     * "<tt>prefix</tt>: <tt>info</tt>"
     */
    public static IProbeInfo textPrefixed(IProbeInfo probeInfo, String prefix, String info) {
        return probeInfo.text(TextStyleClass.LABEL + prefix + ": " + TextStyleClass.INFO + info);
    }
}
