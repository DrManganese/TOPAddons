package io.github.drmanganese.topaddons.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public final class ElementRenderHelper {
    public static int drawSmallText(int x, int y, String text, int color) {
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5F, 0.5F, 1.0F);
        mc.fontRendererObj.drawStringWithShadow(text, x * 2, y * 2, color);
        GlStateManager.popMatrix();
        return mc.fontRendererObj.getStringWidth(text) / 2;
    }
}
