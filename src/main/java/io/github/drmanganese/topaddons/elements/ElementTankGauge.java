package io.github.drmanganese.topaddons.elements;

import net.minecraft.client.Minecraft;
import io.github.drmanganese.topaddons.addons.AddonForge;
import io.github.drmanganese.topaddons.styles.ProgressStyleTank;

import java.awt.*;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.client.ElementProgressRender;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;

import static io.github.drmanganese.topaddons.elements.ElementRenderHelper.drawSmallText;

public class ElementTankGauge implements IElement {

    private final String tankName, fluidName, suffix;
    private final int amount, capacity, color1, color2;
    private final boolean sneaking;

    public ElementTankGauge(String tankName, String fluidName, int amount, int capacity, String suffix, int color1, boolean sneaking) {
        this.tankName = tankName;
        this.fluidName = fluidName;
        this.amount = amount;
        this.capacity = capacity;
        this.suffix = suffix;
        this.color1 = color1;
        this.color2 = new Color(this.color1).darker().hashCode();
        this.sneaking = sneaking;
    }

    public ElementTankGauge(ByteBuf buf) {
        this.tankName = NetworkTools.readString(buf);
        this.fluidName = NetworkTools.readString(buf);
        this.amount = buf.readInt();
        this.capacity = buf.readInt();
        this.suffix = NetworkTools.readString(buf);
        this.color1 = buf.readInt();
        this.color2 = new Color(this.color1).darker().hashCode();
        this.sneaking = buf.readBoolean();
    }

    @Override
    public void render(int x, int y) {
        if (capacity > 0) {
            ElementProgressRender.render(new ProgressStyleTank().filledColor(color1).alternateFilledColor(color2), amount, capacity, x, y, 100, sneaking ? 12 : 8);
        } else {
            ElementProgressRender.render(new ProgressStyleTank(), amount, capacity, x, y, 100, sneaking ? 12 : 8);
        }

        if (sneaking) {
            for (int i = 1; i < 10; i++) {
                RenderHelper.drawVerticalLine(x + i * 10, y + 1, y + (i == 5 ? 11 : 6), 0xff767676);
            }

            ElementTextRender.render((capacity > 0) ? amount + "/" + capacity + " " + suffix : "Empty", x + 3, y + 2);
            drawSmallText(x + 99 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(fluidName) / 2, y + 13, fluidName, color1);
        }

        drawSmallText(sneaking ? x + 1 :  x +2, sneaking ? y + 13 : y + 2, tankName, 0xffffffff);
        RenderHelper.drawVerticalLine(109, y, y + (sneaking ? 12 : 8), 0xff969696);
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return (sneaking) ? 18 : 10;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writeString(buf, this.tankName);
        NetworkTools.writeString(buf, this.fluidName);
        buf.writeInt(this.amount);
        buf.writeInt(this.capacity);
        NetworkTools.writeString(buf, this.suffix);
        buf.writeInt(this.color1);
        buf.writeBoolean(sneaking);
    }

    @Override
    public int getID() {
        return AddonForge.ELEMENT_TANK;
    }
}
