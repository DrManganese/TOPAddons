package io.github.drmanganese.topaddons.addons.forestry.tiles;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import net.minecraftforge.items.CapabilityItemHandler;

import forestry.core.inventory.InventoryPlain;
import forestry.core.tiles.TileForestry;
import forestry.core.tiles.TileNaturalistChest;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

import java.util.Arrays;

public class TileForestryStorageInfo implements ITileInfo<TileForestry> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileForestry tile) {
        final int invSize = tile.getSizeInventory();
        int stackCount = 0;
        for (int i = 0; i < tile.getSizeInventory(); i++)
            if (!tile.getStackInSlot(i).isEmpty())
                stackCount++;

        InfoHelper.textPrefixed(probeInfo, "Storage", String.format("%d/%d (%d%%)", stackCount, invSize, 100 * stackCount / invSize));
    }
}
