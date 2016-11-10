package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.styles.ProgressStyleTOPAddonGrey;

import com.teambrmodding.neotech.common.tiles.MachineGenerator;
import com.teambrmodding.neotech.common.tiles.MachineProcessor;
import com.teambrmodding.neotech.common.tiles.machines.TileGrinder;
import com.teambrmodding.neotech.common.tiles.machines.operators.TileTreeFarm;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

@TOPAddon(dependency = "neotech")
public class AddonNeoTech extends AddonBlank {

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile != null) {
            if (tile instanceof MachineProcessor && ((MachineProcessor) tile).canProcess()) {
                probeInfo.progress(100 * ((MachineProcessor) tile).cookTime() / ((MachineProcessor) tile).getCookTime(), 100, new ProgressStyleTOPAddonGrey().filledColor(0xff111111).alternateFilledColor(0xff222222).suffix("%").prefix("Progress: "));
            }

            if (tile instanceof TileGrinder) {
                probeInfo.progress(100 * ((TileGrinder) tile).progress()/((TileGrinder) tile).MAX_PROGRESS(), 100, new ProgressStyleTOPAddonGrey().filledColor(0xff111111).alternateFilledColor(0xff222222).suffix("%").prefix("Grinding: "));
            }

            if (tile instanceof TileTreeFarm) {
                if (!((TileTreeFarm) tile).hasSaplings()) {
                    probeInfo.text(TextFormatting.RED + "Out of saplings");
                }

                ItemStack axe = ((TileTreeFarm) tile).inventoryContents().get(((TileTreeFarm) tile).AXE_SLOT());
                if (axe != null) {
                    textPrefixed(probeInfo, "Durability", axe.getMaxDamage() - axe.getItemDamage() + "/" + axe.getMaxDamage());
                }

                if (mode == ProbeMode.EXTENDED) {
                    textPrefixed(probeInfo, "Range", ((TileTreeFarm) tile).RANGE() + " blocks");
                }


            }

            if (tile instanceof MachineGenerator) {
                    textPrefixed(probeInfo, "Generating", (((MachineGenerator) tile).didWork() ? ((MachineGenerator) tile).getEnergyProduced() : 0) + " RF/t");
            }
        }

        if (blockState.getBlock() instanceof BlockBasePressurePlate) {
            TileEntity belowTile = world.getTileEntity(data.getPos().down());
            if (belowTile != null && belowTile instanceof TileGrinder) {
                probeInfo.progress(100 * ((TileGrinder) belowTile).progress()/((TileGrinder) belowTile).MAX_PROGRESS(), 100, new ProgressStyleTOPAddonGrey().filledColor(0xff111111).alternateFilledColor(0xff222222).suffix("%").prefix("Grinding: "));
            }
        }
    }
}
