package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.robrit.moofluids.common.entity.EntityFluidCow;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.config.Config;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

@TOPAddon(dependency = "moofluids")
public class AddonMooFluids extends AddonBlank {

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

	}

	@Override
	public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
		if(Config.MooFluids.showFluid && entity instanceof EntityFluidCow) {

			Fluid cowFluid = ((EntityFluidCow) entity).getEntityFluid();
			FluidStack fluid = new FluidStack(cowFluid,1000);
			probeInfo.text("Fluid: " + fluid.getLocalizedName());
		}
	}
}
