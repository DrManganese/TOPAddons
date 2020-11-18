package io.github.drmanganese.topaddons.api;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;

import javax.annotation.Nonnull;

public interface ITileInfo<T> {

    /**
     * See {@link IProbeInfoProvider#addProbeInfo}.
     *
     * @param tile TileEntity the player is looking at, cast to entry given in map.
     */
    void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData, @Nonnull T tile);
}
