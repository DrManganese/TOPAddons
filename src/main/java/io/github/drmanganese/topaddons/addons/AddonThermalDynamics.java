package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.api.TOPAddon;

import cofh.thermaldynamics.duct.entity.DuctUnitTransport;
import cofh.thermaldynamics.duct.tiles.TileTransportDuct;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

@TOPAddon(dependency = "thermaldynamics")
public class AddonThermalDynamics extends AddonBlank {

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());

        if (tile instanceof TileTransportDuct && !(tile instanceof TileTransportDuct.LongRange) && !(tile instanceof TileTransportDuct.Linking)) {
            DuctUnitTransport unit = (DuctUnitTransport) ((TileTransportDuct) tile).getDuctUnits().iterator().next();
            if (unit.isOutput()) {
                IProbeInfo hori = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xffbcece0)).horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));

                if (!unit.data.item.isEmpty()) {
                    hori.item(unit.data.item);
                }

                hori.text(!unit.data.name.isEmpty() ? unit.data.name : "Unnamed");
            }
        }
    }
}
