package io.github.drmanganese.topaddons.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import org.apache.commons.lang3.ClassUtils;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Addons which implement this interface provide information for blocks and tile entities.
 */
public interface IAddonBlocks extends IProbeInfoProvider {


    /**
     * See {@link IProbeInfoProvider#addProbeInfo}.
     */
    @Override
    default void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        // TILE CLASS
        final TileEntity tile = world.getTileEntity(data.getPos());
        if (tile != null) {
            final List<Class<?>> tileClasses = ClassUtils.getAllSuperclasses(tile.getClass());
            tileClasses.add(tile.getClass());

            //noinspection unchecked
            tileClasses
                .stream().filter(TileEntity.class::isAssignableFrom)
                .map(c -> (Class<? extends TileEntity>) c)
                .forEach(cls -> getTileInfos().get(cls).forEach(o -> o.addProbeInfo(mode, probeInfo, player, world, blockState, data, cls.cast(tile))));
        }

        // BLOCK CLASS
        final Block block = blockState.getBlock();
        if (getBlockInfos().containsKey(block)) {
            getBlockInfos().get(block).addProbeInfo(mode, probeInfo, player, world, blockState, data);
        }

        final List<Class<?>> blockClasses = ClassUtils.getAllSuperclasses(block.getClass());
        blockClasses.add(block.getClass());

        for (final Class<?> blockClass : blockClasses) {
            if (getBlockClasses().containsKey(blockClass)) {
                getBlockClasses().get(blockClass).addProbeInfo(mode, probeInfo, player, world, blockState, data);
            }
        }
    }

    /**
     * @return Map of {@link TileEntity} classes this addon provides info for.
     */
    @Nonnull
    default ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> getTileInfos() {
        return ImmutableMultimap.of();
    }

    /**
     * @return Map of {@link Block} instances this addon provides info for.
     */
    @Nonnull
    default ImmutableMap<Block, IBlockInfo> getBlockInfos() {
        return ImmutableMap.of();
    }

    /**
     * @return Map of {@link Block} classes this addon provides info for.
     */
    @Nonnull
    default ImmutableMap<Class<? extends Block>, IBlockInfo> getBlockClasses() {
        return ImmutableMap.of();
    }
}
