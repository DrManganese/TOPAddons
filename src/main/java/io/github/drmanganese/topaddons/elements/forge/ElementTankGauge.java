package io.github.drmanganese.topaddons.elements.forge;

import io.github.drmanganese.topaddons.addons.forge.AddonForge;
import io.github.drmanganese.topaddons.util.ElementHelper;

import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import io.netty.buffer.ByteBuf;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class ElementTankGauge implements IElement {

    private final boolean extended;
    private final int amount, capacity, color;
    private final String tankName, fluidName;
    private int id;

    public ElementTankGauge(int id, boolean extended, int amount, int capacity, String tankName, String fluidName, int color) {
        this.id = id;
        this.extended = extended;
        this.amount = amount;
        this.capacity = capacity;
        this.tankName = tankName;
        this.fluidName = fluidName;
        this.color = color;
    }

    public ElementTankGauge(ByteBuf buf) {
        this.extended = buf.readBoolean();
        this.amount = buf.readInt();
        this.capacity = buf.readInt();
        this.tankName = NetworkTools.readString(buf);
        this.fluidName = NetworkTools.readString(buf);
        this.color = buf.readInt();
    }

    @Override
    public void render(int x, int y) {
        //Background
        if (AddonForge.tankRounded) {
            Gui.drawRect(x + 1, y + 1, x + 99, y + (extended ? 11 : 7), AddonForge.tankBackgroundColor);
            ElementHelper.drawHorizontalLine(x + (extended ? 2 : 1), y, extended ? 96 : 98, AddonForge.tankBorderColor);
            ElementHelper.drawHorizontalLine(x + (extended ? 2 : 1), y + (extended ? 11 : 7), extended ? 96 : 98, AddonForge.tankBorderColor);
            ElementHelper.drawVerticalLine(x, y + (extended ? 2 : 1), extended ? 8 : 6, AddonForge.tankBorderColor);
            ElementHelper.drawVerticalLine(x + 99, y + (extended ? 2 : 1), extended ? 8 : 6, AddonForge.tankBorderColor);
        } else {
            ElementHelper.drawBox(x, y, 100, extended ? 12 : 8, AddonForge.tankBackgroundColor, 1, AddonForge.tankBorderColor);
        }

        //Fluid
        for (int i = 0; i < Math.min(98 * amount / capacity, 98); i++) {
            Gui.drawRect(x + 1 + i, y + 1, x + 2 + i, y + (extended ? 11 : 7), i % 2 == 0 ? color : new Color(color).darker().hashCode());
        }

        //Gauges
        if (extended) {
            if (AddonForge.tankRounded) {
                Gui.drawRect(x + 1, y + 1, x + 2, y + 2, AddonForge.tankBorderColor);
                Gui.drawRect(x + 1, y + 10, x + 2, y + 11, AddonForge.tankBorderColor);
                Gui.drawRect(x + 98, y + 1, x + 99, y + 2, AddonForge.tankBorderColor);
                Gui.drawRect(x + 98, y + 10, x + 99, y + 11, AddonForge.tankBorderColor);
            }

            final int[] gaugeCoords = {13, 25, 37, 49, 61, 73, 85};
            final int[] gaugeLengths = {5, 6, 5, 10, 5, 6, 5};
            for (int i = 0; i < gaugeCoords.length; i++) {
                Gui.drawRect(x + gaugeCoords[i], y + 1, x + gaugeCoords[i] + 1, y + 1 + gaugeLengths[i], AddonForge.tankBorderColor);
            }
        }


        //Text
        final FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        if (extended) {
            font.drawStringWithShadow(amount + "/" + capacity + " mB", x + 2, y + 2, 0xffffffff);
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            font.drawStringWithShadow(tankName, x * 2, (y + 13) * 2, 0xffffffff);
            font.drawStringWithShadow(fluidName, (x + 100) * 2 - font.getStringWidth(fluidName), (y + 13) * 2, color);
            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            font.drawStringWithShadow(tankName, (x + 2) * 2, (y + 2) * 2, 0xffffffff);
            GL11.glPopMatrix();
        }
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
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.extended);
        buf.writeInt(this.amount);
        buf.writeInt(this.capacity);
        NetworkTools.writeString(buf, this.tankName);
        NetworkTools.writeString(buf, this.fluidName);
        buf.writeInt(this.color);
    }

    @Override
    public int getID() {
        return id;
    }
}
