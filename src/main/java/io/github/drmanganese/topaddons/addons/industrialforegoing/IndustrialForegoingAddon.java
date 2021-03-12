package io.github.drmanganese.topaddons.addons.industrialforegoing;

import io.github.drmanganese.topaddons.addons.TopAddon;
import io.github.drmanganese.topaddons.addons.industrialforegoing.tiles.IndustrialActiveTileInfo;
import io.github.drmanganese.topaddons.addons.industrialforegoing.tiles.IndustrialGeneratorTileInfo;
import io.github.drmanganese.topaddons.addons.industrialforegoing.tiles.MobDuplicatorTileInfo;
import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.ITileInfo;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import com.buuz135.industrial.block.agriculturehusbandry.tile.MobDuplicatorTile;
import com.buuz135.industrial.block.tile.IndustrialGeneratorTile;
import com.google.common.collect.ImmutableMultimap;
import com.hrznstudio.titanium.block.tile.ActiveTile;

import javax.annotation.Nonnull;
import java.util.Optional;

public class IndustrialForegoingAddon extends TopAddon implements IAddonBlocks {

    private final ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> TILES = ImmutableMultimap.of(
        ActiveTile.class, new IndustrialActiveTileInfo(),
        IndustrialGeneratorTile.class, new IndustrialGeneratorTileInfo(),
        MobDuplicatorTile.class, new MobDuplicatorTileInfo()
    );

    public IndustrialForegoingAddon() {
        super("industrialforegoing");
    }

    /**
     * Check each side of the tile and return the first IItemHandler capability with at least 1 slot. Can be empty
     * if no side is exposing the capability.
     * <p>
     * This method is necessary because the Industrial Foregoing inventory handler does not expose the capability on
     * side = null.
     *
     * @param tile TileEntity to search.
     * @return The first valid IItemHandler.
     */
    public static Optional<IItemHandler> getFirstItemHandlerFromTile(TileEntity tile) {
        for (final Direction direction : Direction.values()) {
            final Optional<IItemHandler> itemHandler = tile
                .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction)
                .filter(i -> i.getSlots() > 0);
            if (itemHandler.isPresent()) return itemHandler;
        }
        return Optional.empty();
    }

    @Nonnull
    @Override
    public ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> getTileInfos() {
        return TILES;
    }
}
