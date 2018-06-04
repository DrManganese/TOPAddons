package io.github.drmanganese.topaddons.elements.top;

import io.github.drmanganese.topaddons.styles.SimpleProgressStyle;
import io.github.drmanganese.topaddons.util.ElementHelper;

import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;

/**
 * A progress bar with its text centered.
 */
public class ElementSimpleProgressCentered implements IElement {

    private final long current, max;
    private final SimpleProgressStyle style;
    private final String text;
    private int id;

    public ElementSimpleProgressCentered(int id, long current, long max, SimpleProgressStyle style, @Nullable String text) {
        this.id = id;
        this.current = current;
        this.max = max;
        this.style = style;
        this.text = text == null ? Long.toString(current) : text;
    }

    public ElementSimpleProgressCentered(ByteBuf buf) {
        this.current = buf.readLong();
        this.max = buf.readLong();
        this.style = new SimpleProgressStyle()
                .width(buf.readInt())
                .height(buf.readInt())
                .backgroundColor(buf.readInt())
                .borderColor(buf.readInt())
                .fillColor(buf.readInt())
                .alternateFillColor(buf.readInt());
        this.text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void render(int x, int y) {
        ElementHelper.drawBox(x, y, style.getWidth(), style.getHeight(), style.getBackgroundColor(), 1, style.getBorderColor());
        if (current > 0 && max > 0) {
            int dx = (int) Math.min((current * (style.getWidth() - 2) / max), style.getWidth() - 2);

            for (int i = 0; i < dx; i++) {
                RenderHelper.drawVerticalLine(x + i + 1, y + 1, y + style.getHeight() - 1, i % 2 == 0 ? style.getFillColor() : style.getAlternateFillColor());
            }

        }

        final int textWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, x + (style.getWidth() - textWidth) / 2.0F, y + 2, 0xffffffff);
    }

    @Override
    public int getWidth() {
        return style.getWidth();
    }

    @Override
    public int getHeight() {
        return style.getHeight();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.current);
        buf.writeLong(this.max);
        buf.writeInt(this.style.getWidth());
        buf.writeInt(this.style.getHeight());
        buf.writeInt(this.style.getBackgroundColor());
        buf.writeInt(this.style.getBorderColor());
        buf.writeInt(this.style.getFillColor());
        buf.writeInt(this.style.getAlternateFillColor());
        ByteBufUtils.writeUTF8String(buf, this.text);
    }

    @Override
    public int getID() {
        return id;
    }
}
