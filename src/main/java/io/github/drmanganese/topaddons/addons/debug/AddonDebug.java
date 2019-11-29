package io.github.drmanganese.topaddons.addons.debug;

import io.github.drmanganese.topaddons.api.IAddonBlocksAndEntities;
import io.github.drmanganese.topaddons.api.IEntityInfo;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.reference.Reference;
import io.github.drmanganese.topaddons.util.PlayerHelper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.lang3.ClassUtils;

import javax.annotation.Nonnull;

@TOPAddon(dependency = "forge", fancyName = "Debug", order = 0)
public class AddonDebug implements IAddonBlocksAndEntities {

    @GameRegistry.ObjectHolder("minecraft:stick")
    private static Item STICK;
    private final DebugInfo debugInfo;

    public AddonDebug() {
        debugInfo = new DebugInfo();
    }

    @Nonnull
    @Override
    public String getID() {
        return Reference.MOD_ID + ":debug";
    }

    @Nonnull
    @Override
    public ImmutableMap<Class<? extends Entity>, IEntityInfo> getEntities() {
        return ImmutableMap.of(Entity.class, debugInfo);
    }

    @Nonnull
    @Override
    public ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> getTiles() {
        return ImmutableMultimap.of(TileEntity.class, debugInfo);
    }

    public final class DebugInfo implements IEntityInfo<Entity>, ITileInfo<TileEntity> {

        @Override
        public void getInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
            if (PlayerHelper.isHolding(player, STICK)) {
                probeInfo.text(TextStyleClass.OK + entity.getClass().getName());
                ClassUtils.getAllSuperclasses(entity.getClass()).stream().map(Class::getName).forEach(probeInfo::text);
            }
        }

        @Override
        public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileEntity tile) {
            if (PlayerHelper.isHolding(player, STICK)) {
                probeInfo.text(TextStyleClass.OK + tile.getClass().getName());
                ClassUtils.getAllSuperclasses(tile.getClass()).stream().map(Class::getName).forEach(probeInfo::text);
            }
        }
    }
}
