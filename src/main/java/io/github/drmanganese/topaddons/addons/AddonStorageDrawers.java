package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.api.TOPAddon;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;

import java.util.ArrayList;
import java.util.List;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.Config;

@TOPAddon(dependency = "StorageDrawers")
public class AddonStorageDrawers extends AddonBlank {

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (world.getTileEntity(data.getPos()) != null && world.getTileEntity(data.getPos()) instanceof TileEntityDrawers) {
            TileEntityDrawers tile = (TileEntityDrawers) world.getTileEntity(data.getPos());

            if (tile.isShrouded()) {
                probeInfo.text(TextFormatting.GRAY + TextFormatting.ITALIC.toString() + "Shrouded");
                return;
            }


            if (mode == ProbeMode.EXTENDED) {
                List<ItemStack> stacks = new ArrayList<>();
                for (int i = 0; i < tile.getDrawerCount(); i++) {
                    ItemStack stack = tile.getDrawer(i).getStoredItemCopy();
                    if (stack != null) {
                        stacks.add(stack);
                    }
                }

                if (!stacks.isEmpty()) {
                    IProbeInfo vertical = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(Config.chestContentsBorderColor).spacing(0));
                    for (ItemStack stack : stacks) {
                        if (tile.isVending()) {
                            ItemStack infiStack = ItemStack.copyItemStack(stack);
                            infiStack.stackSize = 1;
                            vertical.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                                    .item(infiStack)
                                    .vertical(probeInfo.defaultLayoutStyle().spacing(0))
                                    .text(stack.getDisplayName())
                                    .text(TextFormatting.GRAY + "[\u221e]");

                        } else {
                            int r = stack.stackSize % 64;
                            int q = (stack.stackSize - r) / 64;
                            vertical.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).item(stack)
                                    .vertical(probeInfo.defaultLayoutStyle().spacing(0))
                                    .text(stack.getDisplayName())
                                    .text(TextFormatting.GRAY + "[" + (stack.stackSize >= 64 ? q + "x64 + " : "") + r + "]");
                        }
                    }
            }

            textPrefixed(probeInfo, "Stack limit", tile.isUnlimited() ? "\u221e" : tile.getDrawerCapacity() * tile.getEffectiveStorageMultiplier() + " (x" + tile.getEffectiveStorageMultiplier() + ")", TextFormatting.AQUA);
            if (tile.getOwner() != null && tile.getOwner().compareTo(player.getUniqueID()) != 0) {
                probeInfo.text(TextFormatting.RED + TextFormatting.ITALIC.toString() + "Protected");
            }
        }
    }

}

    @Override
    public void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (world.getTileEntity(data.getPos()) instanceof TileEntityDrawers && player.isSneaking()) {
            config.showChestContents(IProbeConfig.ConfigMode.NOT);
        }
    }
}