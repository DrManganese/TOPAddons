package io.github.drmanganese.topaddons;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Util {

    public static final Map<Integer, String> PREFIXES;

    static {
        Map<Integer, String> tempPrefixes = new HashMap<>();
        tempPrefixes.put(0, "");
        tempPrefixes.put(3, "k");
        tempPrefixes.put(6, "M");
        tempPrefixes.put(9, "G");
        tempPrefixes.put(12, "T");
        tempPrefixes.put(15, "P");
        tempPrefixes.put(-3, "m");
        PREFIXES = Collections.unmodifiableMap(tempPrefixes);
    }

    public static String hoursMinsSecsFromTicks(int ticks, char... symbols) {
        int hours = (ticks / (20 * 60 * 60)) % 24;
        ticks -= hours * 20 * 60 * 60;

        int minutes = (ticks / (20 * 60)) % 60;
        ticks -= minutes * 20 * 60;

        int seconds = ticks / 20;

        if (hours > 0) {
            return String.format("%d%s%d%s%d%s", hours, symbols[0], minutes, symbols[1], seconds, symbols[2]);
        } else if (minutes > 0) {
            return String.format("%d%s%d%s", minutes, symbols[1], seconds, symbols[2]);
        } else {
            return String.format("%d%s", seconds, symbols[2]);
        }
    }

    public static String metricPrefixise(int number) {
        String ret;
        if (number <= 1) {
            ret = "";
        } else if (number < 10000) {
            ret = String.valueOf(number);
        } else if (number < 1000000) {
            ret = String.valueOf(number / 1000) + "k";
        } else if (number < 1000000000) {
            ret = String.valueOf(number / 1000000) + "M";
        } else {
            ret = String.valueOf(number / 1000000000) + "G";
        }

        return ret;
    }
}
