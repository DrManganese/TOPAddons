package io.github.drmanganese.topaddons.elements.bloodmagic;

import io.github.drmanganese.topaddons.addons.bloodmagic.BloodMagicAddon;
import io.github.drmanganese.topaddons.addons.forge.ForgeAddon;
import io.github.drmanganese.topaddons.util.ElementHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import mcjty.theoneprobe.api.IElement;
import wayoftime.bloodmagic.common.item.IBloodOrb;

public class BloodAltarProgressElement implements IElement {

    private final int progress, maxProgress;
    private final ItemStack inputStack, outputStack;
    private final float consumption;

    public BloodAltarProgressElement(int progress, int maxProgress, ItemStack inputStack, ItemStack outputStack, float consumption) {
        this.progress = progress;
        this.maxProgress = maxProgress;
        this.inputStack = inputStack;
        this.outputStack = outputStack;
        this.consumption = consumption;
    }

    public BloodAltarProgressElement(FriendlyByteBuf packetBuffer) {
        this.progress = packetBuffer.readInt();
        this.maxProgress = packetBuffer.readInt();
        this.inputStack = packetBuffer.readItem();
        this.outputStack = packetBuffer.readItem();
        this.consumption = packetBuffer.readFloat();
    }

    @Override
    public void render(PoseStack matrixStack, int x, int y) {
        final Minecraft mc = Minecraft.getInstance();
        mc.getItemRenderer().renderGuiItem(inputStack, x, y);
        mc.getItemRenderer().renderGuiItem(outputStack, x + 84, y);
        ElementHelper.drawBox(matrixStack, x + 18, y + 3, 64, 10, ForgeAddon.machineProgressBackgroundColor.getInt(), 1, ForgeAddon.machineProgressBorderColor.getInt());

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, new ResourceLocation("topaddons:textures/elements/bm_altar.png"));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        final Tesselator tesselator = Tesselator.getInstance();
        final BufferBuilder buf = tesselator.getBuilder();
        buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buf.vertex(x + 19, y + 4, 0).uv(1 - (float) progress / maxProgress, 0).endVertex();
        buf.vertex(x + 19, y + 12, 0).uv(1 - (float) progress / maxProgress, 1).endVertex();
        buf.vertex(x + 19 + (62F * progress / maxProgress), y + 12, 0).uv(1, 1).endVertex();
        buf.vertex(x + 19 + (62F * progress / maxProgress), y + 4, 0).uv(1, 0).endVertex();
        tesselator.end();
        RenderSystem.disableBlend();

        final String barText = progress + "/" + maxProgress + " LP";
        ElementHelper.drawSmallText(matrixStack, mc, x + 50 - mc.font.width(barText) / 4, y + 6, barText, 0xffffffff);
        final String belowText;
        if (inputStack.getItem() instanceof IBloodOrb)
            belowText = new TranslatableComponent("topaddons.bloodmagic:filling").getString();
        else
            belowText = new TranslatableComponent("topaddons.bloodmagic:consumption").getString() + ": " + consumption + " LP";
        ElementHelper.drawSmallText(matrixStack, mc, x + 50 - mc.font.width(belowText) / 4, y + 14, belowText, 0xffffffff);
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
    public void toBytes(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeInt(progress);
        packetBuffer.writeInt(maxProgress);
        packetBuffer.writeItem(inputStack);
        packetBuffer.writeItem(outputStack);
        packetBuffer.writeFloat(consumption);
    }

    @Override
    public ResourceLocation getID() {
        return BloodMagicAddon.PROGRESS_ELEMENT_ID;
    }
}