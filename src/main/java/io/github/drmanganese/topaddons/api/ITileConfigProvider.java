package io.github.drmanganese.topaddons.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeHitData;

public interface ITileConfigProvider {

    void getProbeConfig(IProbeConfig config, Player player, Level world, BlockState blockState, IProbeHitData data);
}
