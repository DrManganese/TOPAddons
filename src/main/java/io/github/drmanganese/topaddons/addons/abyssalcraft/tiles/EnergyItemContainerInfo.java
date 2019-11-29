package io.github.drmanganese.topaddons.addons.abyssalcraft.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import com.shinoow.abyssalcraft.api.energy.IEnergyContainerItem;
import com.shinoow.abyssalcraft.lib.util.blocks.ISingletonInventory;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EnergyItemContainerInfo {

    private static void tryShowItemEnergy(IProbeInfo probeInfo, ItemStack itemStack) {
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IEnergyContainerItem) {
            final IEnergyContainerItem containerItem = (IEnergyContainerItem) itemStack.getItem();
            probeInfo.horizontal(Styles.horiCentered())
                    .item(itemStack)
                    .progress(
                            Math.round(containerItem.getContainedEnergy(itemStack)),
                            containerItem.getMaxEnergy(itemStack),
                            new ProgressStyle()
                                    .width(81)
                                    .height(12)
                                    .borderColor(0xff555555)
                                    .backgroundColor(0x55555555)
                                    .filledColor(0xff0b3a26)
                                    .alternateFilledColor(0xff0f3132)
                                    .suffix(" PE")
                                    .width(81)
                    );
        }
    }

    public static class Single implements ITileInfo<ISingletonInventory> {

        @Override
        public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, ISingletonInventory tile) {
            tryShowItemEnergy(probeInfo, tile.getItem());
        }
    }

    public static class Multi implements ITileInfo<IInventory> {

        @Override
        public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, IInventory tile) {
            for (int i = 0; i < tile.getSizeInventory(); i++) {
                tryShowItemEnergy(probeInfo, tile.getStackInSlot(i));
            }
        }
    }
}