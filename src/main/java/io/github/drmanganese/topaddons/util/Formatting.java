package io.github.drmanganese.topaddons.util;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class Formatting {

    private static final NavigableMap<Integer, Character> INTEGER_SUFFIXES = new TreeMap<>();

    static {
        INTEGER_SUFFIXES.put(1000, 'k');
        INTEGER_SUFFIXES.put(1000000, 'M');
        INTEGER_SUFFIXES.put(1000000000, 'G');
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

    /**
     * <pre>SISuffix(n, "#.#")</pre>
     */
    public static String SISuffix(int n) {
        return SISuffix(n, "#.#");
    }

    /**
     * Formats number into "engineering notation" . E.g. <tt>25000</tt> => <tt>25k</tt>.
     *
     * @param n Number to be formatted.
     * @param dFormat Formatting string. See {@link DecimalFormat}.
     * @return Formatted
     */
    public static String SISuffix(int n, String dFormat) {
        if (n < 10000) return Integer.toString(n);
        final Map.Entry<Integer, Character> pair = INTEGER_SUFFIXES.floorEntry(n);
        return new DecimalFormat(dFormat).format(1.0F * n / pair.getKey()) + pair.getValue();
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
