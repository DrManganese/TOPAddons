package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import io.github.drmanganese.topaddons.TOPRegistrar;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.elements.ElementTankGauge;
import io.github.drmanganese.topaddons.reference.Colors;
import io.github.drmanganese.topaddons.reference.Names;

import java.awt.*;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

@TOPAddon(dependency = "Forge", fancyName = "Base", order = 0)
public class AddonForge extends AddonBlank {

    public static int ELEMENT_TANK;

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, data.getSideHit())) {
            IFluidHandler capability = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, data.getSideHit());
            for (int i = 0; i < capability.getTankProperties().length; i++) {
                IFluidTankProperties tank = capability.getTankProperties()[i];
                int color = 0xff2222dd;
                String tankName = "Tank";
                if (Names.tankNamesMap.containsKey(tile.getClass())) {
                    tankName = Names.tankNamesMap.get(tile.getClass())[i];
                }
                if (tank.getContents() != null) {
                    if (Colors.fluidColorMap.containsKey(tank.getContents().getFluid())) {
                        color = Colors.fluidColorMap.get(tank.getContents().getFluid());
                    }
                    addTankElement(probeInfo, tankName, tank.getContents().getFluid().getLocalizedName(tank.getContents()), tank.getContents().amount, tank.getCapacity(), color, mode);
                } else {
                    addTankElement(probeInfo, tankName, "", 0, 0, color, mode);
                }
            }
        }
    }

    @Override
    public void addFluidColors() {
        Colors.fluidColorMap.put(FluidRegistry.WATER, new Color(52, 95, 218).hashCode());
        Colors.fluidColorMap.put(FluidRegistry.LAVA, new Color(230, 145, 60).hashCode());
    }

    @Override
    public void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {

    }

    @Override
    public void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {

    }

    @Override
    public void registerElements() {
        ELEMENT_TANK = TOPRegistrar.GetTheOneProbe.probe.registerElementFactory(ElementTankGauge::new);
    }

    public static IProbeInfo addTankElement(IProbeInfo probeInfo, String name, String fluidName, int amount, int capacity, int color, ProbeMode mode) {
        return probeInfo.element(new ElementTankGauge(name, fluidName, amount, capacity, color, mode == ProbeMode.EXTENDED));
    }

    public static IProbeInfo addTankElement(IProbeInfo probeInfo, String name, FluidTankInfo tank, ProbeMode mode) {
        return addTankElement(probeInfo, name, tank, mode, 0);
    }

    public static IProbeInfo addTankElement(IProbeInfo probeInfo, String name, FluidTankInfo tank, ProbeMode mode, int i) {
        if (tank.fluid == null) {
            return probeInfo.element(new ElementTankGauge(name, "", 0, 0, 0, mode == ProbeMode.EXTENDED));
        } else {
            int color = tank.fluid.getFluid().getColor(tank.fluid);
            if (Colors.fluidColorMap.containsKey(tank.fluid.getFluid())) {
                color = Colors.fluidColorMap.get(tank.fluid.getFluid()).hashCode();
            }
            return probeInfo.element(new ElementTankGauge(name, tank.fluid.getLocalizedName(), tank.fluid.amount, tank.capacity, color, mode == ProbeMode.EXTENDED));
        }
    }
}
