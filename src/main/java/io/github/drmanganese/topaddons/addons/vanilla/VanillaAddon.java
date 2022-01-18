package io.github.drmanganese.topaddons.addons.vanilla;

import io.github.drmanganese.topaddons.addons.TopAddon;
import io.github.drmanganese.topaddons.addons.vanilla.blocks.BeehiveInfo;
import io.github.drmanganese.topaddons.addons.vanilla.blocks.ComposterInfo;
import io.github.drmanganese.topaddons.addons.vanilla.entities.BreedCoolDownInfo;
import io.github.drmanganese.topaddons.addons.vanilla.tiles.BeaconInfo;
import io.github.drmanganese.topaddons.addons.vanilla.tiles.FurnaceInfo;
import io.github.drmanganese.topaddons.api.*;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

import javax.annotation.Nonnull;

public class VanillaAddon extends TopAddon implements IAddonBlocks, IAddonEntities {

    private static final ImmutableMap<Block, IBlockInfo> BLOCKS = ImmutableMap.of(
        Blocks.COMPOSTER, new ComposterInfo(),
        Blocks.BEE_NEST, BeehiveInfo.INSTANCE,
        Blocks.BEEHIVE, BeehiveInfo.INSTANCE
    );
    private static final ImmutableMultimap<Class<? extends BlockEntity>, ITileInfo> TILES = ImmutableMultimap.of(
        AbstractFurnaceBlockEntity.class, new FurnaceInfo(),
        BeaconBlockEntity.class, new BeaconInfo(),
        BeehiveBlockEntity.class, BeehiveInfo.INSTANCE
    );
    private static final ImmutableMap<Class<? extends Entity>, IEntityInfo> ENTITIES = ImmutableMap.of(Animal.class, new BreedCoolDownInfo());

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
    public ImmutableMultimap<Class<? extends BlockEntity>, ITileInfo> getTileInfos() {
        return TILES;
    }

    @Nonnull
    @Override
    public ImmutableMap<Block, IBlockInfo> getBlockInfos() {
        return BLOCKS;
    }
}
