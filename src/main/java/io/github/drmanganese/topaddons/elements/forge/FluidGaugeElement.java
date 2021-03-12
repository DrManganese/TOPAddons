package io.github.drmanganese.topaddons.elements.forge;

import io.github.drmanganese.topaddons.addons.forge.ForgeAddon;
import io.github.drmanganese.topaddons.client.FluidColorExtraction;
import io.github.drmanganese.topaddons.client.FluidColors;
import io.github.drmanganese.topaddons.util.ElementHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcjty.theoneprobe.api.IElement;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.function.Function;

public class FluidGaugeElement implements IElement {

    private static final int INNER_WIDTH = 98;
    private static final int INNER_HEIGHT = 6;
    private static final int INNER_HEIGHT_EXTENDED = 10;

    private final boolean extended;
    private final int amount, capacity;
    private final String tankNameKey;
    private final Fluid fluid;
    private int id;

    public FluidGaugeElement(int id, boolean extended, int amount, int capacity, String tankNameKey, Fluid fluid) {
        this.id = id;
        this.extended = extended;
        this.amount = amount;
        this.capacity = capacity;
        this.tankNameKey = tankNameKey;
        this.fluid = fluid;
    }

    public FluidGaugeElement(PacketBuffer buf) {
        this.extended = buf.readBoolean();
        this.amount = buf.readInt();
        this.capacity = buf.readInt();
        this.tankNameKey = buf.readString();
        this.fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(buf.readString()));
    }

    @Override
    public void render(MatrixStack stack, int x, int y) {
        final int borderColor = ForgeAddon.gaugeBorderColor.getInt();
        final int backgroundColor = ForgeAddon.gaugeBackgroundColor.getInt();
        final int fluidColor = ImmutableList.of(FluidColors.getOverrideColor(fluid), FluidColors.getForgeColor(fluid))
            .stream()
            .filter(Optional::isPresent)
            .findFirst()
            .flatMap(Function.identity())
            .orElse(FluidColors.getForFluid(fluid, ForgeAddon.gaugeFluidColorAlgorithm.get()));

        renderBackground(stack, x, y, borderColor, backgroundColor);
        if (ForgeAddon.gaugeRenderFluidTexture.get())
            renderFluid(stack, x + 1, y + 1, fluid);
        else
            renderFluid(stack, x + 1, y + 1, fluidColor);
        renderForeGround(stack, x, y, borderColor);
        renderText(stack, x, y, fluidColor);
    }

    @Override
    public int getWidth() {
        return INNER_WIDTH + 2;
    }

    @Override
    public int getHeight() {
        return extended ? 18 : 8;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBoolean(this.extended);
        buf.writeInt(this.amount);
        buf.writeInt(this.capacity);
        buf.writeString(this.tankNameKey);
        buf.writeString(fluid.getRegistryName().toString());
    }

    private static float red(int color) {
        return (color >> 16 & 0xFF) / 255.0F;
    }

    private static float green(int color) {
        return (color >> 8 & 0xFF) / 255.0F;
    }

    private static float blue(int color) {
        return (color & 0xFF) / 255.0F;
    }

    private static float alpha(int color) {
        return (color >> 24 & 0xFF) / 255.0F;
    }

    private void renderBackground(MatrixStack matrixStack, int x, int y, int borderColor, int backgroundColor) {
        if (ForgeAddon.gaugeRounded.get()) {
            AbstractGui.fill(matrixStack,x + 1, y + 1, x + INNER_WIDTH + 1, y + (extended ? 11 : 7), backgroundColor);
            ElementHelper.drawHorizontalLine(matrixStack, x + (extended ? 2 : 1), y, extended ? 96 : 98, borderColor);
            ElementHelper.drawHorizontalLine(matrixStack, x + (extended ? 2 : 1), y + (extended ? 11 : 7), extended ? 96 : 98, borderColor);
            ElementHelper.drawVerticalLine(matrixStack, x, y + (extended ? 2 : 1), extended ? 8 : 6, borderColor);
            ElementHelper.drawVerticalLine(matrixStack, x + 99, y + (extended ? 2 : 1), extended ? 8 : 6, borderColor);
        } else {
            ElementHelper.drawBox(matrixStack, x, y, getWidth(), extended ? 12 : 8, backgroundColor, 1, borderColor);
        }
    }

    /**
     * Render the fluid by drawing vertical lines of alternating colors. The color of the alternating color is
     * calculated with awt.Color.darker (0.7 * r/g/b).
     */
    private void renderFluid(MatrixStack matrixStack, int x, int y, int color) {
        color = (color & 0x00ffffff) | (ForgeAddon.gaugeFluidColorTransparency.get()) << 24;
        final int darkerColor = new Color(color, true).darker().hashCode();
        for (int i = 0; i < Math.min(INNER_WIDTH * amount / capacity, INNER_WIDTH); i++) {
            AbstractGui.fill(
                matrixStack,
                x + i,
                y,
                x + i + 1,
                y + (extended ? INNER_HEIGHT_EXTENDED : INNER_HEIGHT),
                i % 2 == 0 ? color : darkerColor
            );
        }
    }

    /**
     * Render the fluid by tiling its texture. Eac
     */
    private void renderFluid(MatrixStack matrixStack, int x, int y, Fluid fluid) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();
        final TextureAtlasSprite texture = FluidColorExtraction.getStillFluidTexture(fluid);
        Minecraft.getInstance().getTextureManager().bindTexture(texture.getAtlasTexture().getTextureLocation());

        final int textureWidth = texture.getWidth();
        final float minU = texture.getMinU();
        final float maxU = texture.getMaxU();
        final float minV = texture.getMinV();
        final float maxV = texture.getMaxV();

        final int tileHeight = extended ? INNER_HEIGHT_EXTENDED : INNER_HEIGHT;
        // Height to render relative to UV coordinate system
        final float vHeight = (maxV - minV) * 1.0F * tileHeight / texture.getHeight();
        // UV ordinates to  use is based on the gaugeFluidTextureAlignment configuration setting
        final float v1 = ForgeAddon.gaugeFluidTextureAlignment.get().fv1.apply(minV, maxV, vHeight);
        final float v2 = ForgeAddon.gaugeFluidTextureAlignment.get().fv2.apply(minV, maxV, vHeight);


        RenderSystem.enableBlend();
        final int fluidColor = fluid.getAttributes().getColor();
        RenderSystem.color4f(red(fluidColor), green(fluidColor), blue(fluidColor), alpha(fluidColor));

        final int fullWidth = Math.min(INNER_WIDTH, INNER_WIDTH * amount / capacity);
        final int nTiles = (fullWidth + textureWidth - 1) / textureWidth; // Ceil
        for (int tile = 0; tile < nTiles; tile++) {
            final int w = tile == nTiles - 1 ? fullWidth % textureWidth : textureWidth;
            drawFluidTiles(
                x + tile * textureWidth,
                y,
                w,
                tileHeight,
                minU,
                minU + (maxU - minU) * (1.0F * w / textureWidth),
                v1,
                v2,
                tessellator,
                buffer
            );
        }
        RenderSystem.disableBlend();
    }

    // Draw counterclockwise starting at bottom left
    private void drawFluidTiles(int x, int y, int w, int h, float u1, float u2, float v1, float v2, Tessellator tessellator, BufferBuilder buffer) {
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + h, 0).tex(u1, v2).endVertex();
        buffer.pos(x + w, y + h, 0).tex(u2, v2).endVertex();
        buffer.pos(x + w, y, 0).tex(u2, v1).endVertex();
        buffer.pos(x, y, 0).tex(u1, v1).endVertex();
        tessellator.draw();
    }

    private void renderForeGround(MatrixStack matrixStack, int x, int y, int borderColor) {
        if (extended) {
            if (ForgeAddon.gaugeRounded.get()) {
                // Rounded corners overlap fluid
                AbstractGui.fill(matrixStack, x + 1, y + 1, x + 2, y + 2, borderColor);
                AbstractGui.fill(matrixStack, x + 1, y + 10, x + 2, y + 11, borderColor);
                AbstractGui.fill(matrixStack, x + 98, y + 1, x + 99, y + 2, borderColor);
                AbstractGui.fill(matrixStack, x + 98, y + 10, x + 99, y + 11, borderColor);
            }

            final int[] gaugeLineXs = {13, 25, 37, 49, 61, 73, 85};
            final int[] gaugeLineLengths = {5, 6, 5, 10, 5, 6, 5};
            for (int i = 0; i < gaugeLineXs.length; i++) {
                AbstractGui.fill(matrixStack, x + gaugeLineXs[i], y + 1, x + gaugeLineXs[i] + 1, y + 1 + gaugeLineLengths[i], borderColor);
            }
        }
    }

    private void renderText(MatrixStack matrixStack, int x, int y, int color) {
        final String tankDisplayName = new TranslationTextComponent(tankNameKey).getString();
        final FontRenderer font = Minecraft.getInstance().fontRenderer;
        if (extended) {
            final String fluidDisplayName = new TranslationTextComponent(fluid.getAttributes().getTranslationKey()).getString();
            font.drawStringWithShadow(matrixStack, amountText(), x + 3, y + 2, 0xffffffff);
            matrixStack.push();
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            font.drawStringWithShadow(matrixStack, tankDisplayName, x * 2, (y + 13) * 2, 0xffffffff);
            if (fluid != Fluids.EMPTY)
                font.drawStringWithShadow(matrixStack, fluidDisplayName, (x + getWidth()) * 2 - font.getStringWidth(fluidDisplayName), (y + 13) * 2, color);
            matrixStack.pop();
        } else {
            matrixStack.push();
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            font.drawStringWithShadow(matrixStack, tankDisplayName, (x + 2) * 2, (y + 2) * 2, 0xffffffff);
            matrixStack.pop();
        }
    }

    // TODO: Auto-fit option? (recurse until amountText's width <= INNER_WIDTH + padding)
    private String amountText() {
        if (this.amount == 0 && !ForgeAddon.gaugeShowCapacity.get())
            return new TranslationTextComponent("topaddons.forge:empty").getString();
        else {
            final String amount = new DecimalFormat("#.#").format(this.capacity < 100000 ? this.amount : this.amount / 1000);
            final int capacity = this.capacity < 100000 ? this.capacity : this.capacity / 1000;
            final String unit = this.capacity < 100000 ? "mB" : "B";
            final Boolean showCapacity = ForgeAddon.gaugeShowCapacity.get();
            return String.format("%s%s%s %s", amount, showCapacity ? "/" : "", showCapacity ? capacity : "", unit);
        }
    }
}
