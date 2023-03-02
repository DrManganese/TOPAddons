package io.github.drmanganese.topaddons.addons.powah.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import owmii.powah.block.ender.EnderGateTile;

import javax.annotation.Nonnull;

public class EnderGateTileInfo implements ITileInfo<EnderGateTile> {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull EnderGateTile tile) {
        tile.getCapability(ForgeCapabilities.ENERGY, blockState.getValue(BlockStateProperties.FACING))
            .ifPresent(InfoHelper.energyBar(probeInfo));
    }
}
