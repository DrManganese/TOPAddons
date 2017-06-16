package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import io.github.drmanganese.topaddons.api.TOPAddon;

import com.robrit.moofluids.common.entity.EntityFluidCow;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

@TOPAddon(dependency = "moofluids")
public class AddonMooFluids extends AddonBlank {

    private boolean showMooFluid = true;

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        if (showMooFluid && entity instanceof EntityFluidCow) {
            Fluid cowFluid = ((EntityFluidCow) entity).getEntityFluid();
            FluidStack fluid = new FluidStack(cowFluid, 1000);
            textPrefixed(probeInfo, "Fluid", fluid.getLocalizedName());
        }
    }

    @Override
    public void updateConfigs(Configuration config) {
        showMooFluid = config.get("moofluids", "showMooFluid", true, "Show the Fluid of Fluid Cows.").setLanguageKey("topaddons.config:moofluids_show_moo_fluid").getBoolean();
    }
}
