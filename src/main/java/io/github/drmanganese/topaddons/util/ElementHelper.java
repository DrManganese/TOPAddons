package io.github.drmanganese.topaddons.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.FriendlyByteBuf;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;

public final class ElementHelper {

    /**
     * Draws a box at given coordinates. If <tt>s > 0</tt> the box fill will be offset and the top left corner of the
     * stroke will be at (<tt>x</tt>, <tt>y</tt>).
     *
     * @param poseStack       Current rendering PoseStack.
     * @param x               X-coordinate of the top left corner.
     * @param y               Y-coordinate of the top left corner.
     * @param w               Box' total width, including stroke.
     * @param h               Box' height, including stroke.
     * @param backgroundColor Box' fill color.
     * @param s               Width of stroke around box. Drawn "inside".
     * @param strokeColor     Stroke's color.
     */
    public static void drawBox(PoseStack poseStack, int x, int y, int w, int h, int backgroundColor, int s, int strokeColor) {
        Gui.fill(poseStack, x + s, y + s, x + w - s, y + h - s, backgroundColor);
        Gui.fill(poseStack, x, y, x + w, y + s, strokeColor);
        Gui.fill(poseStack, x, y + h - s, x + w, y + h, strokeColor);
        Gui.fill(poseStack, x, y + s, x + s, y + h - s, strokeColor);
        Gui.fill(poseStack, x + w - s, y + s, x + w, y + h - s, strokeColor);
    }

    /**
     * Draw a horizontal line with length <tt>l</tt> and thickness 1.
     *
     * @param poseStack   Current rendering PoseStack.
     * @param x           X-coordinate of line starting point.
     * @param y           Y-coordinate of line starting point.
     * @param l           Line length.
     * @param color       Line color.
     */
    public static void drawHorizontalLine(PoseStack poseStack, int x, int y, int l, int color) {
        Gui.fill(poseStack, x, y, x + l, y + 1, color);
    }

    /**
     * Draw a vertical line with length <tt>l</tt> and thickness 1.
     *
     * @param poseStack Current rendering PoseStack.
     * @param x         X-coordinate of line starting point.
     * @param y         Y-coordinate of line starting point.
     * @param l         Line length.
     * @param color     Line color.
     */
    public static void drawVerticalLine(PoseStack poseStack, int x, int y, int l, int color) {
        Gui.fill(poseStack, x, y, x + 1, y + l, color);
    }

    public static void writeProgressStyleToBuffer(IProgressStyle style, FriendlyByteBuf buf) {
        buf.writeInt(style.getWidth());
        buf.writeInt(style.getHeight());
        buf.writeUtf(style.getPrefix());
        buf.writeUtf(style.getSuffix());
        buf.writeInt(style.getBorderColor());
        buf.writeInt(style.getFilledColor());
        buf.writeInt(style.getAlternatefilledColor());
        buf.writeInt(style.getBackgroundColor());
        buf.writeBoolean(style.isShowText());
        buf.writeByte(style.getNumberFormat().ordinal());
        buf.writeBoolean(style.isLifeBar());
        buf.writeBoolean(style.isArmorBar());
    }

    public static IProgressStyle readProgressStyleFromBuffer(FriendlyByteBuf buf) {
        final IProgressStyle style = new ProgressStyle();
        style
            .width(buf.readInt())
            .height(buf.readInt())
            .prefix(buf.readUtf())
            .suffix(buf.readUtf())
            .borderColor(buf.readInt())
            .filledColor(buf.readInt())
            .alternateFilledColor(buf.readInt())
            .backgroundColor(buf.readInt())
            .showText(buf.readBoolean())
            .numberFormat(NumberFormat.values()[buf.readByte()])
            .lifeBar(buf.readBoolean()).armorBar(buf.readBoolean());
        return style;
    }

    public static int drawSmallText(PoseStack poseStack, Minecraft mc, int x, int y, String text, int color) {
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 1.0F);
        mc.font.drawShadow(poseStack, text, x * 2, y * 2, color);
        poseStack.popPose();
        return mc.font.width(text) / 2;
    }
}
