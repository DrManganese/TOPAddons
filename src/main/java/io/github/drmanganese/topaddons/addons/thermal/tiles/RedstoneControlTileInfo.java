package io.github.drmanganese.topaddons.addons.thermal.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import cofh.core.util.control.IRedstoneControllableTile;
import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

import javax.annotation.Nonnull;

public class RedstoneControlTileInfo implements ITileInfo<IRedstoneControllableTile> {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData, @Nonnull IRedstoneControllableTile tile) {
        if (!tile.redstoneControl().getState())
            probeInfo.text(CompoundText.create().error("topaddons:disabled"));
    }
}
