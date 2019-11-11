package io.github.drmanganese.topaddons.util;

import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.block.BlockCocoa;
import net.minecraft.util.text.TextFormatting;

import static mcjty.theoneprobe.api.TextStyleClass.*;

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
    public static IProbeInfo textPrefixed(IProbeInfo probeInfo, String prefix, String info, TextStyleClass prefixFormatting) {
        return probeInfo.text(prefixFormatting + prefix + ":" + TextStyleClass.INFO  + " " + info);
    }

    /**
     * "<tt>prefix</tt>: <tt>info</tt>"
     */
    public static IProbeInfo textPrefixed(IProbeInfo probeInfo, String prefix, String info, TextFormatting prefixFormatting) {
        return probeInfo.text(prefixFormatting + prefix + ":" + TextStyleClass.INFO  + " " + info);
    }

    /**
     * "<tt>prefix</tt>: <tt>info</tt>"
     */
    public static IProbeInfo textPrefixed(IProbeInfo probeInfo, String prefix, String info) {
        return textPrefixed(probeInfo, prefix, info, TextStyleClass.LABEL);
    }

    public static IProbeInfo growth(IProbeInfo probeInfo, int age, int maxAge) {
        if (age == maxAge) {
            return probeInfo.text(OK + "Fully grown");
        } else {
            return probeInfo.text(LABEL + "Growth: " + WARNING + (age * 100) / maxAge + "%");
        }
    }
}
