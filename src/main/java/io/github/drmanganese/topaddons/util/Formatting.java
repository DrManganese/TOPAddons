package io.github.drmanganese.topaddons.util;

public final class Formatting {

    /**
     * Formats the given value and unit to time in seconds.
     *
     * @param value     time value to format/convert.
     * @param valueUnit unit of the value (ticks, seconds, minutes, ...).
     * @return Formatted time string of the form: <i>#s</i>.
     */
    public static String timeInSeconds(int value, TimeUnit valueUnit) {
        return String.format("%ds", valueUnit.toSeconds(value));
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
    }
}
