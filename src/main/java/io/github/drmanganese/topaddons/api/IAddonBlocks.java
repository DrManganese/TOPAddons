package io.github.drmanganese.topaddons.api;

import io.github.drmanganese.topaddons.addons.TopAddon;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

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
public interface IAddonBlocks {

    /**
     * See {@link IProbeInfoProvider#addProbeInfo} and {@link TopAddon#asBlockInfoProvider()}.
     */
    default void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData data) {
        // TILE CLASS
        final BlockEntity tile = world.getBlockEntity(data.getPos());
        if (tile != null) {
            final List<Class<?>> tileClasses = ClassUtils.getAllSuperclasses(tile.getClass());
            tileClasses.add(tile.getClass());

            //noinspection unchecked
            tileClasses
                .stream().filter(BlockEntity.class::isAssignableFrom)
                .map(c -> (Class<? extends BlockEntity>) c)
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
     * @return Map of {@link BlockEntity} classes this addon provides info for.
     */
    @Nonnull
    default ImmutableMultimap<Class<? extends BlockEntity>, ITileInfo> getTileInfos() {
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
