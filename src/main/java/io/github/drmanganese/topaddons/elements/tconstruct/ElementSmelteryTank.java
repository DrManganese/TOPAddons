package io.github.drmanganese.topaddons.elements.tconstruct;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;
import io.github.drmanganese.topaddons.addons.AddonTinkersConstruct;
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

    private final List<SmelteryFluid> fluids;
    private final int capacity;
    private final boolean sneaking;

    public ElementSmelteryTank(List<FluidStack> fluids, int capacity, boolean sneaking) {
        this.fluids = new ArrayList<>();
        fluids.forEach(fluidStack -> this.fluids.add(new SmelteryFluid(fluidStack.getLocalizedName(), fluidStack.amount, fluidStack.getFluid().getColor())));
        this.capacity = capacity;
        this.sneaking = sneaking;
    }

    public ElementSmelteryTank(ByteBuf buf) {
        this.fluids = new ArrayList<>();
        int count = buf.readInt();
        for (int i = 0; i < count; i++) {
            this.fluids.add(new SmelteryFluid(NetworkTools.readString(buf), buf.readInt(), buf.readInt()));
        }
        this.capacity = buf.readInt();
        this.sneaking = buf.readBoolean();
    }

    @Override
    public void render(int x, int y) {
        if (!sneaking) {
            ElementProgressRender.render(new ProgressStyleTank(), 0, capacity, x, y, 100, 12);
            int xOffset = 0;
            for (int i = 0; i < fluids.size(); i++) {
                SmelteryFluid fluid = fluids.get(i);
                if (i > 0) {
                    xOffset += fluids.get(i - 1).amount * 100 / capacity;
                }
                ElementProgressRender.render(new ProgressStyleSmelteryFluid().backgroundColor(0x00ffffff).filledColor(fluid.color).alternateFilledColor(fluid.darker).showText(false), fluid.amount, capacity, x + xOffset, y, 100, 12);
            }
            for (int i = 1; i < 10; i++) {
                RenderHelper.drawVerticalLine(x + i * 10, y + 1, y + (i == 5 ? 11 : 6), 0xff767676);
            }
            RenderHelper.drawVerticalLine(109, y, y + 12, 0xff969696);
        } else {
            RenderHelper.drawBeveledBox(x, y, x + 100, y + 100, 0xff767676, 0xff767676, 0x44767676);
            int yOffset = 0;
            for (int i = 0; i < fluids.size(); i++) {
                SmelteryFluid fluid = fluids.get(i);
                if (i > 0) {
                    yOffset += fluids.get(i - 1).amount * 100 / capacity;
                }

                drawVerticalProgress(x, y - yOffset, fluid.amount, capacity, fluid.color, fluid.darker);
                String text = fluid.amount + "mB of " + fluid.name;
                drawSmallText(x + 98 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(text)/2, y + 94 - yOffset, text, fluid.brighter2);
            }
            for (int i = 1; i < 10; i++) {
                RenderHelper.drawHorizontalLine(x, y + 10 * i, x + (i == 5 ? 100 : (i % 2 == 0) ? 25 : 15), 0xff767676);
            }
        }
        drawSmallText(x + 1, y + (sneaking ? 102 : 13),  "" + "Smeltery", 0xffffffff);
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return sneaking ? 107 : 18;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(fluids.size());
        fluids.forEach(fluid -> {
            NetworkTools.writeString(buf, fluid.name);
            buf.writeInt(fluid.amount);
            buf.writeInt(fluid.color);
        });
        buf.writeInt(this.capacity);
        buf.writeBoolean(this.sneaking);
    }

    @Override
    public int getID() {
        return AddonTinkersConstruct.ELEMENT_SMELTERY_TANK;
    }

    private void drawVerticalProgress(int x, int y, int amount, int capacity, int color, int dcolor) {
        int pct = (int) Math.floor(amount * 100 / capacity);
        for (int i = 0; i < pct; i++) {
            RenderHelper.drawHorizontalLine(x + 1, y + 98 - i, x + 99, (y + 98 - i) % 2 == 0 ? dcolor : color);
        }
    }

    private final class SmelteryFluid {
        final String name;
        final int amount;
        final int color;
        final int darker;
        final int brighter2;

        SmelteryFluid(String name, int amount, int color) {
            this.name = name;
            this.amount = amount;
            this.color = color;
            this.darker = new Color(color).darker().hashCode();
            this.brighter2 = new Color(color).brighter().brighter().hashCode();
        }
    }
}
