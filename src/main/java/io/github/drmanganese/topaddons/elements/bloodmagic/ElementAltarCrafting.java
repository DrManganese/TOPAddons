package io.github.drmanganese.topaddons.elements.bloodmagic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import io.github.drmanganese.topaddons.addons.AddonBloodMagic;
import io.github.drmanganese.topaddons.elements.ElementRenderHelper;

import org.lwjgl.opengl.GL11;

import WayofTime.bloodmagic.api.orb.IBloodOrb;
import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;

public class ElementAltarCrafting implements IElement {

    private int id;

    private final ItemStack input, result;
    private final int progress, required;
    private final float consumption;

    public ElementAltarCrafting(int id, ItemStack input, ItemStack result, int progress, int required, float consumption) {
        this.id = id;
        this.input = input;
        this.result = result;
        this.progress = progress;
        this.required = required;
        this.consumption = consumption;
    }

    public ElementAltarCrafting(ByteBuf buf) {
        this.input = NetworkTools.readItemStack(buf);
        this.result = NetworkTools.readItemStack(buf);
        this.progress = buf.readInt();
        this.required = buf.readInt();
        this.consumption = buf.readFloat();
    }

    @Override
    public void render(int x, int y) {
        Minecraft mc = Minecraft.getMinecraft();
        RenderHelper.renderItemStack(mc, mc.getRenderItem(), input, x, y, "");
        RenderHelper.renderItemStack(mc, mc.getRenderItem(), result, x + 84, y, "");
        ElementRenderHelper.drawGreyBox(x + 18, y + 3, x + 82, y + 13);


        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(new ResourceLocation("topaddons:textures/elements/bm_altar.png"));
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buf = tessellator.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buf.pos(x + 19, y + 4, 0).tex(1 - (float) progress/required, 0).endVertex();
        buf.pos(x + 19, y + 12, 0).tex(1 - (float) progress/required, 1).endVertex();
        buf.pos(x + 19 + 62 * progress/required, y + 12, 0).tex(1, 1).endVertex();
        buf.pos(x + 19 + 62 * progress/required, y + 4, 0).tex(1, 0).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();

        String text = progress + "/" + required  + " LP";
        ElementRenderHelper.drawSmallText(x + 50 - mc.fontRendererObj.getStringWidth(text)/4, y + 6, text, 0xffffffff);
        if (input.getItem() instanceof IBloodOrb)
            text = I18n.format("topaddons.bloodmagic:filling");
        else
            text = I18n.format("topaddons.ic2:consumption") + ": " + consumption + " LP";
        ElementRenderHelper.drawSmallText(x + 50 - mc.fontRendererObj.getStringWidth(text)/4, y + 14, text, 0xffffffff);
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writeItemStack(buf, input);
        NetworkTools.writeItemStack(buf, result);
        buf.writeInt(progress);
        buf.writeInt(required);
        buf.writeFloat(consumption);
    }

    @Override
    public int getID() {
        return id;
    }
}
