package io.github.drmanganese.topaddons.util;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.network.PacketBuffer;

import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;

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
        AbstractGui.fill(x + s, y + s, x + w - s, y + h - s, backgroundColor);
        AbstractGui.fill(x, y, x + w, y + s, strokeColor);
        AbstractGui.fill(x, y + h - s, x + w, y + h, strokeColor);
        AbstractGui.fill(x, y + s, x + s, y + h - s, strokeColor);
        AbstractGui.fill(x + w - s, y + s, x + w, y + h - s, strokeColor);
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
        AbstractGui.fill(x, y, x + l, y + 1, color);
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
        AbstractGui.fill(x, y, x + 1, y + l, color);
    }

    public static void writeProgressStyleToBuffer(IProgressStyle style, PacketBuffer buf) {
        buf.writeInt(style.getWidth());
        buf.writeInt(style.getHeight());
        buf.writeString(style.getPrefix());
        buf.writeString(style.getSuffix());
        buf.writeInt(style.getBorderColor());
        buf.writeInt(style.getFilledColor());
        buf.writeInt(style.getAlternatefilledColor());
        buf.writeInt(style.getBackgroundColor());
        buf.writeBoolean(style.isShowText());
        buf.writeByte(style.getNumberFormat().ordinal());
        buf.writeBoolean(style.isLifeBar());
        buf.writeBoolean(style.isArmorBar());
    }

    public static IProgressStyle readProgressStyleFromBuffer(PacketBuffer buf) {
        final IProgressStyle style = new ProgressStyle();
        style
            .width(buf.readInt())
            .height(buf.readInt())
            .prefix(buf.readString())
            .suffix(buf.readString())
            .borderColor(buf.readInt())
            .filledColor(buf.readInt())
            .alternateFilledColor(buf.readInt())
            .backgroundColor(buf.readInt())
            .showText(buf.readBoolean())
            .numberFormat(NumberFormat.values()[buf.readByte()])
            .lifeBar(buf.readBoolean()).armorBar(buf.readBoolean());
        return style;
    }
}
