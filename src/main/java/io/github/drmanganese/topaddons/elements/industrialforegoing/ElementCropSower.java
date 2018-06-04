package io.github.drmanganese.topaddons.elements.industrialforegoing;

import io.github.drmanganese.topaddons.util.ElementHelper;

import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import com.buuz135.industrial.tile.agriculture.CropSowerTile;
import com.google.common.collect.ImmutableMap;
import io.netty.buffer.ByteBuf;

public class ElementCropSower implements IElement {

    private static final ImmutableMap<EnumFacing, int[]> SLOT_ORDER_MAP = ImmutableMap.of(
            EnumFacing.NORTH, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8},
            EnumFacing.EAST, new int[]{2, 5, 8, 1, 4, 7, 0, 3, 6},
            EnumFacing.SOUTH, new int[]{8, 7, 6, 5, 4, 3, 2, 1, 0},
            EnumFacing.WEST, new int[]{6, 3, 0, 7, 4, 1, 8, 5, 2}
    );

    //The sower's color list seems to be stored mirrored diagonally to how it's rendered
    private static final int[] MIRROR_MATRIX = {0, 3, 6, 1, 4, 7, 2, 5, 8};

    private final NonNullList<ItemStack> stacks;
    private final EnumFacing facing;

    private int id;

    public ElementCropSower(int id, NonNullList<ItemStack> stacks, EnumFacing facing) {
        this.id = id;
        this.stacks = stacks;
        this.facing = facing;
    }

    public ElementCropSower(ByteBuf byteBuf) {
        this.stacks = NonNullList.withSize(9, ItemStack.EMPTY);
        for (int i = 0; i < 9; i++) {
            this.stacks.set(i, ByteBufUtils.readItemStack(byteBuf));
        }
        this.facing = EnumFacing.values()[byteBuf.readInt()];
    }

    @Override
    public void render(int x, int y) {
        for (int i = 0; i < stacks.size(); i++) {
            final int stackIndex = SLOT_ORDER_MAP.get(facing)[i];
            ElementHelper.drawBox(x + (i % 3) * 17, y + (i / 3) * 17, 16, 16, CropSowerTile.colors[MIRROR_MATRIX[stackIndex]].getColorValue() + 0xaa000000, 0, -1);
            Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stacks.get(stackIndex), x + (i % 3) * 17, y + (i / 3) * 17);
        }

        ElementHelper.drawHorizontalLine(x, y + 16, 50, Config.rfbarBorderColor);
        ElementHelper.drawHorizontalLine(x, y + 33, 50, Config.rfbarBorderColor);
        ElementHelper.drawVerticalLine(x + 16, y, 50, Config.rfbarBorderColor);
        ElementHelper.drawVerticalLine(x + 33, y, 50, Config.rfbarBorderColor);
    }

    @Override
    public int getWidth() {
        return 50;
    }

    @Override
    public int getHeight() {
        return 50;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.stacks.forEach(stack -> ByteBufUtils.writeItemStack(buf, stack));
        buf.writeInt(this.facing.ordinal());
    }

    @Override
    public int getID() {
        return id;
    }
}
