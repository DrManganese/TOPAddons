package io.github.drmanganese.topaddons.addons.thermal.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import cofh.core.util.control.IRedstoneControllableTile;
import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

import javax.annotation.Nonnull;

public class RedstoneControlTileInfo implements ITileInfo<IRedstoneControllableTile> {
    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull IRedstoneControllableTile tile) {
        if (!tile.redstoneControl().getState())
            probeInfo.text(CompoundText.create().error("topaddons:disabled"));
    }
}
