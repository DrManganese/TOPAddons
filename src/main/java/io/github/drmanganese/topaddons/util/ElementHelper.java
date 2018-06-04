package io.github.drmanganese.topaddons.util;

import net.minecraft.client.gui.Gui;

public final class ElementHelper {

    /**
     * Draws a box at given coordinates. If <tt>s > 0</tt> the box fill will be offset and the top left corner of the
     * stroke will be at (<tt>x</tt>, <tt>y</tt>).
     *
     * @param x               X-coordinate of the top left corner.
     * @param y               Y-coordinate of the top left corner.
     * @param w               Box' total width, including stroke.
     * @param h               Box' height, including stroke.
     * @param backgroundColor Box' fill color.
     * @param s               Width of stroke around box. Drawn "inside".
     * @param strokeColor     Stroke's color.
     */
    public static void drawBox(int x, int y, int w, int h, int backgroundColor, int s, int strokeColor) {
        Gui.drawRect(x + s, y + s, x + w - s, y + h - s, backgroundColor);
        Gui.drawRect(x, y, x + w, y + s, strokeColor);
        Gui.drawRect(x, y + h - s, x + w, y + h, strokeColor);
        Gui.drawRect(x, y + s, x + s, y + h - s, strokeColor);
        Gui.drawRect(x + w - s, y + s, x + w, y + h - s, strokeColor);
    }

    /**
     * Draw a horizontal line with length <tt>l</tt> and thickness 1.
     *
     * @param x     X-coordinate of line starting point.
     * @param y     Y-coordinate of line starting point.
     * @param l     Line length.
     * @param color Line color.
     */
    public static void drawHorizontalLine(int x, int y, int l, int color) {
        Gui.drawRect(x, y, x + l, y + 1, color);
    }

    /**
     * Draw a vertical line with length <tt>l</tt> and thickness 1.
     *
     * @param x     X-coordinate of line starting point.
     * @param y     Y-coordinate of line starting point.
     * @param l     Line length.
     * @param color Line color.
     */
    public static void drawVerticalLine(int x, int y, int l, int color) {
        Gui.drawRect(x, y, x + 1, y + l, color);
    }
}
