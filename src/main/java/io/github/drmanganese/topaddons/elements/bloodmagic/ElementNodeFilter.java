package io.github.drmanganese.topaddons.elements.bloodmagic;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import io.github.drmanganese.topaddons.addons.AddonBloodMagic;
import io.github.drmanganese.topaddons.elements.ElementRenderHelper;

import WayofTime.bloodmagic.item.inventory.ItemInventory;
import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;

public class ElementNodeFilter implements IElement {

    private final String side;
    private final ItemStack inventoryOnSide;
    private final ItemStack filterStack;

    public ElementNodeFilter(String side, ItemStack inventoryOnSide, ItemStack filterStack) {
        this.side = side;
        this.inventoryOnSide = inventoryOnSide;
        this.filterStack = filterStack;
    }

    public ElementNodeFilter(ByteBuf buf) {
        this.side = NetworkTools.readString(buf);
        this.inventoryOnSide = NetworkTools.readItemStack(buf);
        this.filterStack = NetworkTools.readItemStack(buf);
    }

    @Override
    public void render(int x, int y) {
        Minecraft mc = Minecraft.getMinecraft();
        ElementRenderHelper.drawGreyBox(x, y, x + 18, y + 18);
        RenderHelper.renderItemStack(mc, mc.getRenderItem(), inventoryOnSide, x + 1, y + 1, "");
        RenderHelper.renderItemStack(mc, mc.getRenderItem(), filterStack, x + 18, y + 1, "");
        ElementRenderHelper.drawGreyBox(x + 34, y, x + 180, y + 18);

        ItemInventory filterInv = new ItemInventory(filterStack, 9, "");
        int xOffset = 0;
        for (int i = 0; i < 9; i++) {
            if (filterInv.getStackInSlot(i) != null) {
                RenderHelper.renderItemStack(mc, mc.getRenderItem(), filterInv.getStackInSlot(i), x + 35 + xOffset, y + 1, "");
                xOffset += 16;
            }
        }
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writeString(buf, side);
        NetworkTools.writeItemStack(buf, inventoryOnSide);
        NetworkTools.writeItemStack(buf, filterStack);
    }

    @Override
    public int getID() {
        return AddonBloodMagic.ELEMENT_NODE_FILTER;
    }
}
