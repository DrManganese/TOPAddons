package io.github.drmanganese.topaddons.addons.thermal.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import cofh.thermal.core.tileentity.MachineTileProcess;
import com.google.common.collect.ImmutableMap;
import mcjty.theoneprobe.api.*;

import javax.annotation.Nonnull;

public class MachineTileInfo implements ITileInfo<MachineTileProcess> {

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
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData, @Nonnull MachineTileProcess tile) {
        if (tile.isActive) {
            final Styles.Colors color = MACHINE_COLORS.getOrDefault(blockState.getBlock().getRegistryName().toString(), Styles.Colors.fromDye(DyeColor.GRAY));
            final IProgressStyle progressStyle = Styles.machineProgress(player, "Progress").filledColor(color.dyeColor).alternateFilledColor(color.darkerColor);
            probeInfo
                .progress(tile.getScaledProgress(100), 100, progressStyle)
                .text(CompoundText.createLabelInfo("{*topaddons.thermal_expansion:consumption*}: ", tile.getCurSpeed()).label(new StringTextComponent(" RF/t")));
        }
    }
}
