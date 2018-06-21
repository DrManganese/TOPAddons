package io.github.drmanganese.topaddons.addons.vanilla;

import io.github.drmanganese.topaddons.addons.forge.tiles.FluidCapInfo;
import io.github.drmanganese.topaddons.addons.vanilla.blocks.CocoaInfo;
import io.github.drmanganese.topaddons.addons.vanilla.entities.EntityAnimalInfo;
import io.github.drmanganese.topaddons.addons.vanilla.tiles.NoteBlockInfo;
import io.github.drmanganese.topaddons.api.IAddonBlocksAndEntities;
import io.github.drmanganese.topaddons.api.IAddonConfig;
import io.github.drmanganese.topaddons.api.IBlockInfo;
import io.github.drmanganese.topaddons.api.IEntityInfo;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.api.TOPAddon;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;

@TOPAddon(dependency = "minecraft")
public class AddonVanilla implements IAddonBlocksAndEntities, IAddonConfig {

    private static final ImmutableMap<Block, IBlockInfo> BLOCKS = ImmutableMap.of(Blocks.COCOA, new CocoaInfo());
    private static final ImmutableMap<Class<? extends TileEntity>, ITileInfo> TILES = ImmutableMap.of(TileEntityNote.class, new NoteBlockInfo());
    private static final ImmutableMap<Class<? extends Entity>, IEntityInfo> ENTITIES = ImmutableMap.of(EntityAnimal.class, new EntityAnimalInfo());

    public static boolean showPitch;

    public AddonVanilla() {
        FluidCapInfo.COLORS.put("water", 0xff345cda);
        FluidCapInfo.COLORS.put("lava", 0xffe6913c);
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

    @Override
    public void updateConfigs(Configuration config, Side side) {
        showPitch = config.getBoolean("showPitch", "Vanilla", true, "Show Note Block pitch.");
    }
}
