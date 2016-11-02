package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import io.github.drmanganese.topaddons.config.Config;
import io.github.drmanganese.topaddons.TOPRegistrar;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.config.capabilities.ModCapabilities;
import io.github.drmanganese.topaddons.elements.ElementTankGauge;
import io.github.drmanganese.topaddons.reference.Colors;
import io.github.drmanganese.topaddons.reference.Names;

import java.awt.*;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

@TOPAddon(dependency = "Forge", fancyName = "Base", order = 1)
public class AddonForge extends AddonBlank {

    public static int ELEMENT_TANK;

    public static IProbeInfo addTankElement(IProbeInfo probeInfo, String name, String fluidName, int amount, int capacity, String suffix, int color, ProbeMode mode) {
        return probeInfo.element(new ElementTankGauge(name, fluidName, amount, capacity, suffix, color, mode == ProbeMode.EXTENDED));
    }

    public static IProbeInfo addTankElement(IProbeInfo probeInfo, String name, FluidTankInfo tank, ProbeMode mode) {
        return addTankElement(probeInfo, name, tank, mode, 0);
    }

    public static IProbeInfo addTankElement(IProbeInfo probeInfo, String name, FluidTankInfo tank, ProbeMode mode, int i) {
        if (tank.fluid == null) {
            return probeInfo.element(new ElementTankGauge(name, "", 0, 0, "mB", 0, mode == ProbeMode.EXTENDED));
        } else {
            int color = 0xff777777;
            if (tank.fluid.getFluid().getColor(tank.fluid) != 0xffffffff) {
                color = tank.fluid.getFluid().getColor(tank.fluid);
            } else if (Colors.fluidColorMap.containsKey(tank.fluid.getFluid())) {
                color = Colors.fluidColorMap.get(tank.fluid.getFluid());
            } else if (Colors.fluidNameColorMap.containsKey(tank.fluid.getFluid().getName())) {
                color = Colors.fluidNameColorMap.get(tank.fluid.getFluid().getName());
            }

            if (Colors.fluidColorMap.containsKey(tank.fluid.getFluid())) {
                color = Colors.fluidColorMap.get(tank.fluid.getFluid()).hashCode();
            }
            return probeInfo.element(new ElementTankGauge(name, tank.fluid.getLocalizedName(), tank.fluid.amount, tank.capacity, "mB", color, mode == ProbeMode.EXTENDED));
        }
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (!Config.Forge.showTankGauge || !player.getCapability(ModCapabilities.OPTIONS, null).getBoolean("fluidGauge"))
            return;

        /* Disable for enderio, endertanks */
        String modid = ForgeRegistries.BLOCKS.getKey(blockState.getBlock()).getResourceDomain();
        if (modid.equals("enderio") || modid.equals("endertanks"))
            return;

        TileEntity tile = world.getTileEntity(data.getPos());

        if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
            IFluidHandler capability = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
            IFluidTankProperties[] tanks;
            try {
                tanks = capability.getTankProperties();
            } catch (NullPointerException ignored) {
                return;
            }

            for (int i = 0; i < tanks.length; i++) {
                IFluidTankProperties tank = tanks[i];
                int color = 0xff777777;
                String tankName = "Tank";
                if (Names.tankNamesMap.containsKey(tile.getClass())) {
                    tankName = Names.tankNamesMap.get(tile.getClass())[i];
                }
                /**
                 * Fluid color: - If the fluid doesn't return white in getColor, use this value;
                 *              - if the fluid is registered by an addon, use its color;
                 *              - if the fluid's name is stored in {@link Colors.fluidNameColorMap}, use that value;
                 *              - otherwise use 0xff777777 (gray-ish)
                 */
                if (tank.getContents() != null) {
                    Fluid fluid = tank.getContents().getFluid();
                    if (fluid.getColor(tank.getContents()) != 0xffffffff) {
                        color = fluid.getColor(tank.getContents());
                    } else if (Colors.fluidColorMap.containsKey(fluid)) {
                        color = Colors.fluidColorMap.get(fluid);
                    } else if (Colors.fluidNameColorMap.containsKey(tank.getContents().getFluid().getName())) {
                        color = Colors.fluidNameColorMap.get(tank.getContents().getFluid().getName());
                    }
                    addTankElement(probeInfo, tankName, tank.getContents().getFluid().getLocalizedName(tank.getContents()), tank.getContents().amount, tank.getCapacity(), "mB", color, mode);
                } else {
                    addTankElement(probeInfo, tankName, "", 0, 0, "", color, mode);
                }
            }

        } else if (tile instanceof net.minecraftforge.fluids.IFluidHandler) {
            net.minecraftforge.fluids.IFluidHandler handler = (net.minecraftforge.fluids.IFluidHandler) tile;
            FluidTankInfo[] tanks = handler.getTankInfo(null);
            if (tanks != null) {
                for (int i = 0; i < tanks.length; i++) {
                    if (tanks[i] != null) {
                        String tankName = "Tank";
                        if (Names.tankNamesMap.containsKey(tile.getClass())) {
                            tankName = Names.tankNamesMap.get(tile.getClass())[i];
                        }
                        if (tanks[i].fluid != null) {
                            addTankElement(probeInfo, tankName, tanks[i], mode);
                        } else {
                            addTankElement(probeInfo, tankName, "", 0, 0, "", 0xff777777, mode);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (player.getCapability(ModCapabilities.OPTIONS, null).getBoolean("hideTOPTank"))
            config.showTankSetting(IProbeConfig.ConfigMode.NOT);
        else
            config.showTankSetting(IProbeConfig.ConfigMode.EXTENDED);
    }

    @Override
    public void addFluidColors() {
        Colors.fluidColorMap.put(FluidRegistry.WATER, new Color(52, 95, 218).hashCode());
        Colors.fluidColorMap.put(FluidRegistry.LAVA, new Color(230, 145, 60).hashCode());
    }

    @Override
    public void registerElements() {
        ELEMENT_TANK = TOPRegistrar.GetTheOneProbe.probe.registerElementFactory(ElementTankGauge::new);
    }
}
