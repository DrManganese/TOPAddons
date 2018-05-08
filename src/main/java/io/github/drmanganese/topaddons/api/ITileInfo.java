package io.github.drmanganese.topaddons.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;

public interface ITileInfo<T extends TileEntity> {

    /**
     * See {@link IProbeInfoProvider#addProbeInfo}.
     * @param tile TileEntity the player is looking at, cast to entry given in map.
     */
    void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, T tile);
}
