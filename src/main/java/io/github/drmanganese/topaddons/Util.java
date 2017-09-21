package io.github.drmanganese.topaddons;

import net.minecraft.item.ItemStack;

import java.util.Collection;
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

    /**
     * Blend two colours together from the given ratio.
     * @param colour1 First colour
     * @param colour2 Second colour
     * @param ratio Ratio of blending (.6 => 40% colour1, 60% colour2)
     * @return The blended color
     */
    public static int blendColors(int colour1, int colour2, float ratio) {
        int[][] argb = {{colour1 >> 24 & 0xff, colour1 >> 16 & 0xff, colour1 >> 8 & 0xff, colour1 & 0xff}, {colour2 >> 24 & 0xff, colour2 >> 16 & 0xff, colour2 >> 8 & 0xff, colour2 & 0xff}};
        int a = Math.round((1 - ratio) * argb[0][0] + ratio * argb[1][0]);
        int r = Math.round((1 - ratio) * argb[0][1] + ratio * argb[1][1]);
        int g = Math.round((1 - ratio) * argb[0][2] + ratio * argb[1][2]);
        int b = Math.round((1 - ratio) * argb[0][3] + ratio * argb[1][3]);
        return b | (g << 8) | (r << 16) | (a << 24);
    }

    public static String getRomanNumeral(int number) {
        return String.join("", Collections.nCopies(number, "I"))
                .replace("IIIII", "V")
                .replace("IIII", "IV")
                .replace("VV", "X")
                .replace("VIV", "IX")
                .replace("XXXXX", "L")
                .replace("XXXX", "XL")
                .replace("LL", "C")
                .replace("LXL", "XC")
                .replace("CCCCC", "D")
                .replace("CCCC", "CD")
                .replace("DD", "M")
                .replace("DCD", "CM");
    }

    public static void mergeItemStack(ItemStack stackToMerge, Collection<ItemStack> collection) {
        if (!stackToMerge.isEmpty()) {
            boolean merged = false;
            for (ItemStack itemStack : collection) {
                if (itemStack.isItemEqual(stackToMerge) && ItemStack.areItemStackTagsEqual(itemStack, stackToMerge)) {
                    itemStack.grow(stackToMerge.getCount());
                    merged = true;
                    break;
                }
            }

            if (!merged) {
                collection.add(stackToMerge.copy());
            }
        }
    }
}
