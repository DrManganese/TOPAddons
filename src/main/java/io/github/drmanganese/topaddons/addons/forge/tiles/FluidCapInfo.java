package io.github.drmanganese.topaddons.addons.forge.tiles;

import io.github.drmanganese.topaddons.addons.forge.AddonForge;
import io.github.drmanganese.topaddons.api.ITileConfigProvider;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.elements.ElementSync;
import io.github.drmanganese.topaddons.elements.forge.ElementTankGauge;
import io.github.drmanganese.topaddons.util.PlayerHelper;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class FluidCapInfo implements ITileInfo<TileEntity>, ITileConfigProvider {

    public static final FluidCapInfo INSTANCE = new FluidCapInfo();
    public static final Map<String, Integer> COLORS = new HashMap<>();
    public static final Map<Class<? extends TileEntity>, String[]> TANK_NAMES = new HashMap<>();

    static {
        //Actually Additions
        COLORS.put("canolaoil", 0xffb9a12d);
        COLORS.put("refinedcanolaoil", 0xff51471a);
        COLORS.put("crystaloil", 0xff783c22);
        COLORS.put("empoweredoil", 0xffd33c52);
    }

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileEntity tile) {
        if (!PlayerHelper.getSync(player).getString("showGauge").equals("The One Probe")
                && !AddonForge.tankModBlacklist.contains(ForgeRegistries.BLOCKS.getKey(blockState.getBlock()).getNamespace())
                && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
            IFluidTankProperties[] tanks = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).getTankProperties();
            for (int i = 0; i < tanks.length; i++) {
                IFluidTankProperties tank = tanks[i];
                final String tankName = TANK_NAMES.containsKey(tile.getClass()) ? TANK_NAMES.get(tile.getClass())[i] : "Tank";

                if (tank.getContents() == null) {
                    probeInfo.element(new ElementTankGauge(ElementSync.getId("tank_gauge", player), probeMode == ProbeMode.EXTENDED, 0, tank.getCapacity(), tankName, "", -1));
                } else {
                    final int color = getFluidColor(tank.getContents());
                    final String fluidName = tank.getContents().getFluid().getLocalizedName(tank.getContents());
                    probeInfo.element(new ElementTankGauge(ElementSync.getId("tank_gauge", player), probeMode == ProbeMode.EXTENDED, tank.getContents().amount, tank.getCapacity(), tankName, fluidName, color));
                }
            }
        }
    }

    @Override
    public void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (!AddonForge.tankModBlacklist.contains(ForgeRegistries.BLOCKS.getKey(blockState.getBlock()).getNamespace())
                && world.getTileEntity(data.getPos()).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
            if (PlayerHelper.getSync(player).getString("showGauge").equals("TOP Addons")) {
                config.showTankSetting(IProbeConfig.ConfigMode.NOT);
            }
        }
    }

    private int getFluidColor(FluidStack fluidStack) {
        int fluidColor = fluidStack.getFluid().getColor(fluidStack);
        if (fluidColor != -1) {
            return fluidColor;
        } else {
            return COLORS.getOrDefault(fluidStack.getFluid().getName(), 0xff9797ff);
        }
    }
}
