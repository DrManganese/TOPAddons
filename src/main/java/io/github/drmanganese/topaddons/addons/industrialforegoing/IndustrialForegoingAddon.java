package io.github.drmanganese.topaddons.addons.industrialforegoing;

import io.github.drmanganese.topaddons.addons.TopAddon;
import io.github.drmanganese.topaddons.addons.forge.tiles.FluidHandlerTileInfo;
import io.github.drmanganese.topaddons.addons.industrialforegoing.tiles.IndustrialActiveTileInfo;
import io.github.drmanganese.topaddons.addons.industrialforegoing.tiles.MobDuplicatorTileInfo;
import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.IAddonConfig;
import io.github.drmanganese.topaddons.api.ITileInfo;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import com.buuz135.industrial.block.agriculturehusbandry.tile.MobDuplicatorTile;
import com.google.common.collect.ImmutableMultimap;
import com.hrznstudio.titanium.block.tile.ActiveTile;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class IndustrialForegoingAddon extends TopAddon implements IAddonBlocks, IAddonConfig {

    private final ImmutableMultimap<Class<? extends BlockEntity>, ITileInfo> TILES = ImmutableMultimap.of(
        ActiveTile.class, new IndustrialActiveTileInfo(),
        MobDuplicatorTile.class, new MobDuplicatorTileInfo()
    );

    public static ForgeConfigSpec.BooleanValue colorTinyProgressBackground;

    public IndustrialForegoingAddon() {
        super("industrialforegoing");
        FluidHandlerTileInfo.CUSTOM_TANK_KEYS.put("industrialforegoing:fermentation_station", "topaddons.industrialforegoing:fermentation_station");
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
    public static Optional<IItemHandler> getFirstItemHandlerFromTile(BlockEntity tile) {
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
    public ImmutableMultimap<Class<? extends BlockEntity>, ITileInfo> getTileInfos() {
        return TILES;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder, ModConfig.Type type) {
        if (type == ModConfig.Type.CLIENT) {
            builder.push(name);
            colorTinyProgressBackground = builder.comment("Color the background of tiny progress bars with the GUI's progress bar color", "Replaces the default machine progress in the forge section").define("colorTinyProgressBackground", true);
            builder.pop();
        }
    }

    @Override
    public List<ForgeConfigSpec.ConfigValue<?>> getClientConfigValuesToSync() {
        return Collections.singletonList(colorTinyProgressBackground);
    }
}
