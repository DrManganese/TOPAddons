package io.github.drmanganese.topaddons.addons.thermal.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import cofh.thermal.lib.block.entity.DynamoBlockEntity;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

import javax.annotation.Nonnull;

public class DynamoTileInfo implements ITileInfo<DynamoBlockEntity> {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, net.minecraft.world.level.Level world, BlockState blockState, IProbeHitData hitData, @Nonnull DynamoBlockEntity tile) {
        if (tile.isActive)
            InfoHelper.textPrefixed(probeInfo, "{*topaddons:generating*}", String.format("%d RF/t", tile.getCurSpeed()));
    }
}
