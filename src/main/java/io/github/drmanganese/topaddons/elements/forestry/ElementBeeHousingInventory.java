package io.github.drmanganese.topaddons.elements.forestry;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import io.github.drmanganese.topaddons.addons.AddonForestry;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;

import static mcjty.theoneprobe.rendering.RenderHelper.renderItemStack;

public class ElementBeeHousingInventory implements IElement {

    private final ItemStack[] inventoryStacks;
    private boolean isApiary = false;

    public ElementBeeHousingInventory(boolean isApiary, ItemStack[] inventoryStacks) {
        this.inventoryStacks = inventoryStacks;
        this.isApiary = isApiary;
    }

    public ElementBeeHousingInventory(ByteBuf buf) {
        int slots = 9;
        if (buf.readBoolean()) {
            isApiary = true;
            slots = 12;
        }
        this.inventoryStacks = new ItemStack[slots];
        for (int i = 0; i < slots; i++) {
            this.inventoryStacks[i] = NetworkTools.readItemStack(buf);
        }
    }

    @Override
    public void render(int x, int y) {
        Minecraft minecraft = Minecraft.getMinecraft();
        RenderHelper.drawBeveledBox(x + 9, y, x + 47, y + 20, 0xff969696, 0xff969696, 0x44969696);
        if (inventoryStacks[0].getItem() != Item.getItemFromBlock(Blocks.BARRIER))
            renderItemStack(minecraft, minecraft.getRenderItem(), inventoryStacks[0], x + 11, y + 2, "");
        if (inventoryStacks[1].getItem() != Item.getItemFromBlock(Blocks.BARRIER))
            renderItemStack(minecraft, minecraft.getRenderItem(), inventoryStacks[1], x + 29, y + 2, inventoryStacks[1].stackSize + "");
        RenderHelper.drawBeveledBox(x, y + 22, x + 56, y + 80, 0xff969696, 0xff969696,0x44969696);
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


            if (inventoryStacks[i].getItem() != Item.getItemFromBlock(Blocks.BARRIER)) {
                renderItemStack(minecraft, minecraft.getRenderItem(), inventoryStacks[i], xPos, yPos, inventoryStacks[i].stackSize + "");
            }
        }

        if (isApiary) {
            RenderHelper.drawBeveledBox(x + 58, y + 22, x + 78, y + 80, 0xff969696, 0xff969696,0x44969696);
            for (int i = 9; i < 12; i++) {
                if (inventoryStacks[i].getItem() != Item.getItemFromBlock(Blocks.BARRIER)) {
                    renderItemStack(minecraft, minecraft.getRenderItem(), inventoryStacks[i], x + 60, y + 24 + 19 * (i-9), "");
                }
            }
        }
    }

    @Override
    public int getWidth() {
        return 60;
    }

    @Override
    public int getHeight() {
        return 80;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isApiary);
        for (ItemStack inventoryStack : inventoryStacks) {
            if (inventoryStack != null)
                NetworkTools.writeItemStack(buf, inventoryStack);
            else
                NetworkTools.writeItemStack(buf, new ItemStack(Blocks.BARRIER, 0));
        }
    }

    @Override
    public int getID() {
        return AddonForestry.ELEMENT_BEE_INV;
    }
}
