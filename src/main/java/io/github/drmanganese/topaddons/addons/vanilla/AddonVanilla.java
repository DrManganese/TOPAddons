package io.github.drmanganese.topaddons.addons.vanilla;

import io.github.drmanganese.topaddons.addons.vanilla.entities.EntityAnimalInfo;
import io.github.drmanganese.topaddons.addons.vanilla.tiles.NoteBlockInfo;
import io.github.drmanganese.topaddons.api.*;
import io.github.drmanganese.topaddons.reference.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.passive.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;

@TOPAddon(dependency = "minecraft")
public class AddonVanilla implements IAddonBlocks, IAddonEntities {

    private static final ImmutableMap<Class<? extends TileEntity>, ITileInfo> TILES;
    private static final ImmutableMap<Class<? extends Entity>, IEntityInfo> ENTITIES;

    static {
        final ImmutableMap.Builder<Class<? extends TileEntity>, ITileInfo> tileMapBuilder = ImmutableMap.builder();
        tileMapBuilder.put(TileEntityNote.class, new NoteBlockInfo());
        TILES = tileMapBuilder.build();

        //TODO update when more dynamic way is implemented
        final IEntityInfo<EntityAnimal> animalInfo = new EntityAnimalInfo();
        final ImmutableMap.Builder<Class<? extends Entity>, IEntityInfo> entityMapBuilder = ImmutableMap.builder();
        entityMapBuilder.put(EntityChicken.class, animalInfo);
        entityMapBuilder.put(EntityCow.class, animalInfo);
        entityMapBuilder.put(EntityHorse.class, animalInfo);
        entityMapBuilder.put(EntityPig.class, animalInfo);
        entityMapBuilder.put(EntityPolarBear.class, animalInfo);
        entityMapBuilder.put(EntityRabbit.class, animalInfo);
        entityMapBuilder.put(EntitySheep.class, animalInfo);
        ENTITIES = entityMapBuilder.build();
    }

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
