package io.github.drmanganese.topaddons.addons.forge.tiles;

import io.github.drmanganese.topaddons.addons.forge.ForgeAddon;
import io.github.drmanganese.topaddons.addons.forge.ForgeAddon.FluidGaugeChoice;
import io.github.drmanganese.topaddons.api.ITileConfigProvider;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.capabilities.ElementSync;
import io.github.drmanganese.topaddons.config.Config;
import io.github.drmanganese.topaddons.elements.forge.FluidGaugeElement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class FluidHandlerTileInfo implements ITileInfo<TileEntity>, ITileConfigProvider {

    public static final FluidHandlerTileInfo INSTANCE = new FluidHandlerTileInfo();
    public static final Map<String, String> CUSTOM_TANK_KEYS = new HashMap<>();

    static {
        CUSTOM_TANK_KEYS.put("thermal:machine_refinery", "topaddons.thermal:fractioning_still");
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData, @Nonnull TileEntity tile) {
        if (FluidGaugeChoice.getSyncedValueFor(player).hideTopAddonsGauge) return;
        final Block block = blockState.getBlock();
        if (!ForgeAddon.gaugeModBlacklist.get().contains(ForgeRegistries.BLOCKS.getKey(block).getNamespace())) {
            tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(fluidHandler -> {
                final IntFunction<FluidTank> tankMaker = i -> new FluidTank(fluidHandler, block, i, player);
                IntStream.range(0, fluidHandler.getTanks())
                    .mapToObj(tankMaker)
                    .filter(FluidTank::isValidTank)
                    .map(FluidTank::elementMaker)
                    .forEachOrdered(maker -> probeInfo.element(maker.apply(player, probeMode)));
            });
        }
    }

    @Override
    public void getProbeConfig(IProbeConfig config, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        if (FluidGaugeChoice.getSyncedValueFor(player).hideOriginal)
            config.showTankSetting(IProbeConfig.ConfigMode.NOT);
    }

    private static class FluidTank {

        private final FluidStack stack;
        private final int capacity;
        private final String translationKey;
        private final int elementId;

        private FluidTank(IFluidHandler handler, Block block, int tankIndex, PlayerEntity player) {
            this.stack = handler.getFluidInTank(tankIndex);
            this.capacity = handler.getTankCapacity(tankIndex);
            this.translationKey = getTranslationKey(block.getRegistryName().toString(), tankIndex, player);
            this.elementId = ElementSync.getId(ForgeAddon.GAUGE_ELEMENT_ID, player);
        }

        private BiFunction<PlayerEntity, ProbeMode, FluidGaugeElement> elementMaker() {
            return (player, mode) -> new FluidGaugeElement(
                elementId,
                mode == ProbeMode.EXTENDED,
                stack.getAmount(),
                capacity,
                translationKey,
                stack.getFluid()
            );
        }

        private String getTranslationKey(String registryName, int tankIndex, PlayerEntity player) {
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
