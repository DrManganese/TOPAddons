package io.github.drmanganese.topaddons.addons.bloodmagic.tiles;

import io.github.drmanganese.topaddons.addons.bloodmagic.BloodMagicAddon;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import wayoftime.bloodmagic.common.tile.TileIncenseAltar;

import javax.annotation.Nonnull;

public class TileIncenseAltarInfo implements ITileInfo<TileIncenseAltar> {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull TileIncenseAltar tile) {
        if (BloodMagicAddon.altarsRequireSigil.get() && !BloodMagicAddon.isHoldingSigil(player)) return;
        final int tranquility = (int) (100 * tile.tranquility);
        InfoHelper.textPrefixed(probeInfo, "{*topaddons.bloodmagic:tranquility*}", Integer.toString(tranquility));
        InfoHelper.textPrefixed(probeInfo, "{*topaddons.bloodmagic:bonus*}", (int) (tile.incenseAddition * 100) + "%");
    }
}