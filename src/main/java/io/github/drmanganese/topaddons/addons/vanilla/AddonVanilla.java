package io.github.drmanganese.topaddons.addons.vanilla;

import io.github.drmanganese.topaddons.addons.forge.tiles.FluidCapInfo;
import io.github.drmanganese.topaddons.addons.vanilla.blocks.CocoaInfo;
import io.github.drmanganese.topaddons.addons.vanilla.entities.EntityAnimalInfo;
import io.github.drmanganese.topaddons.addons.vanilla.tiles.NoteBlockInfo;
import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.IAddonEntities;
import io.github.drmanganese.topaddons.api.IBlockInfo;
import io.github.drmanganese.topaddons.api.IEntityInfo;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.reference.Reference;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;

@TOPAddon(dependency = "minecraft")
public class AddonVanilla implements IAddonBlocks, IAddonEntities {

    private static final ImmutableMap<Block, IBlockInfo> BLOCKS = ImmutableMap.of(Blocks.COCOA, new CocoaInfo());
    private static final ImmutableMap<Class<? extends TileEntity>, ITileInfo> TILES = ImmutableMap.of(TileEntityNote.class, new NoteBlockInfo());
    private static final ImmutableMap<Class<? extends Entity>, IEntityInfo> ENTITIES = ImmutableMap.of(EntityAnimal.class, new EntityAnimalInfo());

    public AddonVanilla() {
        FluidCapInfo.COLORS.put("water", 0xff345cda);
        FluidCapInfo.COLORS.put("lava", 0xffe6913c);
    }

    @Override
    public String getID() {
        return Reference.MOD_ID + ":vanilla";
    }

    @Nonnull
    @Override
    public ImmutableMap<Block, IBlockInfo> getBlocks() {
        return BLOCKS;
    }

    @Override
    @Nonnull
    public ImmutableMap<Class<? extends TileEntity>, ITileInfo> getTiles() {
        return TILES;
    }

    @Nonnull
    @Override
    public ImmutableMap<Class<? extends Entity>, IEntityInfo> getEntities() {
        return ENTITIES;
    }
}
