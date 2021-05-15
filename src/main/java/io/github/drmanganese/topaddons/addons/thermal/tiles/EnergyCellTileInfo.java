package io.github.drmanganese.topaddons.addons.thermal.tiles;

import io.github.drmanganese.topaddons.addons.thermal.ThermalExpansionAddon;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.config.Config;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import cofh.thermal.core.tileentity.storage.EnergyCellTile;
import mcjty.theoneprobe.api.*;

import javax.annotation.Nonnull;

public class EnergyCellTileInfo implements ITileInfo<EnergyCellTile> {

    private static final ILayoutStyle ICON_LAYOUT_STYLE = Styles.CENTERED.copy().spacing(1);
    private static final IIconStyle ICON_STYLE = IIconStyle.createBounds(10, 10).textureBounds(16, 16);
    private static final ResourceLocation INPUT_ICON = new ResourceLocation("cofh_core:textures/gui/icons/icon_input.png");
    private static final ResourceLocation OUTPUT_ICON = new ResourceLocation("cofh_core:textures/gui/icons/icon_output.png");


    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData, @Nonnull EnergyCellTile tile) {
        if (probeMode == ProbeMode.EXTENDED || Config.getSyncedBoolean(player, ThermalExpansionAddon.alwaysShowCellIo)) {
            ioIcon(probeInfo, tile.amountInput, INPUT_ICON);
            ioIcon(probeInfo, tile.amountOutput, OUTPUT_ICON);
        }
    }

    private static void ioIcon(IProbeInfo probeInfo, int amount, ResourceLocation icon) {
        probeInfo.horizontal(ICON_LAYOUT_STYLE)
            .icon(icon, 3, 3, 13, 13, ICON_STYLE)
            .text(CompoundText.create().label(new StringTextComponent(String.format("%d RF/t", amount))));
    }
}
