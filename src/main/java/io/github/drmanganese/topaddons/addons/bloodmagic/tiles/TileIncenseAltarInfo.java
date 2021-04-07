package io.github.drmanganese.topaddons.addons.bloodmagic.tiles;

import io.github.drmanganese.topaddons.addons.bloodmagic.BloodMagicAddon;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import wayoftime.bloodmagic.tile.TileIncenseAltar;

import javax.annotation.Nonnull;

public class TileIncenseAltarInfo implements ITileInfo<TileIncenseAltar> {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData, @Nonnull TileIncenseAltar tile) {
        if (BloodMagicAddon.altarsRequireSigil.get() && !BloodMagicAddon.isHoldingSigil(player)) return;
        final int tranquility = (int) (100 * tile.tranquility);
        InfoHelper.textPrefixed(probeInfo, "{*topaddons.bloodmagic:tranquility*}", Integer.toString(tranquility));
        InfoHelper.textPrefixed(probeInfo, "{*topaddons.bloodmagic:bonus*}", (int) (tile.incenseAddition * 100) + "%");
    }
}
