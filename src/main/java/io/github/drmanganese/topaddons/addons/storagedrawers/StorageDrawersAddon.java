package io.github.drmanganese.topaddons.addons.storagedrawers;

import io.github.drmanganese.topaddons.addons.TopAddon;
import io.github.drmanganese.topaddons.addons.storagedrawers.tiles.DrawerInfo;
import io.github.drmanganese.topaddons.api.*;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class StorageDrawersAddon extends TopAddon implements IAddonBlocks, IAddonConfig, IAddonConfigProviders {

    private static final DrawerInfo DRAWER_INFO = new DrawerInfo();

    private static final ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> TILE_INFOS = ImmutableMultimap.of(TileEntityDrawers.class, DRAWER_INFO);
    private static final ImmutableMap<Object, ITileConfigProvider> TILE_CONFIGS = ImmutableMap.of(TileEntityDrawers.class, DRAWER_INFO);

    public static ForgeConfigSpec.BooleanValue hideConcealed;
    public static ForgeConfigSpec.BooleanValue alwaysShowExtended;

    public StorageDrawersAddon() {
        super("storagedrawers");
    }

    @Nonnull
    @Override
    public ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> getTileInfos() {
        return TILE_INFOS;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder, ModConfig.Type type) {
        builder.push(name);
        if (type == ModConfig.Type.COMMON)
            hideConcealed = builder.comment("Hide info when drawer is concealed").define("hideConcealed", false);
        if (type == ModConfig.Type.CLIENT)
            alwaysShowExtended = builder.comment("Always display the extended drawer info").define("alwaysDisplayExtended", false);
        builder.pop();
    }

    @Override
    public List<ForgeConfigSpec.ConfigValue<?>> getClientConfigValuesToSync() {
        return Collections.singletonList(alwaysShowExtended);
    }

    @Nonnull
    @Override
    public ImmutableMap<Object, ITileConfigProvider> getBlockConfigProviders() {
        return TILE_CONFIGS;
    }

    @Override
    public String getFancyName() {
        return "Storage Drawers";
    }
}
