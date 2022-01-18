package io.github.drmanganese.topaddons.elements.top;

import io.github.drmanganese.topaddons.TopAddons;
import io.github.drmanganese.topaddons.util.ElementHelper;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IItemStyle;
import mcjty.theoneprobe.apiimpl.client.ElementItemStackRender;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;

/**
 * Draws an itemstack with a colored square behind it.
 */
public class ElementItemStackBackground implements IElement {

    public static final ResourceLocation ID = new ResourceLocation(TopAddons.MOD_ID, "itemstack_background");
    
    private final ItemStack itemStack;
    private final int color;
    private final IItemStyle style;

    public ElementItemStackBackground(ItemStack itemStack, int color, IItemStyle style) {
        this.itemStack = itemStack;
        this.color = color;
        this.style = style;
    }

    public ElementItemStackBackground(FriendlyByteBuf buf) {
        this.itemStack = buf.readItem();
        this.color = buf.readInt();
        this.style = new ItemStyle()
            .width(buf.readInt())
            .height(buf.readInt());
    }

    @Override
    public void render(PoseStack poseStack, int x, int y) {
        ElementHelper.drawBox(poseStack, x, y, style.getWidth(), style.getHeight(), this.color, 0, -1);
        ElementItemStackRender.render(this.itemStack, this.style, poseStack, x + 1, y + 1);
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
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(this.itemStack);
        buf.writeInt(this.color);
        buf.writeInt(this.style.getWidth());
        buf.writeInt(this.style.getHeight());
    }
}
