package io.github.drmanganese.topaddons.addons.thermal;

import io.github.drmanganese.topaddons.addons.TopAddon;
import io.github.drmanganese.topaddons.addons.thermal.tiles.DynamoTileInfo;
import io.github.drmanganese.topaddons.addons.thermal.tiles.EnergyCellTileInfo;
import io.github.drmanganese.topaddons.addons.thermal.tiles.MachineTileInfo;
import io.github.drmanganese.topaddons.addons.thermal.tiles.RedstoneControlTileInfo;
import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.IAddonConfig;
import io.github.drmanganese.topaddons.api.ITileInfo;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import cofh.thermal.core.block.entity.device.DeviceRockGenTile;
import cofh.thermal.core.block.entity.storage.EnergyCellBlockEntity;
import cofh.thermal.lib.block.entity.AugmentableBlockEntity;
import cofh.thermal.lib.block.entity.DynamoBlockEntity;
import cofh.thermal.lib.block.entity.MachineBlockEntity;
import com.google.common.collect.ImmutableMultimap;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

import cofh.thermal.expansion.block.entity.dynamo.DynamoCompressionTile;

public class ThermalExpansionAddon extends TopAddon implements IAddonBlocks, IAddonConfig {

    private static final ImmutableMultimap<Class<? extends BlockEntity>, ITileInfo> TILES;
    public static ForgeConfigSpec.BooleanValue alwaysShowCellIo;

    static {
        final ImmutableMultimap.Builder<Class<? extends BlockEntity>, ITileInfo> builder = ImmutableMultimap.builder();
        builder.put(DynamoBlockEntity.class, new DynamoTileInfo());
        builder.put(EnergyCellBlockEntity.class, new EnergyCellTileInfo());
        builder.put(MachineBlockEntity.class, MachineTileInfo.INSTANCE);
        builder.put(DeviceRockGenTile.class, MachineTileInfo.INSTANCE);
        builder.put(AugmentableBlockEntity.class, new RedstoneControlTileInfo());
        TILES = builder.build();
    }

    public ThermalExpansionAddon() {
        super("thermal_expansion");
    }

    @Nonnull
    @Override
    public ImmutableMultimap<Class<? extends BlockEntity>, ITileInfo> getTileInfos() {
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
