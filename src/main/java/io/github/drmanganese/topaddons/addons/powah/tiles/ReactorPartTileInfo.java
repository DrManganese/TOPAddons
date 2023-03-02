package io.github.drmanganese.topaddons.addons.powah.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import com.google.common.collect.ImmutableList;
import mcjty.theoneprobe.api.*;
import owmii.powah.block.reactor.ReactorPartTile;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.lib.util.Ticker;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class ReactorPartTileInfo implements ITileInfo<ReactorPartTile> {

    // @formatter:off
    private static final ImmutableList<TickerProgress> TICKERS = ImmutableList.of(
        new TickerProgress(tile -> tile.fuel,         "Fuel",         Styles.Colors.fromDye(DyeColor.GREEN)),
        new TickerProgress(tile -> tile.carbon,       "Carbon",   new Styles.Colors(MaterialColor.TERRACOTTA_GRAY)),
        new TickerProgress(tile -> tile.redstone,     "Redstone", new Styles.Colors(0xffb50000)),
        new TickerProgress(tile -> tile.solidCoolant, "Coolant",  new Styles.Colors(MaterialColor.DIAMOND))
    );
    // @formatter:on

    private static final ResourceLocation REACT_TEX = new ResourceLocation("powah:textures/gui/container/reactor.png");
    private static final CompoundText AUTO_MODE_TEXT = CompoundText.create()
        .label(Component.translatable("topaddons.powah:mode").append(": "))
        .ok("topaddons.powah:auto");
    
    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull ReactorPartTile tile) {
        if (tile.isBuilt()) tile.core().ifPresent(core -> {
            if (!tile.isExtractor())
                core.getCapability(ForgeCapabilities.ENERGY).ifPresent(InfoHelper.energyBar(probeInfo));

            tickerBars(probeMode, probeInfo, player, core);

            // temp
            probeInfo
                .horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_BOTTOMRIGHT).spacing(6))
                .icon(REACT_TEX, 205, 8, 4, 10, probeInfo.defaultIconStyle().bounds(4, 10))
                .text(String.format("%.1f\u00b0C", core.temp.getTicks()));

            EnergyProviderTileInfo.INSTANCE.addProbeInfo(probeMode, probeInfo, player, world, blockState, hitData, core);
            EnergyStorageTileInfo.INSTANCE.addProbeInfo(probeMode, probeInfo, player, world, blockState, hitData, core);
            if (core.isGenModeOn())
                probeInfo.text(AUTO_MODE_TEXT);
        });
    }

    private static void tickerBars(ProbeMode probeMode, IProbeInfo probeInfo, Player player, ReactorTile core) {
        final IProbeInfo vert = probeInfo.vertical(probeInfo.defaultLayoutStyle().spacing(1));
        final boolean extended = probeMode == ProbeMode.EXTENDED;
        final IProgressStyle progressStyle = Styles.machineProgress(player)
            .height(extended ? 12 : 6)
            .showText(extended)
            .suffix("");
        TICKERS.forEach(ticker -> ticker.progress(core, vert, progressStyle));
    }

    private record TickerProgress(Function<ReactorTile, Ticker> tickerGetter, String name, Styles.Colors color) {
        private void progress(ReactorTile tile, IProbeInfo probeInfo, IProgressStyle style) {
            final Ticker ticker = tickerGetter.apply(tile);
            probeInfo.progress(
                (int) ticker.getTicks(),
                (int) ticker.getMax(),
                style.copy()
                    .filledColor(color.color)
                    .alternateFilledColor(color.darkerColor)
                    .prefix(name + ": ")
                    .backgroundColor(color.semiTransparentColor)
            );
        }
    }
}
