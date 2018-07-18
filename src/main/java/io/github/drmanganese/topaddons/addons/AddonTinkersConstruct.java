package io.github.drmanganese.topaddons.addons;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.elements.tconstruct.ElementSmelteryTank;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import slimeknights.tconstruct.library.smeltery.ISmelteryTankHandler;
import slimeknights.tconstruct.library.smeltery.SmelteryTank;
import slimeknights.tconstruct.smeltery.tileentity.TileSmeltery;
import slimeknights.tconstruct.smeltery.tileentity.TileTinkerTank;

import java.util.List;

@TOPAddon(dependency = "tconstruct")
public class AddonTinkersConstruct extends AddonBlank {

    @Override
    public void registerElements() {
        registerElement("smeltery", ElementSmelteryTank::new);
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());

        if (tile instanceof ISmelteryTankHandler) {
            SmelteryTank tank = ((ISmelteryTankHandler) tile).getTank();
            if (tile instanceof TileSmeltery) {
                final boolean inIngots = player.getCapability(TOPAddons.OPTS_CAP, null).getBoolean("smelteryInIngots");
                addSmelteryTankElement(probeInfo, tank.getFluids(), Math.max(tank.getFluidAmount(), tank.getCapacity()), inIngots, mode, player);
            }

            if (tile instanceof TileTinkerTank) {
                textPrefixed(probeInfo, "Capacity", tank.getCapacity() / 1000 + " B");
                addSmelteryTankElement(probeInfo, tank.getFluids(), Math.max(tank.getFluidAmount(), tank.getCapacity()), false, mode, player);
            }
        }
    }

    private void addSmelteryTankElement(IProbeInfo probeInfo, List<FluidStack> fluids, int capacity, boolean inIngots, ProbeMode mode, EntityPlayer player) {
        probeInfo.element(new ElementSmelteryTank(getElementId(player, "smeltery"), fluids, capacity, inIngots, mode == ProbeMode.EXTENDED));
    }
}
