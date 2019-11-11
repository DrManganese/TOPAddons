package io.github.drmanganese.topaddons.elements.forestry;

import io.github.drmanganese.topaddons.util.ElementHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import org.lwjgl.opengl.GL11;

public class ElementFarm implements IElement {

    private final String facing;
    private final NonNullList<ItemStack> icons;
    private final int[] colors;
    private int id;

    public ElementFarm(int id, String facing, NonNullList<ItemStack> icons, int[] colors) {
        this.id = id;
        this.facing = facing;
        this.icons = icons;
        this.colors = colors;
    }

    public ElementFarm(ByteBuf buf) {
        this.facing = ByteBufUtils.readUTF8String(buf);
        this.icons = NonNullList.withSize(5, ItemStack.EMPTY);
        for (int i = 0; i < 5; i++) {
            this.icons.set(i, ByteBufUtils.readItemStack(buf));
        }
        this.colors = new int[4];
        for (int i = 0; i < 4; i++) {
            colors[i] = buf.readInt();
        }
    }

    @Override
    public void render(int x, int y) {
        x += 6;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        drawQuadrant(tessellator, bufferbuilder, x + 52, y + 36, -1, 1, colors[0]);
        drawQuadrant(tessellator, bufferbuilder, x + 52, y + 52, -1, -1, colors[1]);
        drawQuadrant(tessellator, bufferbuilder, x + 36, y + 52, 1, -1, colors[2]);
        drawQuadrant(tessellator, bufferbuilder, x + 36, y + 36, 1, 1, colors[3]);

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(this.icons.get(4), x + 36, y + 36);
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(this.icons.get(0), x + 32, y + 12);
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(this.icons.get(1), x + 60, y + 32);
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(this.icons.get(2), x + 40, y + 60);
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(this.icons.get(3), x + 12, y + 40);
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 88;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.facing);
        for (ItemStack icon : this.icons) {
            ByteBufUtils.writeItemStack(buf, icon);
        }

        for (int color : this.colors) {
            buf.writeInt(color);
        }
    }

    @Override
    public int getID() {
        return id;
    }

    /*
     *    y                              y2
     *   x#-----------#            x2#---#
     *    |xdir  1    |              |   |
     *    |ydir -1#---#          #---#   |
     *    |       |              |       |
     *    |   #---#          #---#ydir  1|
     *    |   |              |    xdir -1|
     *    #---#x2            #-----------#x
     *    y2                             y
     */
    private void drawQuadrant(Tessellator tessellator, BufferBuilder bufferbuilder, int x, int y, int xdir, int ydir, int color) {
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        GlStateManager.color(r, g, b, a);
        bufferbuilder.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(x, y, 0.0D).endVertex();
        if (xdir != ydir) {
            final int x2 = x + xdir * 16;
            final int y2 = y - ydir * 36;
            bufferbuilder.pos(x, y2, 0.0D).endVertex();
            bufferbuilder.pos(x2, y2, 0.0D).endVertex();
            for (int i = 0; i < 9; i++) {
                bufferbuilder.pos(x2 + xdir * i * 4, y2 + ydir * (i + 1) * 4, 0).endVertex();
                bufferbuilder.pos(x2 + xdir * (i + 1) * 4, y2 + ydir * (i + 1) * 4, 0).endVertex();
            }
        } else {
            final int x2 = x - xdir * 36;
            final int y2 = y + ydir * 16;
            bufferbuilder.pos(x2, y, 0.0D).endVertex();
            bufferbuilder.pos(x2, y2, 0.0D).endVertex();
            for (int i = 0; i < 9; i++) {
                bufferbuilder.pos(x2 + xdir * (i + 1) * 4, y2 + ydir * i * 4, 0).endVertex();
                bufferbuilder.pos(x2 + xdir * (i + 1) * 4, y2 + ydir * (i + 1) * 4, 0).endVertex();
            }
        }
        tessellator.draw();

    }
}
