package io.github.drmanganese.topaddons.addons;

import io.github.drmanganese.topaddons.api.TOPAddon;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import com.timwoodcreates.roost.data.DataChicken;
import com.timwoodcreates.roost.data.DataChickenModded;
import com.timwoodcreates.roost.tileentity.TileEntityBreeder;
import com.timwoodcreates.roost.tileentity.TileEntityRoost;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

import javax.annotation.Nonnull;

@TOPAddon(dependency = "roost")
public class AddonRoost extends AddonBlank {

    /**
     * Show the roost chicken icon with its name and, if applicable, its stats
     * @param probeInfo
     * @param chicken
     */
    private static void chickenProbe(IProbeInfo probeInfo, @Nonnull DataChicken chicken) {
        final IProbeInfo vert = probeInfo
                .horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                .item(chicken.buildChickenStack())
                .vertical()
                .text(chicken.getDisplayName());

        if (chicken instanceof DataChickenModded) {
            vert.text(chicken.getDisplaySummary().replaceAll(".*\\s", ""));

        }
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        final TileEntity tile = world.getTileEntity(data.getPos());

        if (tile instanceof TileEntityRoost) {
            final TileEntityRoost roost = (TileEntityRoost) tile;
            final DataChicken chicken = roost.createChickenData();
            if (chicken != null) {
                chickenProbe(probeInfo, chicken);
                progressBar(probeInfo, (int) (100 * roost.getProgress()), 0xffd2af0e, 0xffc19d0f);
            }
        }

        if (tile instanceof TileEntityBreeder) {
            final TileEntityBreeder breeder = (TileEntityBreeder) tile;
            final IItemHandler handler = breeder.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (handler != null) {
                final DataChicken chicken0 = DataChicken.getDataFromStack(handler.getStackInSlot(0));
                final DataChicken chicken1 = DataChicken.getDataFromStack(handler.getStackInSlot(1));

                int chickens = 0;
                if (chicken0 != null) {
                    chickenProbe(probeInfo, chicken0);
                    chickens++;
                }

                if (chicken1 != null) {
                    chickenProbe(probeInfo, chicken1);
                    chickens++;
                }

                if (chickens == 2) {
                    progressBar(probeInfo, (int) (100 * breeder.getProgress()), 0xffa00e19, 0xff4e070c);
                }
            }
        }
    }
}
