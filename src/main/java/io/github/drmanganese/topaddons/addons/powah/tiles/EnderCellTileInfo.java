package io.github.drmanganese.topaddons.addons.powah.tiles;

import io.github.drmanganese.topaddons.addons.powah.PowahAddon;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import owmii.powah.block.ender.AbstractEnderTile;

import javax.annotation.Nonnull;
import java.util.Objects;

public class EnderCellTileInfo implements ITileInfo<AbstractEnderTile> {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull AbstractEnderTile tile) {
        if (Objects.equals(tile.getOwner(), player.getGameProfile()) || !PowahAddon.onlyOwnerCanSeeEnderChannel.get())
            InfoHelper.textPrefixed(probeInfo, "Channel", String.valueOf(tile.getChannel().get() + 1));
    }
}
