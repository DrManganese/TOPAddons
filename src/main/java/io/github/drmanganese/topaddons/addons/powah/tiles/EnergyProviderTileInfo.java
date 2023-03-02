package io.github.drmanganese.topaddons.addons.powah.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.util.Formatting;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.block.solar.SolarTile;
import owmii.powah.block.thermo.ThermoTile;
import owmii.powah.lib.block.AbstractEnergyProvider;

import javax.annotation.Nonnull;

public class EnergyProviderTileInfo implements ITileInfo<AbstractEnergyProvider> {

    public static final EnergyProviderTileInfo INSTANCE = new EnergyProviderTileInfo();

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull AbstractEnergyProvider tile) {
        long generating;
        if (tile instanceof ThermoTile)
            generating = ((ThermoTile) tile).generating;
        else if (tile instanceof ReactorTile)
            generating = (long) ((ReactorTile) tile).calcProduction();
        else if (tile instanceof SolarTile)
            generating = world.isDay() ? tile.getGeneration() : 0;
        else
            generating = tile.getGeneration();

        generating = tile.getEnergy().isFull() ? 0 : generating;
        probeInfo.text(CompoundText.create().label(Component.translatable("topaddons:generating").append(": ")).info(Formatting.formatRealNumberWithUnit(generating, "FE/t", 1)));
    }
}
