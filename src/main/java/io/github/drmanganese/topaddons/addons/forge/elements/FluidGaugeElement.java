package io.github.drmanganese.topaddons.addons.forge.elements;

import io.github.drmanganese.topaddons.addons.forge.ForgeAddon;
import io.github.drmanganese.topaddons.client.FluidColorExtraction;
import io.github.drmanganese.topaddons.client.FluidColors;
import io.github.drmanganese.topaddons.util.ElementHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import mcjty.theoneprobe.api.Color;
import mcjty.theoneprobe.api.IElement;

import java.text.DecimalFormat;
import java.util.Optional;
import java.util.function.Function;

public class FluidGaugeElement implements IElement {

    private static final int INNER_WIDTH = 98;
    private static final int INNER_HEIGHT = 6;
    private static final int INNER_HEIGHT_EXTENDED = 10;
    public static final String EMPTY = new TranslatableComponent("topaddons.forge:empty").getString();
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.0");

    private final boolean extended;
    private final long amount, capacity;
    private final String tankNameKey;
    private final FluidStack fluidStack;

    public FluidGaugeElement(boolean extended, long amount, long capacity, String tankNameKey, FluidStack fluidStack) {
        this.extended = extended;
        this.amount = amount;
        this.capacity = capacity;
        this.tankNameKey = tankNameKey;
        this.fluidStack = fluidStack;
    }

    public FluidGaugeElement(FriendlyByteBuf buf) {
        this.extended = buf.readBoolean();
        this.amount = buf.readLong();
        this.capacity = buf.readLong();
        this.tankNameKey = buf.readUtf();
        this.fluidStack = buf.readFluidStack();
    }

    @Override
    public void render(PoseStack stack, int x, int y) {
        final int borderColor = ForgeAddon.gaugeBorderColor.getInt();
        final int backgroundColor = ForgeAddon.gaugeBackgroundColor.getInt();
        final int fluidColor = ImmutableList.of(FluidColors.getOverrideColor(fluidStack.getFluid()), FluidColors.getForgeColor(fluidStack))
            .stream()
            .filter(Optional::isPresent)
            .findFirst()
            .flatMap(Function.identity())
            .orElse(FluidColors.getForFluid(fluidStack.getFluid(), ForgeAddon.gaugeFluidColorAlgorithm.get()));

        renderBackground(stack, x, y, borderColor, backgroundColor);
        if (ForgeAddon.gaugeRenderFluidTexture.get()) {
            try {
                renderFluid(stack, x + 1, y + 1, fluidStack);
            } catch (final NullPointerException e) {
                renderFluid(stack, x + 1, y + 1, fluidColor);
            }
        } else {
            renderFluid(stack, x + 1, y + 1, fluidColor);
        }
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
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(this.extended);
        buf.writeLong(this.amount);
        buf.writeLong(this.capacity);
        buf.writeUtf(this.tankNameKey);
        buf.writeFluidStack(this.fluidStack);
    }

    @Override
    public ResourceLocation getID() {
        return ForgeAddon.GAUGE_ELEMENT_ID;
    }

    private void renderBackground(PoseStack poseStack, int x, int y, int borderColor, int backgroundColor) {
        if (ForgeAddon.gaugeRounded.get()) {
            Gui.fill(poseStack, x + 1, y + 1, x + INNER_WIDTH + 1, y + (extended ? 11 : 7), backgroundColor);
            ElementHelper.drawHorizontalLine(poseStack, x + (extended ? 2 : 1), y, extended ? 96 : 98, borderColor);
            ElementHelper.drawHorizontalLine(poseStack, x + (extended ? 2 : 1), y + (extended ? 11 : 7), extended ? 96 : 98, borderColor);
            ElementHelper.drawVerticalLine(poseStack, x, y + (extended ? 2 : 1), extended ? 8 : 6, borderColor);
            ElementHelper.drawVerticalLine(poseStack, x + 99, y + (extended ? 2 : 1), extended ? 8 : 6, borderColor);
        } else {
            ElementHelper.drawBox(poseStack, x, y, getWidth(), extended ? 12 : 8, backgroundColor, 1, borderColor);
        }
    }

    /**
     * Render the fluid by drawing vertical lines of alternating colors. The color of the alternating color is
     * calculated with Color.darker (0.7 * r/g/b).
     */
    private void renderFluid(PoseStack poseStack, int x, int y, int color) {
        color = (color & 0x00ffffff) | (ForgeAddon.gaugeFluidColorTransparency.get()) << 24;
        final int darkerColor = new Color(color).darker().hashCode();
        for (int i = 0; i < Math.min(INNER_WIDTH * amount / capacity, INNER_WIDTH); i++) {
            Gui.fill(
                poseStack,
                x + i,
                y,
                x + i + 1,
                y + (extended ? INNER_HEIGHT_EXTENDED : INNER_HEIGHT),
                i % 2 == 0 ? color : darkerColor
            );
        }
    }

