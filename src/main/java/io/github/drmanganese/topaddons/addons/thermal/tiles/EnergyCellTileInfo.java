package io.github.drmanganese.topaddons.addons.thermal.tiles;

import io.github.drmanganese.topaddons.addons.thermal.ThermalExpansionAddon;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.config.Config;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import cofh.thermal.core.block.entity.storage.EnergyCellBlockEntity;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.IconStyle;

import javax.annotation.Nonnull;

public class EnergyCellTileInfo implements ITileInfo<EnergyCellBlockEntity> {

    private static final ILayoutStyle ICON_LAYOUT_STYLE = Styles.CENTERED.copy().spacing(1);
    private static final IIconStyle ICON_STYLE = new IconStyle().bounds(10, 10).textureBounds(16, 16);
    private static final ResourceLocation INPUT_ICON = new ResourceLocation("cofh_core:textures/gui/icons/icon_input.png");
    private static final ResourceLocation OUTPUT_ICON = new ResourceLocation("cofh_core:textures/gui/icons/icon_output.png");

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull EnergyCellBlockEntity tile) {
        if (probeMode == ProbeMode.EXTENDED || Config.getSyncedBoolean(player, ThermalExpansionAddon.alwaysShowCellIo)) {
            ioIcon(probeInfo, tile.amountInput, INPUT_ICON);
            ioIcon(probeInfo, tile.amountOutput, OUTPUT_ICON);
        }
    }

    private static void ioIcon(IProbeInfo probeInfo, int amount, ResourceLocation icon) {
        probeInfo.horizontal(ICON_LAYOUT_STYLE)
            .icon(icon, 3, 3, 13, 13, ICON_STYLE)
            .text(CompoundText.create().label(Component.literal(String.format("%d RF/t", amount))));
    }
}
