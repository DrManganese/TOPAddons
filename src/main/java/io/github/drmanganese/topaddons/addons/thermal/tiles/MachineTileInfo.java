package io.github.drmanganese.topaddons.addons.thermal.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import cofh.thermal.lib.tileentity.ThermalTileAugmentable;
import com.google.common.collect.ImmutableMap;
import mcjty.theoneprobe.api.*;

import javax.annotation.Nonnull;

public class MachineTileInfo implements ITileInfo<ThermalTileAugmentable> {

    public static final MachineTileInfo INSTANCE = new MachineTileInfo();

    private static final ImmutableMap<String, Styles.Colors> MACHINE_COLORS =
        new ImmutableMap.Builder<String, Styles.Colors>()
            .put("thermal:machine_crucible", Styles.Colors.fromDye(DyeColor.ORANGE))
            .put("thermal:machine_chiller", Styles.Colors.fromDye(DyeColor.BLUE))
            .put("thermal:machine_brewer", Styles.Colors.fromDye(DyeColor.PURPLE))
            .put("thermal:machine_pyrolyzer", Styles.Colors.fromDye(DyeColor.GREEN))
            .put("thermal:machine_sawmill", Styles.Colors.fromDye(DyeColor.BROWN))
            .put("thermal:machine_insolator", Styles.Colors.fromDye(DyeColor.YELLOW))
            .put("thermal:machine_furnace", Styles.Colors.fromDye(DyeColor.RED))
            .build();

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull ThermalTileAugmentable tile) {
        if (tile.isActive) {
            final Styles.Colors color = MACHINE_COLORS.getOrDefault(blockState.getBlock().getRegistryName().toString(), Styles.Colors.fromDye(DyeColor.GRAY));
            final IProgressStyle progressStyle = Styles.machineProgress(player).filledColor(color.dyeColor).alternateFilledColor(color.darkerColor);
            probeInfo
                .progress(tile.getScaledProgress(100), 100, progressStyle);

            final int consumption = tile.getCurSpeed();
            if (consumption > 0)
                probeInfo
                    .text(CompoundText.createLabelInfo("{*topaddons.thermal_expansion:consumption*}: ", consumption).label(new TextComponent(" RF/t")));
        }
    }
}
