package io.github.drmanganese.topaddons.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import io.github.drmanganese.topaddons.addons.AddonForestry;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.network.NetworkTools;

import static mcjty.theoneprobe.rendering.RenderHelper.renderItemOverlayIntoGUI;

public class ElementForestryFarm implements IElement {

    private ItemStack[] farmIcons;
    private String oneDirection;

    public ElementForestryFarm(ItemStack[] farmIcons, String oneDirection) {
        this.farmIcons = farmIcons;
        this.oneDirection = oneDirection;
    }

    public ElementForestryFarm(ByteBuf buf) {
        this.farmIcons = new ItemStack[4];
        farmIcons[0] = NetworkTools.readItemStack(buf);
        farmIcons[1] = NetworkTools.readItemStack(buf);
        farmIcons[2] = NetworkTools.readItemStack(buf);
        farmIcons[3] = NetworkTools.readItemStack(buf);
        oneDirection = NetworkTools.readString(buf);
    }

    @Override
    public void render(int x, int y) {
        int centerX = x + 20;
        int centerY = y + 20;
        renderItemStack(Minecraft.getMinecraft(), Minecraft.getMinecraft().getRenderItem(), farmIcons[0], centerX, centerY - 20, oneDirection);
        renderItemStack(Minecraft.getMinecraft(), Minecraft.getMinecraft().getRenderItem(), farmIcons[1], centerX + 20, centerY, nextDirection());
        renderItemStack(Minecraft.getMinecraft(), Minecraft.getMinecraft().getRenderItem(), farmIcons[2], centerX, centerY + 20, nextDirection());
        renderItemStack(Minecraft.getMinecraft(), Minecraft.getMinecraft().getRenderItem(), farmIcons[3], centerX - 20, centerY, nextDirection());
        nextDirection();
    }

    @Override
    public int getWidth() {
        return 60;
    }

    @Override
    public int getHeight() {
        return 60;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        for (ItemStack farmIcon : farmIcons) {
            NetworkTools.writeItemStack(buf, farmIcon);
        }

        NetworkTools.writeString(buf, oneDirection);
    }

    @Override
    public int getID() {
        return AddonForestry.ELEMENT_FARM;
    }

    private String nextDirection() {
        switch (this.oneDirection) {
            case "N":
                this.oneDirection = "E";
                return "E";
            case "E":
                this.oneDirection = "S";
                return "S";
            case "S":
                this.oneDirection = "W";
                return "W";
            case "W":
                this.oneDirection = "N";
                return "N";
            default:
                return this.oneDirection;
        }
    }

    public static int renderItemStack(Minecraft mc, RenderItem itemRender, ItemStack itm, int x, int y, String txt) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);

        int rc = 0;
        if (itm != null && itm.getItem() != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 32.0F);
            GlStateManager.color(2.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableLighting();
            short short1 = 240;
            short short2 = 240;
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, short1 / 1.0F, short2 / 1.0F);
            itemRender.renderItemAndEffectIntoGUI(itm, x, y);
            renderItemOverlayIntoGUI(mc.fontRendererObj, itm, x, y, txt, txt.length() - 2);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableLighting();
            rc = 20;
        }

        return rc;
    }
}
