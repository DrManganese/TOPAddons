package io.github.drmanganese.topaddons.addons.powah;

import io.github.drmanganese.topaddons.addons.TopAddon;
import io.github.drmanganese.topaddons.addons.powah.tiles.*;
import io.github.drmanganese.topaddons.api.*;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import owmii.powah.block.cable.CableTile;
import owmii.powah.block.ender.AbstractEnderTile;
import owmii.powah.block.ender.EnderGateTile;
import owmii.powah.block.reactor.ReactorPartTile;
import owmii.powah.lib.block.AbstractEnergyProvider;
import owmii.powah.lib.block.AbstractEnergyStorage;

import javax.annotation.Nonnull;

public class PowahAddon extends TopAddon implements IAddonBlocks, IAddonConfig, IAddonConfigProviders {

    private static final ImmutableMultimap<Class<? extends BlockEntity>, ITileInfo> TILES = ImmutableMultimap.of(
        EnderGateTile.class, new EnderGateTileInfo(),
        AbstractEnderTile.class, new EnderCellTileInfo(),
        AbstractEnergyStorage.class, EnergyStorageTileInfo.INSTANCE,
        AbstractEnergyProvider.class, EnergyProviderTileInfo.INSTANCE,
        ReactorPartTile.class, new ReactorPartTileInfo()
    );

    private static final ImmutableMap<Object, ITileConfigProvider> TILE_CONFIGS = ImmutableMap.of(
        CableTile.class, (config, player, world, blockState, data) -> config.setRFMode(0)
    );

    public static ForgeConfigSpec.BooleanValue onlyOwnerCanSeeEnderChannel;

    public PowahAddon() {
        super("powah");
    }

    @Nonnull
    @Override
    public ImmutableMultimap<Class<? extends BlockEntity>, ITileInfo> getTileInfos() {
        return TILES;
    }

    @Nonnull
    @Override
    public ImmutableMap<Object, ITileConfigProvider> getBlockConfigProviders() {
        return TILE_CONFIGS;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder, ModConfig.Type type) {
        if (type == ModConfig.Type.COMMON)
            onlyOwnerCanSeeEnderChannel = builder.comment("Ender Cell channel info is only visible to its owner.").define("onlyOwnerCanSeeEnderChannel", false);
    }
}
