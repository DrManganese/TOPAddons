package io.github.drmanganese.topaddons.elements.forge;

import io.github.drmanganese.topaddons.addons.forge.ForgeAddon;
import io.github.drmanganese.topaddons.client.FluidColors;
import io.github.drmanganese.topaddons.util.ElementHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import com.google.common.collect.ImmutableList;
import mcjty.theoneprobe.api.IElementNew;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.function.Function;

public class FluidGaugeElement implements IElementNew {

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
    public void render(int x, int y) {
        final int borderColor = ForgeAddon.gaugeBorderColor.getInt();
        final int backgroundColor = ForgeAddon.gaugeBackgroundColor.getInt();
        final int fluidColor = ImmutableList.of(FluidColors.getOverrideColor(fluid), FluidColors.getForgeColor(fluid))
            .stream()
            .filter(Optional::isPresent)
            .findFirst()
            .flatMap(Function.identity())
            .orElse(FluidColors.getForFluid(fluid, ForgeAddon.gaugeFluidColorAlgorithm.get()));

        renderBackground(x, y, borderColor, backgroundColor);
        renderFluid(x, y, fluidColor);
        renderForeGround(x, y, borderColor);
        renderText(x, y, fluidColor);
    }

    @Override
    public int getWidth() {
        return 100;
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

    private void renderBackground(int x, int y, int borderColor, int backgroundColor) {
        if (ForgeAddon.gaugeRounded.get()) {
            AbstractGui.fill(x + 1, y + 1, x + 99, y + (extended ? 11 : 7), backgroundColor);
            ElementHelper.drawHorizontalLine(x + (extended ? 2 : 1), y, extended ? 96 : 98, borderColor);
            ElementHelper.drawHorizontalLine(x + (extended ? 2 : 1), y + (extended ? 11 : 7), extended ? 96 : 98, borderColor);
            ElementHelper.drawVerticalLine(x, y + (extended ? 2 : 1), extended ? 8 : 6, borderColor);
            ElementHelper.drawVerticalLine(x + 99, y + (extended ? 2 : 1), extended ? 8 : 6, borderColor);
        } else {
            ElementHelper.drawBox(x, y, 100, extended ? 12 : 8, backgroundColor, 1, borderColor);
        }
    }

    private void renderFluid(int x, int y, int color) {
        color = (color & 0x00ffffff) | (ForgeAddon.gaugeFluidColorTransparency.get()) << 24;
        for (int i = 0; i < Math.min(98 * amount / capacity, 98); i++) {
            AbstractGui.fill(x + 1 + i, y + 1, x + 2 + i, y + (extended ? 11 : 7), i % 2 == 0 ? color : new Color(color, true).darker().hashCode());
        }
    }

    private void renderForeGround(int x, int y, int borderColor) {
        if (extended) {
            if (ForgeAddon.gaugeRounded.get()) {
                // Rounded corners overlap fluid
                AbstractGui.fill(x + 1, y + 1, x + 2, y + 2, borderColor);
                AbstractGui.fill(x + 1, y + 10, x + 2, y + 11, borderColor);
                AbstractGui.fill(x + 98, y + 1, x + 99, y + 2, borderColor);
                AbstractGui.fill(x + 98, y + 10, x + 99, y + 11, borderColor);
            }

            final int[] gaugeLineXs = {13, 25, 37, 49, 61, 73, 85};
            final int[] gaugeLineLengths = {5, 6, 5, 10, 5, 6, 5};
            for (int i = 0; i < gaugeLineXs.length; i++) {
                AbstractGui.fill(x + gaugeLineXs[i], y + 1, x + gaugeLineXs[i] + 1, y + 1 + gaugeLineLengths[i], borderColor);
            }
        }
    }

    private void renderText(int x, int y, int color) {
        final String tankDisplayName = new TranslationTextComponent(tankNameKey).getString();
        final FontRenderer font = Minecraft.getInstance().fontRenderer;
        if (extended) {
            final String fluidDisplayName = new TranslationTextComponent(fluid.getAttributes().getTranslationKey()).getString();
            font.drawStringWithShadow(amountText(), x + 2, y + 2, 0xffffffff);
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            font.drawStringWithShadow(tankDisplayName, x * 2, (y + 13) * 2, 0xffffffff);
            if (fluid != Fluids.EMPTY)
                font.drawStringWithShadow(fluidDisplayName, (x + 100) * 2 - font.getStringWidth(fluidDisplayName), (y + 13) * 2, color);
            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            font.drawStringWithShadow(tankDisplayName, (x + 2) * 2, (y + 2) * 2, 0xffffffff);
            GL11.glPopMatrix();
        }
    }

    private String amountText() {
        if (this.amount == 0 && !ForgeAddon.gaugeShowCapacity.get())
            return new TranslationTextComponent("topaddons.forge:empty").getString();
        else {
            final String amount = new DecimalFormat("#.#").format(this.capacity < 100000 ? this.amount : this.amount / 10000);
            final int capacity = this.capacity < 100000 ? this.capacity : this.capacity / 10000;
            final String unit = this.capacity < 100000 ? "mB" : "B";
            final Boolean showCapacity = ForgeAddon.gaugeShowCapacity.get();
            return String.format("%s%s%s %s", amount, showCapacity ? "/" : "", showCapacity ? capacity : "", unit);
        }
    }
}
