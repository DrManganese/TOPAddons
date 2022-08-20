package io.github.drmanganese.topaddons.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public final class Formatting {

    private static final String[] PREFIXES = {"", "k", "M", "G", "T", "P", "E"};

    public static String formatRealNumberWithUnit(long number, String unit) {
        return formatRealNumberWithUnit(number, unit, 0);
    }

    public static String formatRealNumberWithUnit(long number, String unit, int precision) {
        final DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance());
        df.setMaximumFractionDigits(precision);

        final int magnitude = number > 0 ? (int) (3 * Math.floor(Math.log10(number) / 3)) : 0;
        final String fmtNumber = df.format((float) number / Math.pow(10, magnitude));
        return String.format("%s %s%s", fmtNumber, PREFIXES[magnitude / 3], unit);
    }

    /**
     * Formats the given value and unit to time in seconds.
     *
     * @param value     time value to format/convert.
     * @param valueUnit unit of the value (ticks, seconds, minutes, ...).
     * @return Formatted time string of the form: <i>##s</i>.
     */
    public static String timeInSeconds(int value, TimeUnit valueUnit) {
        return String.format("%02ds", valueUnit.toSeconds(value));
    }


    /**
     * Formats the given value and unit to time in minutes.
     *
     * @param value     time value to format/convert.
     * @param valueUnit unit of the value (ticks, seconds, minutes, ...).
     * @return Formatted time string of the form: <i>##m</i>.
     */
    public static String timeInMinutes(int value, TimeUnit valueUnit) {
        return String.format("%02ds", valueUnit.toMinutes(value));
    }

    /**
     * Formats the given value and unit to time in seconds.
     *
     * @param value     time value to format/convert.
     * @param valueUnit unit of the value (ticks, seconds, minutes, ...).
     * @return Formatted time string of the form: <i>##m ##s or ##s</i>.
     */
    public static String timeInMinutesAndOrSeconds(int value, TimeUnit valueUnit) {
        final int minutes = valueUnit.toMinutes(value);
        final int seconds = valueUnit.toSeconds(value) - minutes * 60;
        if (minutes > 0) {
            return String.format("%02dm %02ds", minutes, seconds);
        } else {
            return String.format("%02ds", seconds);
        }
    }

    public enum TimeUnit {
        TICKS(1),
        SECONDS(20),
        MINUTES(1200);

        private final int ticks;

        TimeUnit(int ticks) {
            this.ticks = ticks;
        }

        public int toTicks(int value) {
            return value * this.ticks;
        }

        public int toSeconds(int value) {
            return toTicks(value) / 20;
        }

        public int toMinutes(int value) {
            return toTicks(value) / 1200;
        }
    }
}
