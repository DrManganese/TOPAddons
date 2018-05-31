package io.github.drmanganese.topaddons.util;

import net.minecraft.client.gui.Gui;

public final class ElementHelper {

    /**
     * Draws a box at given coordinates. If <pre>s > 0</pre> the box fill will be offset and the top left corner of the
     * stroke will be at (x, y).
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
}
