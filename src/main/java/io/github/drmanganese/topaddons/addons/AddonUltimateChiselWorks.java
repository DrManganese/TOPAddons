package io.github.drmanganese.topaddons.addons;

import io.github.drmanganese.topaddons.api.TOPAddon;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import pl.asie.ucw.BlockUCWProxy;
import team.chisel.api.block.ICarvable;

@TOPAddon(dependency = "unlimitedchiselworks")
public class AddonUltimateChiselWorks extends AddonBlank {
    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (blockState.getBlock() instanceof BlockUCWProxy) {
            try {
                final Block block = ((BlockUCWProxy) blockState.getBlock()).getThroughState(blockState).getBlock();
                final ICarvable carvable = (ICarvable) block;
                final String unloc = block.getUnlocalizedName() + "." + carvable.getVariationData(data.getPickBlock().getItemDamage()).name + ".desc.1";
                probeInfo.text(TextFormatting.GRAY + IProbeInfo.STARTLOC + unloc + IProbeInfo.ENDLOC);
            } catch (RuntimeException e) {
                //
            }
        }
    }
}
