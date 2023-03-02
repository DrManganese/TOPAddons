package io.github.drmanganese.topaddons.addons.thermal.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;
import io.github.drmanganese.topaddons.styles.Styles.Colors;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.registries.ForgeRegistries;

import cofh.thermal.lib.block.entity.AugmentableBlockEntity;
import com.google.common.collect.ImmutableMap;
import mcjty.theoneprobe.api.*;

import javax.annotation.Nonnull;

public class MachineTileInfo implements ITileInfo<AugmentableBlockEntity> {

    public static final MachineTileInfo INSTANCE = new MachineTileInfo();

    private static final ImmutableMap<String, Colors> MACHINE_COLORS =
        new ImmutableMap.Builder<String, Colors>()
            .put("thermal:machine_crucible", Colors.fromDye(DyeColor.ORANGE))
            .put("thermal:machine_chiller", Colors.fromDye(DyeColor.BLUE))
            .put("thermal:machine_brewer", Colors.fromDye(DyeColor.PURPLE))
            .put("thermal:machine_pyrolyzer", Colors.fromDye(DyeColor.GREEN))
            .put("thermal:machine_sawmill", Colors.fromDye(DyeColor.BROWN))
            .put("thermal:machine_insolator", Colors.fromDye(DyeColor.YELLOW))
            .put("thermal:machine_furnace", Colors.fromDye(DyeColor.RED))
            .build();

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull AugmentableBlockEntity tile) {
        if (tile.isActive) {
            final Colors color = MACHINE_COLORS.getOrDefault(ForgeRegistries.BLOCKS.getKey(blockState.getBlock()).toString(), Colors.fromDye(DyeColor.GRAY));
            final IProgressStyle progressStyle = Styles.machineProgress(player).filledColor(color.color).alternateFilledColor(color.darkerColor);
            probeInfo
                .progress(tile.getScaledProgress(100), 100, progressStyle);

            final int consumption = tile.getCurSpeed();
            if (consumption > 0)
                probeInfo
                    .text(CompoundText.createLabelInfo("{*topaddons.thermal_expansion:consumption*}: ", consumption).label(Component.literal(" RF/t")));
        }
    }
}
