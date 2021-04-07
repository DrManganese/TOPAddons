package io.github.drmanganese.topaddons.addons.thermal.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import cofh.thermal.lib.tileentity.DynamoTileBase;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

import javax.annotation.Nonnull;

public class DynamoTileInfo implements ITileInfo<DynamoTileBase> {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData, @Nonnull DynamoTileBase tile) {
        if (tile.isActive)
            InfoHelper.textPrefixed(probeInfo, "{*topaddons:generating*}", String.format("%d RF/t", tile.getCurSpeed()));
    }
}
