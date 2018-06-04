package io.github.drmanganese.topaddons.addons.industrialforegoing.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.elements.ElementSync;
import io.github.drmanganese.topaddons.elements.industrialforegoing.ElementCropSower;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import com.buuz135.industrial.tile.agriculture.CropSowerTile;
import net.ndrei.teslacorelib.inventory.LockableItemHandler;

import java.lang.reflect.Field;

/**
 * Seed layout using custom element which rotates with player facing.
 */
public class CropSowerInfo implements ITileInfo<CropSowerTile> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, CropSowerTile tile) {
        try {
            if (!tile.isPaused()) {
                Field f = tile.getClass().getDeclaredField("inPlant");
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                LockableItemHandler handler = (LockableItemHandler) f.get(tile);

                NonNullList<ItemStack> stacks;
                if (handler.getLocked()) {
                    stacks = NonNullList.from(ItemStack.EMPTY, handler.getFilter());
                } else {
                    stacks = NonNullList.withSize(handler.getSlots(), ItemStack.EMPTY);
                    for (int i = 0; i < handler.getSlots(); i++) {
                        stacks.set(i, handler.getStackInSlot(i));
                    }
                }

                probeInfo.element(new ElementCropSower(ElementSync.getId("sower_grid", player), stacks, EnumFacing.fromAngle(player.rotationYaw)));
            }
        } catch (Exception e) {
            //
        }
    }
}
