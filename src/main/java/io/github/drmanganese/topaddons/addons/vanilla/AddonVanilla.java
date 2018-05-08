package io.github.drmanganese.topaddons.addons.vanilla;

import io.github.drmanganese.topaddons.addons.vanilla.entities.EntityAnimalInfo;
import io.github.drmanganese.topaddons.addons.vanilla.tiles.NoteBlockInfo;
import io.github.drmanganese.topaddons.api.*;
import io.github.drmanganese.topaddons.reference.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;

@TOPAddon(dependency = "minecraft")
public class AddonVanilla implements IAddonBlocks, IAddonEntities {

    private static final ImmutableMap<Class<? extends TileEntity>, ITileInfo> TILES = ImmutableMap.of(TileEntityNote.class, new NoteBlockInfo());
    private static final ImmutableMap<Class<? extends Entity>, IEntityInfo> ENTITIES = ImmutableMap.of(EntityAnimal.class, new EntityAnimalInfo());

    @Override
    public String getID() {
        return Reference.MOD_ID + ":vanilla";
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
