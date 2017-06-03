package io.github.drmanganese.topaddons.elements.tconstruct;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

import io.github.drmanganese.topaddons.elements.ElementRenderHelper;
import io.github.drmanganese.topaddons.styles.ProgressStyleSmelteryFluid;
import io.github.drmanganese.topaddons.styles.ProgressStyleTank;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.client.ElementProgressRender;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;

import static io.github.drmanganese.topaddons.elements.ElementRenderHelper.drawSmallText;

public class ElementSmelteryTank implements IElement {

    private int id;

    private final List<SmelteryFluid> fluids;
    private final int capacity;
    private final boolean sneaking;

    private final int gaugeHeight;

    public ElementSmelteryTank(int id, List<FluidStack> fluids, int capacity, boolean sneaking) {
        this.id = id;
        this.fluids = new ArrayList<>();
        fluids.forEach(fluidStack -> this.fluids.add(new SmelteryFluid(fluidStack.getLocalizedName(), fluidStack.amount, fluidStack.getFluid().getColor(), capacity)));
        this.capacity = capacity;
        this.sneaking = sneaking;

        gaugeHeight = calcGaugeHeight();
    }

    public ElementSmelteryTank(ByteBuf buf) {
        this.fluids = new ArrayList<>();
        int count = buf.readInt();
        this.capacity = buf.readInt();
        for (int i = 0; i < count; i++) {
            this.fluids.add(new SmelteryFluid(NetworkTools.readString(buf), buf.readInt(), buf.readInt(), this.capacity));
        }
        this.sneaking = buf.readBoolean();
        gaugeHeight = calcGaugeHeight();
    }

    private int calcGaugeHeight() {
        int sum = 0;
        for (SmelteryFluid fluid : this.fluids) {
            sum += fluid.height;
        }

        return (sum > 100) ? sum : 100;
    }

    @Override
    public void render(int x, int y) {
        if (!sneaking) {
            ElementProgressRender.render(new ProgressStyleTank(), 0, capacity, x, y, 100, 12);
            int xOffset = 0;
            for (int i = 0; i < fluids.size(); i++) {
                SmelteryFluid fluid = fluids.get(i);
                if (i > 0) {
                    xOffset += Math.ceil(100 * fluids.get(i - 1).amount / capacity);
                }
                ElementProgressRender.render(new ProgressStyleSmelteryFluid().backgroundColor(0x00ffffff).filledColor(fluid.color).alternateFilledColor(fluid.darker).showText(false), fluid.amount, capacity, x + xOffset, y, 100, 12);
            }
            for (int i = 1; i < 10; i++) {
                RenderHelper.drawVerticalLine(x + i * 10, y + 1, y + (i == 5 ? 11 : 6), 0xff767676);
            }
            RenderHelper.drawVerticalLine(x + 99, y, y + 12, 0xff969696);
        } else {
            ElementRenderHelper.drawGreyBox(x, y, x + 100, y + gaugeHeight + 2);
            int yOffset = 0;
            for (int i = 0; i < fluids.size(); i++) {
                SmelteryFluid fluid = fluids.get(i);
                if (i > 0) {
                    yOffset += fluids.get(i - 1).height;
                }

                drawVerticalProgress(x, y - yOffset, fluid.amount, capacity, fluid.color, fluid.darker, fluid.height);
                String text = fluid.amount + "mB of " + fluid.name;
                //noinspection MethodCallSideOnly
                drawSmallText(x + 98 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) / 2, y + gaugeHeight - 4 - yOffset, text, fluid.brighter2);
            }
            for (int i = 1; i < 10; i++) {
                RenderHelper.drawHorizontalLine(x + 1, y + 10 * i, x + (i == 5 ? 99 : (i % 2 == 0) ? 25 : 15), 0xaa767676);
            }
        }
        drawSmallText(x + 1, y + (sneaking ? gaugeHeight + 4 : 13), "" + "Smeltery", 0xffffffff);
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return sneaking ? gaugeHeight + 8 : 18;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(fluids.size());
        buf.writeInt(this.capacity);
        fluids.forEach(fluid -> {
            NetworkTools.writeString(buf, fluid.name);
            buf.writeInt(fluid.amount);
            buf.writeInt(fluid.color);
        });
        buf.writeBoolean(this.sneaking);
    }

    private void drawVerticalProgress(int x, int y, int amount, int capacity, int color, int dcolor, int height) {
        for (int i = 0; i < height; i++) {
            RenderHelper.drawHorizontalLine(x + 1, y + gaugeHeight - i, x + 99, (y + gaugeHeight - 2 - i) % 2 == 0 ? dcolor : color);
        }
    }

    @Override
    public int getID() {
        return id;
    }

    private final class SmelteryFluid {

        final String name;
        final int amount;
        final int color;
        final int darker;
        final int brighter2;

        int height;
        boolean wasTooShort = false;

        SmelteryFluid(String name, int amount, int color, int capacity) {
            this.name = name;
            this.amount = amount;
            this.color = color;
            this.darker = new Color(color).darker().hashCode();
            this.brighter2 = new Color(color).brighter().brighter().hashCode();

            height = (int) Math.floor(amount * 100 / capacity);
            if (height < 5) {
                height = 5;
                wasTooShort = true;
            }
        }

        boolean decreaseHeight(int amount) {
            if (wasTooShort) {
                return false;
            } else {
                height += amount;
            }
            return true;
        }
    }
}