    /**
     * Render the fluid by tiling its texture.
     */
    private void renderFluid(PoseStack poseStack, int x, int y, FluidStack fluidStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        final Tesselator tesselator = Tesselator.getInstance();
        final BufferBuilder buffer = tesselator.getBuilder();
        final TextureAtlasSprite texture = FluidColorExtraction.getStillFluidTextureSafe(fluidStack.getFluid());
        final int textureWidth = texture.getWidth();
        final float minU = texture.getU0();
        final float maxU = texture.getU1();
        final float minV = texture.getV0();
        final float maxV = texture.getV1();

        final int tileHeight = extended ? INNER_HEIGHT_EXTENDED : INNER_HEIGHT;
        // Height to render relative to UV coordinate system
        final float vHeight = (maxV - minV) * 1.0F * tileHeight / texture.getHeight();
        // UV ordinates to  use is based on the gaugeFluidTextureAlignment configuration setting
        final float v1 = ForgeAddon.gaugeFluidTextureAlignment.get().fv1.apply(minV, maxV, vHeight);
        final float v2 = ForgeAddon.gaugeFluidTextureAlignment.get().fv2.apply(minV, maxV, vHeight);


        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        final Color fluidColor = new Color(fluidStack.getFluid().getAttributes().getColor(fluidStack));
        RenderSystem.setShaderColor(fluidColor.getRed()/ 255.0F, fluidColor.getGreen()/ 255.0F, fluidColor.getBlue()/ 255.0F, fluidColor.getAlpha()/ 255.0F);
        final int fullWidth = (int) Math.min(INNER_WIDTH, INNER_WIDTH * amount / capacity);
        final int nTiles = fullWidth > 0 ? (fullWidth + textureWidth - 1) / textureWidth : 0; // Ceil
        for (int tile = 0; tile < nTiles; tile++) {
            // Use remainder of fullWidth/textureWidth for the last tile, unless it would be 0, then set to textureWidth
            // 0/textureWidth would break this logic, but this is already mitigated above by setting nTiles to 0 when
            // fullWidth is 0
            final int w = tile == nTiles - 1 && fullWidth % textureWidth > 0 ? fullWidth % textureWidth : textureWidth;
            drawFluidTiles(
                x + tile * textureWidth,
                y,
                w,
                tileHeight,
                minU,
                minU + (maxU - minU) * (1.0F * w / textureWidth),
                v1,
                v2,
                tesselator,
                buffer
            );
        }
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    // Draw counterclockwise starting at bottom left
    private void drawFluidTiles(int x, int y, int w, int h, float u1, float u2, float v1, float v2, Tesselator tesselator, BufferBuilder buffer) {
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(x, y + h, 0).uv(u1, v2).endVertex();
        buffer.vertex(x + w, y + h, 0).uv(u2, v2).endVertex();
        buffer.vertex(x + w, y, 0).uv(u2, v1).endVertex();
        buffer.vertex(x, y, 0).uv(u1, v1).endVertex();
        tesselator.end();
    }

    private void renderForeGround(PoseStack poseStack, int x, int y, int borderColor) {
        if (extended) {
            if (ForgeAddon.gaugeRounded.get()) {
                // Rounded corners overlap fluid
                Gui.fill(poseStack, x + 1, y + 1, x + 2, y + 2, borderColor);
                Gui.fill(poseStack, x + 1, y + 10, x + 2, y + 11, borderColor);
                Gui.fill(poseStack, x + 98, y + 1, x + 99, y + 2, borderColor);
                Gui.fill(poseStack, x + 98, y + 10, x + 99, y + 11, borderColor);
            }

            final int[] gaugeLineXs = {13, 25, 37, 49, 61, 73, 85};
            final int[] gaugeLineLengths = {5, 6, 5, 10, 5, 6, 5};
            for (int i = 0; i < gaugeLineXs.length; i++) {
                Gui.fill(poseStack, x + gaugeLineXs[i], y + 1, x + gaugeLineXs[i] + 1, y + 1 + gaugeLineLengths[i], borderColor);
            }
        }
    }

    private void renderText(PoseStack poseStack, int x, int y, int color) {
        final String tankDisplayName = new TranslatableComponent(tankNameKey).getString();
        final Font font = Minecraft.getInstance().font;
        if (extended) {
            final String fluidDisplayName = new TranslatableComponent(fluidStack.getFluid().getAttributes().getTranslationKey(fluidStack)).getString();
            font.drawShadow(poseStack, amountText(), x + 3, y + 2, 0xffffffff);
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            font.drawShadow(poseStack, tankDisplayName, x * 2, (y + 13) * 2, 0xffffffff);
            if (fluidStack.getFluid() != Fluids.EMPTY)
                font.drawShadow(poseStack, fluidDisplayName, (x + getWidth()) * 2 - font.width(fluidDisplayName), (y + 13) * 2, color);
            poseStack.popPose();
        } else {
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            font.drawShadow(poseStack, tankDisplayName, (x + 2) * 2, (y + 2) * 2, 0xffffffff);
            poseStack.popPose();
        }
    }

    private String amountText() {
        final boolean showCapacity = ForgeAddon.gaugeShowCapacity.get();
        if (!showCapacity && amount == 0) return EMPTY;

        final long sizeCheck = showCapacity ? capacity : amount;
        float factor = 1f;
        String unit = "m";
        if (sizeCheck >= 100000000000L) { factor = 1000000000f; unit = "M"; }
        else if (sizeCheck >= 100000000L) { factor = 1000000f; unit = "k"; }
        else if (sizeCheck >= 100000L) { factor = 1000f; unit = ""; }

        final String amount = factor > 1 ? DECIMAL_FORMAT.format(this.amount / factor) : String.valueOf(this.amount);
        final String capacity = factor > 1 ? DECIMAL_FORMAT.format(this.capacity / factor) : String.valueOf(this.capacity);
        if (showCapacity)
            return String.format("%s/%s %sB", amount, capacity, unit);
        else
            return String.format("%s %sB", amount, unit);
    }
}
