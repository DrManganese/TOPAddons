package io.github.drmanganese.topaddons.elements.forestry;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import io.github.drmanganese.topaddons.elements.ElementRenderHelper;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.network.NetworkTools;

import static mcjty.theoneprobe.rendering.RenderHelper.renderItemStack;

public class ElementBeeHousingInventory implements IElement {

    private int id;

    private final NonNullList<ItemStack> inventoryStacks;
    private boolean isApiary = false;

    public ElementBeeHousingInventory(int id, boolean isApiary, NonNullList<ItemStack> inventoryStacks) {
        this.id = id;
        this.inventoryStacks = inventoryStacks;
        this.isApiary = isApiary;
    }

    public ElementBeeHousingInventory(ByteBuf buf) {
        int slots = 9;
        if (buf.readBoolean()) {
            isApiary = true;
            slots = 12;
        }
        this.inventoryStacks = NonNullList.withSize(slots, ItemStack.EMPTY);
        for (int i = 0; i < slots; i++) {
            this.inventoryStacks.set(i, NetworkTools.readItemStack(buf));
        }
    }

    @Override
    public void render(int x, int y) {
        Minecraft minecraft = Minecraft.getMinecraft();
        ElementRenderHelper.drawGreyBox(x + 9, y, x + 47, y + 20);
        if (inventoryStacks.get(0).getItem() != Item.getItemFromBlock(Blocks.BARRIER))
            renderItemStack(minecraft, minecraft.getRenderItem(), inventoryStacks.get(0), x + 11, y + 2, "");
        if (inventoryStacks.get(1).getItem() != Item.getItemFromBlock(Blocks.BARRIER))
            renderItemStack(minecraft, minecraft.getRenderItem(), inventoryStacks.get(1), x + 29, y + 2, inventoryStacks.get(1).getCount() + "");
        ElementRenderHelper.drawGreyBox(x, y + 22, x + 56, y + 80);
        for (int i = 2; i < 9; i++) {
            int xPos = x + 2;
            int yPos = y + 22;

            if (i < 4) {
                //X
                //X
                yPos += 8 + 20 * (i - 2);
            } else if (i < 7) {
                // X
                // X
                // X
                xPos += 18;
                yPos += 20 * (i - 4);
            } else {
                //  X
                //  X
                xPos += 36;
                yPos += 8 + 20 * (i - 7);
            }


            if (inventoryStacks.get(i).getItem() != Item.getItemFromBlock(Blocks.BARRIER)) {
                renderItemStack(minecraft, minecraft.getRenderItem(), inventoryStacks.get(i), xPos, yPos, inventoryStacks.get(i).getCount() + "");
            }
        }

        if (isApiary) {
            ElementRenderHelper.drawGreyBox(x + 58, y + 22, x + 78, y + 80);
            for (int i = 9; i < 12; i++) {
                if (inventoryStacks.get(i).getItem() != Item.getItemFromBlock(Blocks.BARRIER)) {
                    renderItemStack(minecraft, minecraft.getRenderItem(), inventoryStacks.get(i), x + 60, y + 24 + 19 * (i-9), "");
                }
            }
        }
    }

    @Override
    public int getWidth() {
        return 79;
    }

    @Override
    public int getHeight() {
        return 80;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isApiary);
        for (ItemStack inventoryStack : inventoryStacks) {
            NetworkTools.writeItemStack(buf, inventoryStack);
        }
    }

    @Override
    public int getID() {
        return id;
    }
}
