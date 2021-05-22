package io.github.drmanganese.topaddons.addons.vanilla.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.SmokerTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.IconStyle;

import javax.annotation.Nonnull;

import static mcjty.theoneprobe.api.TextStyleClass.INFO;

public class FurnaceInfo implements ITileInfo<AbstractFurnaceTileEntity> {

    private static final IIconStyle FIRE_STYLE =  new IconStyle().bounds(8, 8).textureBounds(8, 64);
    private static final ResourceLocation FIRE_ICON = new ResourceLocation("minecraft:textures/block/campfire_fire.png");

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData, @Nonnull AbstractFurnaceTileEntity tile) {
        final int burnTime = tile.furnaceData.get(0);
        final int cookTime = tile.furnaceData.get(2);
        final int cookTimeTotal = tile.furnaceData.get(3);

        if (burnTime > 0)
            probeInfo
                .horizontal()
                .icon(FIRE_ICON, 0, (int) (world.getGameTime() % 8 * 16), FIRE_STYLE.getWidth(), FIRE_STYLE.getHeight(), FIRE_STYLE)
                .text(
                    CompoundText.create()
                        .label("topaddons.vanilla:fuel")
                        .text(": ")
                        .style(INFO)
                        .text(new TranslationTextComponent("topaddons:n_ticks", burnTime))
                );

        if (cookTime > 0) {
            final String prefixWord = (tile instanceof SmokerTileEntity) ? "topaddons.vanilla:smoking" : "topaddons.vanilla:smelting";
            probeInfo.progress(100 * cookTime / cookTimeTotal, 100, Styles.machineProgress(player, prefixWord).alternateFilledColor(0xff777777));
        }
    }
}
