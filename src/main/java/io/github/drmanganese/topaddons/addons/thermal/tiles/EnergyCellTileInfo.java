package io.github.drmanganese.topaddons.addons.thermal.tiles;

import io.github.drmanganese.topaddons.addons.thermal.ThermalExpansionAddon;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.config.Config;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import cofh.thermal.core.tileentity.storage.EnergyCellTile;
import mcjty.theoneprobe.api.*;

import javax.annotation.Nonnull;

public class EnergyCellTileInfo implements ITileInfo<EnergyCellTile> {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData, @Nonnull EnergyCellTile tile) {
        if (probeMode == ProbeMode.EXTENDED || Config.getSyncedBoolean(player, ThermalExpansionAddon.alwaysShowCellIo)) {
            final IIconStyle iconStyle = probeInfo.defaultIconStyle().textureHeight(16).textureWidth(16).width(10).height(10);

            probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER).spacing(1))
                .icon(new ResourceLocation("cofh_core:textures/gui/icons/icon_input.png"), 3, 3, 13, 13, iconStyle)
                .text(CompoundText.create().label(new StringTextComponent(String.format("%d RF/t", tile.amountInput))));

            probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER).spacing(1))
                .icon(new ResourceLocation("cofh_core:textures/gui/icons/icon_output.png"), 3, 3, 13, 13, iconStyle)
                .text(CompoundText.create().label(new StringTextComponent(String.format("%d RF/t", tile.amountOutput))));
        }
    }
}
