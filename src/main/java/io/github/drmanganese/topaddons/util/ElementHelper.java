package io.github.drmanganese.topaddons.util;

import net.minecraft.client.gui.Gui;

public final class ElementHelper {

    public static void drawBox(int x, int y, int w, int h, int backgroundColor, int s, int strokeColor) {
        Gui.drawRect(x + s, y + s, x + w - s, y + h - s, backgroundColor);
        Gui.drawRect(x, y, x + w, y + s, strokeColor);
        Gui.drawRect(x, y + h - s, x + w, y + h, strokeColor);
        Gui.drawRect(x, y + s, x + s, y + h - s, strokeColor);
        Gui.drawRect(x + w - s, y + s, x + w, y + h - s, strokeColor);
    }
}
