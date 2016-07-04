package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTankInfo;
import io.github.drmanganese.topaddons.TOPRegistrar;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.elements.ElementTankGauge;
import io.github.drmanganese.topaddons.reference.Colors;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

@TOPAddon(dependency = "Forge", fancyName = "Base")
public class AddonForge extends AddonBlank {

    public static int ELEMENT_TANK;

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
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    }

    @Override
    public void registerElements() {
        ELEMENT_TANK = TOPRegistrar.GetTheOneProbe.probe.registerElementFactory(ElementTankGauge::new);
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
