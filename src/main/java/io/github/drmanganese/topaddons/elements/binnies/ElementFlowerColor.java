package io.github.drmanganese.topaddons.elements.binnies;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.rendering.RenderHelper;

public class ElementFlowerColor implements IElement {

    private int id;

    private final int color;

    public ElementFlowerColor(int id, int color) {
        this.id = id;
        this.color = color;
    }

    public ElementFlowerColor(ByteBuf buf) {
        color = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(color);
    }

    @Override
    public void render(int x, int y) {
        RenderHelper.drawBeveledBox(x, y - 2, x + 8, y + 6, 0xffffffff, 0xffffffff, color + 0xff000000);
    }

    @Override
    public int getWidth() {
        return 8;
    }

    @Override
    public int getHeight() {
        return 8;
    }

    @Override
    public int getID() {
        return id;
    }
}
