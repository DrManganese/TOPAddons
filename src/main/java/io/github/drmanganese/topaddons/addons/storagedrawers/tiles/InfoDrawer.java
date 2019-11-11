package io.github.drmanganese.topaddons.addons.storagedrawers.tiles;

import io.github.drmanganese.topaddons.addons.storagedrawers.AddonStorageDrawers;
import io.github.drmanganese.topaddons.api.ITileConfigProvider;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.util.InfoHelper;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.config.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;

public class InfoDrawer implements ITileInfo<TileEntityDrawers>, ITileConfigProvider {

    @GameRegistry.ObjectHolder("theoneprobe:probe")
    public static Item PROBE;

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileEntityDrawers tile) {
        if (probeMode != ProbeMode.EXTENDED) return;
        if (AddonStorageDrawers.hideConcealed && tile.getDrawerAttributes().isConcealed()) return;

        final IDrawerGroup drawers = tile.getGroup();
        boolean empty = true;
        for (int i = 0; i < drawers.getDrawerCount(); i++) {
            if (!drawers.getDrawer(i).isEmpty()) {
                empty = false;
                break;
            }
        }
        if (empty) return;

        final IProbeInfo vert = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(Config.chestContentsBorderColor).spacing(2));
        for (int i = 0; i < drawers.getDrawerCount(); i++) {
            final IDrawer drawer = drawers.getDrawer(i);
            if (drawer.isEmpty()) continue;
            final boolean vendor = tile.upgrades().hasVendingUpgrade();

            final IProbeInfo hori = vert.horizontal(probeInfo.defaultLayoutStyle().spacing(0));
            final ItemStack stack = drawer.getStoredItemPrototype().copy();
            if (!vendor) {
                stack.setCount(Math.max(1, drawer.getStoredItemCount()));
            }

            hori.item(stack);
            final IProbeInfo vertText = hori.vertical(probeInfo.defaultLayoutStyle().spacing(0));
            vertText.itemLabel(drawer.getStoredItemPrototype());

            if (!vendor) {
                final int count = drawer.getStoredItemCount();
                final int stacks = count / drawer.getStoredItemStackSize();
                final int remainder = count % drawer.getStoredItemStackSize();
                if (stacks > 0) {
                    if (drawer.getStoredItemStackSize() > 1) {
                        if (remainder > 0) {
                            vertText.text(TextStyleClass.LABEL + "[" + stacks + "x" + drawer.getStoredItemStackSize() + " + " + remainder + "]");
                        } else {
                            vertText.text(TextStyleClass.LABEL + "[" + stacks + "x" + drawer.getStoredItemStackSize() + "]");
                        }
                    } else {
                        vertText.text(TextStyleClass.LABEL + "[" + count + "]");
                    }
                } else {
                    vertText.text(TextStyleClass.LABEL + "[" + remainder + "]");
                }
            } else {
                vertText.text(TextStyleClass.LABEL + "[\u221e]");
            }
        }

        InfoHelper.textPrefixed(probeInfo,
                "{*storagedrawers.waila.config.displayStackLimit*}",
                tile.getDrawerAttributes().isUnlimitedStorage() ? "\u221e" : tile.getDrawerCapacity() * tile.upgrades().getStorageMultiplier() + " (x" + tile.upgrades().getStorageMultiplier() + ")");
    }

    @Override
    public void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (player.isSneaking()
                || Config.extendedInMain && player.getHeldItemMainhand().getItem() == PROBE
                || ((TileEntityDrawers) world.getTileEntity(data.getPos())).getDrawerAttributes().isConcealed() && AddonStorageDrawers.hideConcealed) {
            config.showChestContents(IProbeConfig.ConfigMode.NOT);
        }
    }
}
