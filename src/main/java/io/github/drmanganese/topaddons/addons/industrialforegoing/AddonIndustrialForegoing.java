package io.github.drmanganese.topaddons.addons.industrialforegoing;

import io.github.drmanganese.topaddons.addons.industrialforegoing.tiles.*;
import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.IAddonElements;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.elements.ElementSync;
import io.github.drmanganese.topaddons.elements.industrialforegoing.ElementCropSower;

import com.buuz135.industrial.tile.agriculture.CropSowerTile;
import com.buuz135.industrial.tile.generator.AbstractReactorTile;
import com.buuz135.industrial.tile.generator.PetrifiedFuelGeneratorTile;
import com.buuz135.industrial.tile.misc.BlackHoleControllerTile;
import com.buuz135.industrial.tile.misc.BlackHoleUnitTile;
import com.buuz135.industrial.tile.misc.DyeMixerTile;
import com.buuz135.industrial.tile.mob.MobDuplicatorTile;
import com.buuz135.industrial.tile.world.LaserBaseTile;
import com.buuz135.industrial.tile.world.MaterialStoneWorkFactoryTile;
import com.buuz135.industrial.tile.world.TreeFluidExtractorTile;
import com.google.common.collect.ImmutableMultimap;
import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

@TOPAddon(dependency = "industrialforegoing")
public class AddonIndustrialForegoing implements IAddonBlocks, IAddonElements {

    private final ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> TILES;

    public AddonIndustrialForegoing() {
        ImmutableMultimap.Builder<Class<? extends TileEntity>, ITileInfo> mapBuilder = ImmutableMultimap.builder();
        mapBuilder.put(AbstractReactorTile.class, new AbstractReactorInfo());
        mapBuilder.put(BlackHoleControllerTile.class, new BlackHoleUnitControllerInfo());
        mapBuilder.put(BlackHoleUnitTile.class, new BlackHoleUnitInfo());
        mapBuilder.put(CropSowerTile.class, new CropSowerInfo());
        mapBuilder.put(DyeMixerTile.class, new DyeMixerInfo());
        mapBuilder.put(LaserBaseTile.class, new LaserBaseInfo());
        mapBuilder.put(MaterialStoneWorkFactoryTile.class, new MaterialStoneWorkFactoryInfo());
        mapBuilder.put(MobDuplicatorTile.class, new MobDuplicatorInfo());
        mapBuilder.put(PetrifiedFuelGeneratorTile.class, new PetrifiedGeneratorInfo());
        mapBuilder.put(TreeFluidExtractorTile.class, new TreeFluidExtractorInfo());
        TILES = mapBuilder.build();
    }

    @Nonnull
    @Override
    public ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> getTiles() {
        return TILES;
    }

    @Override
    public void registerElements(ITheOneProbe probe) {
        ElementSync.registerElement(probe, "sower_grid", ElementCropSower::new);
    }
}
