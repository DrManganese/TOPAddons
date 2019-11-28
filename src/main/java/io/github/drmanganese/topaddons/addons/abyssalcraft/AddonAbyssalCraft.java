package io.github.drmanganese.topaddons.addons.abyssalcraft;

import io.github.drmanganese.topaddons.addons.abyssalcraft.tiles.EnergyCollectorTileInfo;
import io.github.drmanganese.topaddons.addons.abyssalcraft.tiles.EnergyContainerTileInfo;
import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.api.TOPAddon;

import com.google.common.collect.ImmutableMap;
import com.shinoow.abyssalcraft.common.blocks.tile.TileEntityEnergyCollector;
import com.shinoow.abyssalcraft.common.blocks.tile.TileEntityEnergyContainer;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

@TOPAddon(dependency = "abyssalcraft")
public class AddonAbyssalCraft implements IAddonBlocks {

    @Nonnull
    @Override
    public ImmutableMap<Class<? extends TileEntity>, ITileInfo> getTiles() {
        return ImmutableMap.of(TileEntityEnergyCollector.class, new EnergyCollectorTileInfo(),TileEntityEnergyContainer.class, new EnergyContainerTileInfo());
    }
}
