package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.api.TOPAddon;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import team.chisel.api.block.ICarvable;

@TOPAddon(dependency = "chisel")
public class AddonChisel extends AddonBlank {

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (blockState.getBlock() instanceof ICarvable) {
            ICarvable block = (ICarvable) blockState.getBlock();
            ItemStack stack = data.getPickBlock();
            String unloc = stack.getUnlocalizedName() + "." + block.getVariationData(stack.getItemDamage()).name + ".desc.1";
            probeInfo.text(TextFormatting.GRAY + IProbeInfo.STARTLOC + unloc + IProbeInfo.ENDLOC);
        }
    }
}
