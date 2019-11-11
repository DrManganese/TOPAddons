package io.github.drmanganese.topaddons.addons.forestry;

import forestry.core.blocks.BlockBogEarth;
import forestry.core.blocks.BlockRegistryCore;
import forestry.cultivation.tiles.TilePlanter;
import io.github.drmanganese.topaddons.addons.forestry.blocks.BogEarthInfo;
import io.github.drmanganese.topaddons.addons.forestry.entities.EntityButterflyInfo;
import io.github.drmanganese.topaddons.addons.forestry.tiles.*;
import io.github.drmanganese.topaddons.addons.forge.tiles.FluidCapInfo;
import io.github.drmanganese.topaddons.api.IAddonBlocksAndEntities;
import io.github.drmanganese.topaddons.api.IAddonElements;
import io.github.drmanganese.topaddons.api.IBlockInfo;
import io.github.drmanganese.topaddons.api.IEntityInfo;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.elements.ElementSync;
import io.github.drmanganese.topaddons.elements.forestry.ElementFarm;

import forestry.core.tiles.TileNaturalistChest;
import forestry.database.tiles.TileDatabase;
import forestry.factory.tiles.TileMoistener;
import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

import com.google.common.collect.ImmutableMap;
import forestry.apiculture.multiblock.TileAlveary;
import forestry.apiculture.tiles.TileBeeHousingBase;
import forestry.arboriculture.tiles.TileLeaves;
import forestry.arboriculture.tiles.TileSapling;
import forestry.core.fluids.Fluids;
import forestry.core.tiles.TileAnalyzer;
import forestry.energy.tiles.TileEngineBiogas;
import forestry.energy.tiles.TileEnginePeat;
import forestry.factory.tiles.TileBottler;
import forestry.factory.tiles.TileCarpenter;
import forestry.factory.tiles.TileCentrifuge;
import forestry.factory.tiles.TileFabricator;
import forestry.factory.tiles.TileFermenter;
import forestry.factory.tiles.TileSqueezer;
import forestry.factory.tiles.TileStill;
import forestry.farming.tiles.TileFarm;
import forestry.lepidopterology.entities.EntityButterfly;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

@TOPAddon(dependency = "forestry")
public class AddonForestry implements IAddonBlocksAndEntities, IAddonElements {

    private static final ImmutableMap<Class<? extends Entity>, IEntityInfo> ENTITIES;
    private static final ImmutableMap<Class<? extends TileEntity>, ITileInfo> TILES;

    @GameRegistry.ObjectHolder("forestry:bog_earth")
    public static Block BOG_EARTH;

    static {
        ImmutableMap.Builder<Class<? extends TileEntity>, ITileInfo> builder = ImmutableMap.builder();
        builder.put(TileEnginePeat.class, new TileEngineInfo());
        builder.put(TileEngineBiogas.class, new TileEngineInfo());
        builder.put(TileBeeHousingBase.class, new TileBeeHousingInfo());
        builder.put(TileAlveary.class, new TileAlvearyInfo());
        builder.put(TileSapling.class, new TileSaplingInfo());
        builder.put(TileLeaves.class, new TileLeavesInfo());
        builder.put(TileAnalyzer.class, new TileAnalyzerInfo());

        final TilePoweredInfo poweredInfo = new TilePoweredInfo();
        builder.put(TileStill.class, poweredInfo);
        builder.put(TileSqueezer.class, poweredInfo);
        builder.put(TileBottler.class, poweredInfo);
        builder.put(TileCentrifuge.class, poweredInfo);
        builder.put(TileCarpenter.class, poweredInfo);

        builder.put(TileFermenter.class, new TileFermenterInfo());
        builder.put(TileFabricator.class, new TileThermionicFabInfo());
        builder.put(TileFarm.class, new TileFarmInfo());
        builder.put(TilePlanter.class, new TilePlanterInfo());
        builder.put(TileMoistener.class, new TileMoistenerInfo());

        final TileForestryStorageInfo storageInfo = new TileForestryStorageInfo();
        builder.put(TileNaturalistChest.class, storageInfo);
        builder.put(TileDatabase.class, storageInfo);

        TILES = builder.build();
        ENTITIES = ImmutableMap.of(EntityButterfly.class, new EntityButterflyInfo());
    }

    public AddonForestry() {
        for (Fluids fluid : Fluids.values()) {
            FluidCapInfo.COLORS.put(fluid.getTag(), fluid.getParticleColor().hashCode());
        }

        FluidCapInfo.TANK_NAMES.put(TileEngineBiogas.class, new String[]{"Fuel", "Heating", "Burner"});
    }


    @Nonnull
    @Override
    public ImmutableMap<Class<? extends Entity>, IEntityInfo> getEntities() {
        return ENTITIES;
    }

    @Nonnull
    @Override
    public ImmutableMap<Class<? extends TileEntity>, ITileInfo> getTiles() {
        return TILES;
    }

    @Nonnull
    @Override
    public ImmutableMap<Block, IBlockInfo> getBlocks() {
        return ImmutableMap.of(BOG_EARTH, new BogEarthInfo());
    }

    @Override
    public void registerElements(ITheOneProbe probe) {
        ElementSync.registerElement(probe, "farm", ElementFarm::new);
    }
}
