package io.github.drmanganese.topaddons.elements.forestry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import io.github.drmanganese.topaddons.addons.AddonForestry;
import io.github.drmanganese.topaddons.elements.ElementRenderHelper;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;

import static mcjty.theoneprobe.rendering.RenderHelper.renderItemStack;

public class ElementForestryFarm implements IElement {

    private final NonNullList<ItemStack> farmIcons;
    private String oneDirection;

    private NonNullList<ItemStack> inventoryStacks;

    public ElementForestryFarm(NonNullList<ItemStack> farmIcons, String oneDirection, boolean renderInventory, NonNullList<ItemStack> inventoryStacks) {
        this.farmIcons = farmIcons;
        this.oneDirection = oneDirection;
        this.inventoryStacks = inventoryStacks;
    }

    public ElementForestryFarm(ByteBuf buf) {
        this.farmIcons = NonNullList.withSize(5, ItemStack.EMPTY);
        for (int i = 0; i < 5; i++) {
            this.farmIcons.set(i, NetworkTools.readItemStack(buf));
        }
        oneDirection = NetworkTools.readString(buf);
        if (buf.readBoolean()) {
            this.inventoryStacks = NonNullList.withSize(20, ItemStack.EMPTY);
            for (int i = 0; i < 20; i++) {
                this.inventoryStacks.set(i, NetworkTools.readItemStack(buf));
            }
        } else {
            this.inventoryStacks = NonNullList.create();
        }
    }

    @Override
    public void render(int x, int y) {
        Minecraft minecraft = Minecraft.getMinecraft();
        int centerX = x + 31;
        int centerY = y + 18;

        drawPlus(centerX - 22, centerY - 22, centerX + 38, centerY + 38);

        renderItemStack(minecraft, minecraft.getRenderItem(), farmIcons.get(4), centerX, centerY + 2, "");
        renderItemStack(minecraft, minecraft.getRenderItem(), farmIcons.get(0), centerX, centerY - 17, I18n.format("for.gui.solder." + oneDirection));
        renderItemStack(minecraft, minecraft.getRenderItem(), farmIcons.get(1), centerX + 19, centerY + 2, I18n.format("for.gui.solder." +nextDirection()));
        renderItemStack(minecraft, minecraft.getRenderItem(), farmIcons.get(2), centerX, centerY + 21, I18n.format("for.gui.solder." +nextDirection()));
        renderItemStack(minecraft, minecraft.getRenderItem(), farmIcons.get(3), centerX - 19, centerY + 2, I18n.format("for.gui.solder." +nextDirection()));
        nextDirection();

        if (this.inventoryStacks.size() > 0) {
            ElementRenderHelper.drawGreyBox(x, y + 60, x + 38, y + 116);
            ElementRenderHelper.drawGreyBox(x + 40, y + 60, x + 78, y + 116);
            ElementRenderHelper.drawGreyBox(x , y + 120, x + 38, y + 158);
            ElementRenderHelper.drawGreyBox(x + 40, y + 120, x + 78, y + 158);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 2; k++) {
                        int xOffset = x + 2 + 4 * i + (2 * i + (k % 2)) * 18;
                        int yOffset = y + 62 + j * 18;
                        int slot = i * 6 + j * 2 + k;

                        if (!inventoryStacks.get(slot).isEmpty()) {
                            renderItemStack(minecraft, minecraft.getRenderItem(), inventoryStacks.get(slot), xOffset, yOffset, inventoryStacks.get(slot).getCount() + "");
                        }
                    }
                }
            }

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 2; k++) {
                        int xOffset = x + 2 + 4 * i + (2 * i + (k % 2)) * 18;
                        int yOffset = y + 122 + j * 18;
                        int slot = i * 4 + j * 2 + k + 12;

                        if (!inventoryStacks.get(slot).isEmpty()) {
                            renderItemStack(minecraft, minecraft.getRenderItem(), inventoryStacks.get(slot), xOffset, yOffset, inventoryStacks.get(slot).getCount() + "");
                        }
                    }
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
        return (inventoryStacks.size() > 0) ? 160 : 60;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        for (ItemStack farmIcon : farmIcons) {
            NetworkTools.writeItemStack(buf, farmIcon);
        }

        NetworkTools.writeString(buf, oneDirection);
        if (inventoryStacks.size() > 0) {
            buf.writeBoolean(true);
            for (ItemStack inventoryStack : inventoryStacks) {
                NetworkTools.writeItemStack(buf, inventoryStack);
            }
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public int getID() {
        return AddonForestry.ELEMENT_FARM;
    }

    private String nextDirection() {
        switch (this.oneDirection) {
            case "north":
                this.oneDirection = "east";
                return "east";
            case "east":
                this.oneDirection = "south";
                return "south";
            case "south":
                this.oneDirection = "west";
                return "west";
            case "west":
                this.oneDirection = "north";
                return "north";
            default:
                return this.oneDirection;
        }
    }

    private static void drawPlus(int x1, int y1, int x2, int y2) {
        Gui.drawRect(x1 + 21, y1 + 4, x2 - 21, y2 - 37, 0x44969696);
        Gui.drawRect(x1 + 21, y1 + 41, x2 - 21, y2, 0x44969696);
        Gui.drawRect(x1 + 2, y1 + 23, x2 - 2, y2 - 19, 0x44969696);

        //TOP
        RenderHelper.drawHorizontalLine(x1 + 21, y1 + 3, x2 - 21, 0xff969696);
        RenderHelper.drawVerticalLine(x1 + 20, y1 + 3, y1 + 23, 0xff969696);
        RenderHelper.drawVerticalLine(x2 - 21, y1 + 3, y1 + 23, 0xff969696);

        //RIGHT
        RenderHelper.drawHorizontalLine(x1 + 40, y1 + 22, x2 - 2, 0xff969696);
        RenderHelper.drawVerticalLine(x2 - 2, y1 + 22, y1 + 42, 0xff969696);
        RenderHelper.drawHorizontalLine(x1 + 40, y1 + 41, x2 - 2, 0xff969696);

        //BOTTOM
        RenderHelper.drawVerticalLine(x1 + 20, y1 + 41, y1 + 61, 0xff969696);
        RenderHelper.drawVerticalLine(x2 - 21, y1 + 41, y1 + 61, 0xff969696);
        RenderHelper.drawHorizontalLine(x1 + 21, y1 + 60, x2 - 21, 0xff969696);

        //LEFT
        RenderHelper.drawHorizontalLine(x1 + 2, y1 + 22, x2 - 40, 0xff969696);
        RenderHelper.drawVerticalLine(x1 + 1, y1 + 22, y1 + 42, 0xff969696);
        RenderHelper.drawHorizontalLine(x1 + 2, y1 + 41, x2 - 40, 0xff969696);
    }
}
