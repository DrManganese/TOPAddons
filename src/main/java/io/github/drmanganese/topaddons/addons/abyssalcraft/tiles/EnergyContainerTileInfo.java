package io.github.drmanganese.topaddons.addons.abyssalcraft.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import com.shinoow.abyssalcraft.api.energy.IEnergyContainer;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EnergyContainerTileInfo implements ITileInfo<IEnergyContainer> {

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, IEnergyContainer tile) {
        probeInfo.progress(Math.round(tile.getContainedEnergy()), tile.getMaxEnergy(), Styles.machineProgress(player, "Energy").suffix(" PE").filledColor(0xff0b3a26).alternateFilledColor(0xff0f3132));
    }
}
