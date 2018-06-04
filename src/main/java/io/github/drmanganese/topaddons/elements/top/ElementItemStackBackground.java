package io.github.drmanganese.topaddons.elements.top;

import io.github.drmanganese.topaddons.util.ElementHelper;

import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IItemStyle;
import mcjty.theoneprobe.apiimpl.client.ElementItemStackRender;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import io.netty.buffer.ByteBuf;

/**
 * Draws an itemstack with a colored squared behind it.
 */
public class ElementItemStackBackground implements IElement {

    private final ItemStack itemStack;
    private final int color;
    private final IItemStyle style;

    private int id;

    public ElementItemStackBackground(int id, ItemStack itemStack, int color, IItemStyle style) {
        this.id = id;
        this.itemStack = itemStack;
        this.color = color;
        this.style = style;
    }

    public ElementItemStackBackground(ByteBuf buf) {
        this.itemStack = ByteBufUtils.readItemStack(buf);
        this.color = buf.readInt();
        this.style = new ItemStyle()
                .width(buf.readInt())
                .height(buf.readInt());
    }

    @Override
    public void render(int x, int y) {
        ElementHelper.drawBox(x, y, style.getWidth(), style.getHeight(), this.color, 0, -1);
        ElementItemStackRender.render(this.itemStack, this.style, x + 1, y + 1);
    }

    @Override
    public int getWidth() {
        return this.style.getWidth() + 3;
    }

    @Override
    public int getHeight() {
        return this.style.getHeight() + 2;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, this.itemStack);
        buf.writeInt(this.color);
        buf.writeInt(this.style.getWidth());
        buf.writeInt(this.style.getHeight());
    }

    @Override
    public int getID() {
        return this.id;
    }
}
