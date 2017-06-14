package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.api.TOPAddon;

import com.teambrmodding.neotech.common.tiles.MachineGenerator;
import com.teambrmodding.neotech.common.tiles.machines.operators.TileTreeFarm;
import com.teambrmodding.neotech.common.tiles.traits.IUpgradeItem;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

@TOPAddon(dependency = "neotech")
public class AddonNeoTech extends AddonBlank {

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());

        if (tile instanceof TileTreeFarm) {
            ItemStack axe = ((TileTreeFarm) tile).inventoryContents.get(TileTreeFarm.AXE_SLOT);
            if (axe != null) {
                textPrefixed(probeInfo, "{*topaddons.neotech:durability*}", axe.getMaxDamage() - axe.getItemDamage() + "/" + axe.getMaxDamage());
            }

            if (mode == ProbeMode.EXTENDED) {
                int range = 7 + 2 * ((TileTreeFarm) tile).getModifierForCategory(IUpgradeItem.ENUM_UPGRADE_CATEGORY.MEMORY);
                textPrefixed(probeInfo, "{*neotech.text.rangeTree*}", String.format("%dx%d", range, range));
            }
        }

        if (tile instanceof MachineGenerator) {
            textPrefixed(probeInfo, "{*topaddons:generating*}", ((MachineGenerator) tile).getEnergyProduced() + " RF/t");
        }
    }
}
