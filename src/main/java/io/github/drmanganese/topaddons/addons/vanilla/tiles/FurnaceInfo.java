package io.github.drmanganese.topaddons.addons.vanilla.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.IconStyle;

import static mcjty.theoneprobe.api.TextStyleClass.INFO;

public class FurnaceInfo implements ITileInfo<AbstractFurnaceBlockEntity> {

    private static final IIconStyle FIRE_STYLE =  new IconStyle().bounds(8, 8).textureBounds(8, 64);
    private static final ResourceLocation FIRE_ICON = new ResourceLocation("minecraft:textures/block/campfire_fire.png");

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, AbstractFurnaceBlockEntity tile) {
        final int burnTime = tile.dataAccess.get(0);
        final int cookTime = tile.dataAccess.get(2);
        final int cookTimeTotal = tile.dataAccess.get(3);

        if (burnTime > 0)
            probeInfo
                .horizontal()
                .icon(FIRE_ICON, 0, (int) (world.getGameTime() % 8 * 16), FIRE_STYLE.getWidth(), FIRE_STYLE.getHeight(), FIRE_STYLE)
                .text(
                    CompoundText.create()
                        .label("topaddons.vanilla:fuel")
                        .text(": ")
                        .style(INFO)
                        .text(Component.translatable("topaddons:n_ticks", burnTime))
                );

        if (cookTime > 0) {
            final String prefixWord = (tile instanceof SmokerBlockEntity) ? "topaddons.vanilla:smoking" : "topaddons.vanilla:smelting";
            probeInfo.progress(100 * cookTime / cookTimeTotal, 100, Styles.machineProgress(player, prefixWord).alternateFilledColor(0xff777777));
        }
    }
}
