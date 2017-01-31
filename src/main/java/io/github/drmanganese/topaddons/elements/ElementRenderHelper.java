package io.github.drmanganese.topaddons.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import mcjty.theoneprobe.rendering.RenderHelper;

public final class ElementRenderHelper {
    public static int drawSmallText(int x, int y, String text, int color) {
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5F, 0.5F, 1.0F);
        mc.fontRenderer.drawStringWithShadow(text, x * 2, y * 2, color);
        GlStateManager.popMatrix();
        return mc.fontRenderer.getStringWidth(text) / 2;
    }

    public static void drawGreyBox(int x, int y, int x2, int y2) {
        RenderHelper.drawBeveledBox(x, y, x2, y2, 0xff969696, 0xff969696, 0x44969696);
    }
}
