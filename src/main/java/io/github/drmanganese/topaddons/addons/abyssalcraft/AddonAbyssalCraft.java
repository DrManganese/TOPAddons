package io.github.drmanganese.topaddons.addons.abyssalcraft;

import io.github.drmanganese.topaddons.addons.abyssalcraft.tiles.EnergyContainerTileInfo;
import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.api.TOPAddon;

import com.google.common.collect.ImmutableMultimap;
import com.shinoow.abyssalcraft.common.blocks.tile.TileEntityEnergyCollector;
import com.shinoow.abyssalcraft.common.blocks.tile.TileEntityEnergyContainer;
import com.shinoow.abyssalcraft.common.blocks.tile.TileEntityEnergyPedestal;
import com.shinoow.abyssalcraft.common.blocks.tile.TileEntitySacrificialAltar;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

@TOPAddon(dependency = "abyssalcraft")
public class AddonAbyssalCraft implements IAddonBlocks {

    private static final ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> TILES;

    static {
        final ImmutableMultimap.Builder<Class<? extends TileEntity>, ITileInfo> builder = ImmutableMultimap.builder();
        final EnergyContainerTileInfo energyContainerTileInfo = new EnergyContainerTileInfo();

        TILES = builder
                .put(TileEntityEnergyCollector.class, energyContainerTileInfo)
                .put(TileEntityEnergyContainer.class, energyContainerTileInfo)
                .put(TileEntityEnergyPedestal.class, energyContainerTileInfo)
                .put(TileEntitySacrificialAltar.class, energyContainerTileInfo)
                .build();
    }

    @Nonnull
    @Override
    public ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> getTiles() {
        return TILES;
    }
}
