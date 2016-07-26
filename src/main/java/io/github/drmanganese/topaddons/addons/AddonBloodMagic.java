package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import io.github.drmanganese.topaddons.api.TOPAddon;

import WayofTime.bloodmagic.api.altar.IBloodAltar;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

@TOPAddon(dependency = "BloodMagic")
public class AddonBloodMagic extends AddonBlank {

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile != null) {
            if (tile instanceof IBloodAltar) {
                IBloodAltar altar = (IBloodAltar) tile;
                textPrefixed(probeInfo, "Tier", altar.getTier().toInt() + "", TextFormatting.RED);
                AddonForge.addTankElement(probeInfo, "Blood Altar", "Life Essence", altar.getCurrentBlood(), altar.getCapacity(), BlockLifeEssence.getLifeEssence().getColor(), mode);
            }
        }
    }
}
