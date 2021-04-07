package io.github.drmanganese.topaddons.elements.bloodmagic;

import io.github.drmanganese.topaddons.addons.bloodmagic.BloodMagicAddon;
import io.github.drmanganese.topaddons.addons.forge.ForgeAddon;
import io.github.drmanganese.topaddons.util.ElementHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcjty.theoneprobe.api.IElement;
import org.lwjgl.opengl.GL11;
import wayoftime.bloodmagic.common.item.IBloodOrb;

public class BloodAltarProgressElement implements IElement {

    private final int progress, maxProgress;
    private final ItemStack inputStack, outputStack;
    private final float consumption;
    private int id;

    public BloodAltarProgressElement(int id, int progress, int maxProgress, ItemStack inputStack, ItemStack outputStack, float consumption) {
        this.progress = progress;
        this.maxProgress = maxProgress;
        this.inputStack = inputStack;
        this.outputStack = outputStack;
        this.consumption = consumption;
        this.id = id;
    }

    public BloodAltarProgressElement(PacketBuffer packetBuffer) {
        this.progress = packetBuffer.readInt();
        this.maxProgress = packetBuffer.readInt();
        this.inputStack = packetBuffer.readItemStack();
        this.outputStack = packetBuffer.readItemStack();
        this.consumption = packetBuffer.readFloat();
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y) {
        final Minecraft mc = Minecraft.getInstance();
        mc.getItemRenderer().renderItemAndEffectIntoGUI(inputStack, x, y);
        mc.getItemRenderer().renderItemAndEffectIntoGUI(outputStack, x + 84, y);
        ElementHelper.drawBox(matrixStack, x + 18, y + 3, 64, 10, ForgeAddon.machineProgressBackgroundColor.getInt(), 1, ForgeAddon.machineProgressBorderColor.getInt());
        RenderSystem.blendColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        mc.getTextureManager().bindTexture(new ResourceLocation("topaddons:textures/elements/bm_altar.png"));
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buf = tessellator.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buf.pos(x + 19, y + 4, 0).tex(1 - (float) progress / maxProgress, 0).endVertex();
        buf.pos(x + 19, y + 12, 0).tex(1 - (float) progress / maxProgress, 1).endVertex();
        buf.pos(x + 19 + (62F * progress / maxProgress), y + 12, 0).tex(1, 1).endVertex();
        buf.pos(x + 19 + (62F * progress / maxProgress), y + 4, 0).tex(1, 0).endVertex();
        tessellator.draw();
        RenderSystem.disableBlend();

        final String barText = progress + "/" + maxProgress + " LP";
        ElementHelper.drawSmallText(matrixStack, mc, x + 50 - mc.fontRenderer.getStringWidth(barText) / 4, y + 6, barText, 0xffffffff);
        final String belowText;
        if (inputStack.getItem() instanceof IBloodOrb)
            belowText = new TranslationTextComponent("topaddons.bloodmagic:filling").getString();
        else
            belowText = new TranslationTextComponent("topaddons.bloodmagic:consumption").getString() + ": " + consumption + " LP";
        ElementHelper.drawSmallText(matrixStack, mc, x + 50 - mc.fontRenderer.getStringWidth(belowText) / 4, y + 14, belowText, 0xffffffff);
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
    public void toBytes(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(progress);
        packetBuffer.writeInt(maxProgress);
        packetBuffer.writeItemStack(inputStack);
        packetBuffer.writeItemStack(outputStack);
        packetBuffer.writeFloat(consumption);
    }

    @Override
    public int getID() {
        return id;
    }
}
