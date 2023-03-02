package io.github.drmanganese.topaddons.addons.powah.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.util.Formatting;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.CapabilityEnergy;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import owmii.powah.block.solar.SolarTile;
import owmii.powah.lib.block.AbstractEnergyProvider;
import owmii.powah.lib.block.AbstractEnergyStorage;

import javax.annotation.Nonnull;

public class EnergyStorageTileInfo implements ITileInfo<AbstractEnergyStorage> {

    public static final EnergyStorageTileInfo INSTANCE = new EnergyStorageTileInfo();

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull AbstractEnergyStorage tile) {
        if (tile instanceof SolarTile)
            tile.getCapability(ForgeCapabilities.ENERGY, Direction.DOWN).ifPresent(InfoHelper.energyBar(probeInfo));

        final long maxReceive = tile.getEnergy().getMaxReceive();
        if (maxReceive > 0 && (!(tile instanceof AbstractEnergyProvider) || probeMode == ProbeMode.EXTENDED))
            InfoHelper.textPrefixed(probeInfo, "Transfer rate", Formatting.formatRealNumberWithUnit(tile.getEnergy().getMaxReceive(), "FE/t"));
    }
}
