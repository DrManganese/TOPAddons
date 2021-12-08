package io.github.drmanganese.topaddons.addons.thermal;

import io.github.drmanganese.topaddons.addons.TopAddon;
import io.github.drmanganese.topaddons.addons.thermal.tiles.DynamoTileInfo;
import io.github.drmanganese.topaddons.addons.thermal.tiles.EnergyCellTileInfo;
import io.github.drmanganese.topaddons.addons.thermal.tiles.MachineTileInfo;
import io.github.drmanganese.topaddons.addons.thermal.tiles.RedstoneControlTileInfo;
import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.IAddonConfig;
import io.github.drmanganese.topaddons.api.ITileInfo;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import cofh.thermal.core.tileentity.device.DeviceRockGenTile;
import cofh.thermal.core.tileentity.storage.EnergyCellTile;
import cofh.thermal.lib.tileentity.DynamoTileBase;
import cofh.thermal.lib.tileentity.MachineTileProcess;
import cofh.thermal.lib.tileentity.ThermalTileAugmentable;
import com.google.common.collect.ImmutableMultimap;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class ThermalExpansionAddon extends TopAddon implements IAddonBlocks, IAddonConfig {

    private static final ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> TILES;
    public static ForgeConfigSpec.BooleanValue alwaysShowCellIo;

    static {
        final ImmutableMultimap.Builder<Class<? extends TileEntity>, ITileInfo> builder = ImmutableMultimap.builder();
        builder.put(DynamoTileBase.class, new DynamoTileInfo());
        builder.put(EnergyCellTile.class, new EnergyCellTileInfo());
        builder.put(MachineTileProcess.class, MachineTileInfo.INSTANCE);
        builder.put(DeviceRockGenTile.class, MachineTileInfo.INSTANCE);
        builder.put(ThermalTileAugmentable.class, new RedstoneControlTileInfo());
        TILES = builder.build();
    }

    public ThermalExpansionAddon() {
        super("thermal_expansion");
    }

    @Nonnull
    @Override
    public ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> getTileInfos() {
        return TILES;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder, ModConfig.Type type) {
        if (type == ModConfig.Type.CLIENT) {
            builder.push("thermal");
            alwaysShowCellIo = builder.comment("Always show Flux Cell input/output rate (even when not extended)").define("alwaysShowCellIo", false);
            builder.pop();
        }
    }

    @Override
    public List<ForgeConfigSpec.ConfigValue<?>> getClientConfigValuesToSync() {
        return Collections.singletonList(alwaysShowCellIo);
    }
}
