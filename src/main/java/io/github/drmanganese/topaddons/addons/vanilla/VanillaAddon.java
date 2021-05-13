package io.github.drmanganese.topaddons.addons.vanilla;

import io.github.drmanganese.topaddons.addons.TopAddon;
import io.github.drmanganese.topaddons.addons.vanilla.blocks.BeehiveInfo;
import io.github.drmanganese.topaddons.addons.vanilla.blocks.ComposterInfo;
import io.github.drmanganese.topaddons.addons.vanilla.entities.BreedCoolDownInfo;
import io.github.drmanganese.topaddons.addons.vanilla.tiles.BeaconInfo;
import io.github.drmanganese.topaddons.addons.vanilla.tiles.FurnaceInfo;
import io.github.drmanganese.topaddons.api.*;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.BeehiveTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

import javax.annotation.Nonnull;

public class VanillaAddon extends TopAddon implements IAddonBlocks, IAddonEntities {

    private static final ImmutableMap<Block, IBlockInfo> BLOCKS = ImmutableMap.of(
        Blocks.COMPOSTER, new ComposterInfo(),
        Blocks.BEE_NEST, BeehiveInfo.INSTANCE,
        Blocks.BEEHIVE, BeehiveInfo.INSTANCE
    );
    private static final ImmutableMap<Class<? extends Entity>, IEntityInfo> ENTITIES = ImmutableMap.of(AnimalEntity.class, new BreedCoolDownInfo());
    private static final ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> TILES = ImmutableMultimap.of(
        AbstractFurnaceTileEntity.class, new FurnaceInfo(),
        BeaconTileEntity.class, new BeaconInfo(),
        BeehiveTileEntity.class, BeehiveInfo.INSTANCE
    );

    public VanillaAddon() {
        super("vanilla");
    }

    @Nonnull
    @Override
    public ImmutableMap<Class<? extends Entity>, IEntityInfo> getEntityInfos() {
        return ENTITIES;
    }

    @Nonnull
    @Override
    public ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> getTileInfos() {
        return TILES;
    }

    @Nonnull
    @Override
    public ImmutableMap<Block, IBlockInfo> getBlockInfos() {
        return BLOCKS;
    }
}
