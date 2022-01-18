package io.github.drmanganese.topaddons.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;

import javax.annotation.Nonnull;

public interface ITileInfo<T> {

    /**
     * See {@link IProbeInfoProvider#addProbeInfo}.
     *
     * @param tile BlockEntity the player is looking at, cast to entry given in map.
     */
    void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull T tile);
}
