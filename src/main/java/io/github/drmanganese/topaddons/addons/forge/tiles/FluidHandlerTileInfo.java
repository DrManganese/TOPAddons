package io.github.drmanganese.topaddons.addons.forge.tiles;

import io.github.drmanganese.topaddons.addons.forge.ForgeAddon;
import io.github.drmanganese.topaddons.addons.forge.ForgeAddon.FluidGaugeChoice;
import io.github.drmanganese.topaddons.addons.forge.elements.FluidGaugeElement;
import io.github.drmanganese.topaddons.api.ITileConfigProvider;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.config.Config;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.Color;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static mcjty.theoneprobe.config.Config.*;

public class FluidHandlerTileInfo implements ITileInfo<BlockEntity>, ITileConfigProvider {

    public static final FluidHandlerTileInfo INSTANCE = new FluidHandlerTileInfo();
    public static final Map<String, String> CUSTOM_TANK_KEYS = new HashMap<>();

    static {
        CUSTOM_TANK_KEYS.put("thermal:machine_refinery", "topaddons.thermal:fractioning_still");
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull BlockEntity tile) {
        final int tankMode = mcjty.theoneprobe.config.Config.getDefaultConfig().getTankMode();
        getTileFluidHandler(blockState, tile)
            .ifPresent(fluidHandler -> {
                final IntFunction<FluidTank> tankMaker = i -> new FluidTank(fluidHandler, blockState.getBlock(), i, player);
                IntStream.range(0, fluidHandler.getTanks())
                    .mapToObj(tankMaker)
                    .filter(FluidTank::isValidTank)
                    .forEachOrdered(tank -> {
                        if (FluidGaugeChoice.getSyncedValueFor(player).hideTopAddonsGauge)
                            addCompactVanillaGauge(probeInfo, probeMode, player, tank, tankMode);
                        else
                            addTopAddonsGauge(probeMode, probeInfo, player, tank);
                    });
            });
    }

    @Override
    public void getProbeConfig(IProbeConfig config, Player player, Level world, BlockState blockState, IProbeHitData data) {
        if (FluidGaugeChoice.getSyncedValueFor(player).hideOriginal)
            config.showTankSetting(IProbeConfig.ConfigMode.NOT);
    }

    private static boolean shouldAddCompactVanillaGauge(ProbeMode probeMode, Player player, int tankMode) {
        return probeMode == ProbeMode.NORMAL &&
            Config.getSyncedBoolean(player, ForgeAddon.gaugeShowCompactTop) &&
            (tankMode == 1 || tankMode == 2);
    }

    private static void addTopAddonsGauge(ProbeMode probeMode, IProbeInfo probeInfo, Player player, FluidTank tank) {
        if (tank.stack.getAmount() < 1 && Config.getSyncedBoolean(player, ForgeAddon.gaugeHideEmptyTanks)) return;
        probeInfo.element(tank.makeElement(probeMode));
    }

    private static void addCompactVanillaGauge(IProbeInfo probeInfo, ProbeMode probeMode, Player player, FluidTank tank, int tankMode) {
        if (!shouldAddCompactVanillaGauge(probeMode, player, tankMode) || tank.stack.isEmpty()) return;
        final Color color;
        if (Objects.equals(tank.stack.getFluid(), Fluids.LAVA)) {
            color = new Color(255, 139, 27);
        } else {
            color = new Color(IClientFluidTypeExtensions.of(tank.stack.getFluid()).getTintColor(tank.stack));
        }
        if (tankMode == 1)
            probeInfo.tankSimple(
                (int) tank.capacity,
                tank.stack,
                probeInfo.defaultProgressStyle()
                    .height(6)
                    .showText(false)
                    .borderlessColor(color, color.darker(0.47))
            );
        else
            probeInfo.progress(
                tank.stack.getAmount(),
                tank.capacity,
                probeInfo.defaultProgressStyle()
                    .filledColor(tankbarFilledColor)
                    .alternateFilledColor(tankbarAlternateFilledColor)
                    .borderColor(tankbarBorderColor)
                    .showText(false)
                    .height(6)
            );
    }

    private LazyOptional<IFluidHandler> getTileFluidHandler(BlockState blockState, BlockEntity tile) {
        final Block block = blockState.getBlock();
        if (!ForgeAddon.gaugeModBlacklist.get().contains(ForgeRegistries.BLOCKS.getKey(block).getNamespace()))
            return tile.getCapability(ForgeCapabilities.FLUID_HANDLER);
        else
            return LazyOptional.empty();
    }

    private static class FluidTank {

        private final FluidStack stack;
        private final long capacity;
        private final String translationKey;

        private FluidTank(IFluidHandler handler, Block block, int tankIndex, Player player) {
            this.stack = handler.getFluidInTank(tankIndex);
            this.capacity = handler.getTankCapacity(tankIndex);
            this.translationKey = getTranslationKey(ForgeRegistries.BLOCKS.getKey(block).toString(), tankIndex, player);
        }

        private FluidGaugeElement makeElement(ProbeMode mode) {
            return new FluidGaugeElement(
                mode == ProbeMode.EXTENDED,
                stack.getAmount(),
                capacity,
                translationKey,
                stack
            );
        }

        private String getTranslationKey(String registryName, int tankIndex, Player player) {
            if (CUSTOM_TANK_KEYS.containsKey(registryName) && Config.getSyncedBoolean(player, ForgeAddon.gaugeUseCustomTankNames)) {
                return String.format("%s_%d", CUSTOM_TANK_KEYS.get(registryName), tankIndex);
            } else {
                return "topaddons.forge:default_tank_name";
            }
        }

        private Boolean isValidTank() {
            return capacity > 0;
        }
    }
}
