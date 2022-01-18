package io.github.drmanganese.topaddons.api;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import com.google.common.collect.ImmutableMap;
import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeConfigProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import org.apache.commons.lang3.ClassUtils;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Addons which implement this interface provide The One Prober configuration modifications for blocks and tile
 * entities.
 */
public interface IAddonConfigProviders extends IProbeConfigProvider {

    @Override
    default void getProbeConfig(IProbeConfig config, Player player, Level world, Entity entity, IProbeHitEntityData data) {

    }

    /**
     * See {@link IProbeConfigProvider#getProbeConfig}.
     */
    @Override
    default void getProbeConfig(IProbeConfig config, Player player, Level world, BlockState blockState, IProbeHitData data) {
        //Get the tileentity's class and superclasses. All of these are BlockEntity or children of BlockEntity.
        final BlockEntity tile = world.getBlockEntity(data.getPos());
        if (tile != null) {
            final List<Class<?>> classes = ClassUtils.getAllSuperclasses(tile.getClass());
            classes.add(tile.getClass());

            for (final Class class_ : classes) {
                if (getBlockConfigProviders().containsKey(class_)) {
                    getBlockConfigProviders().get(class_).getProbeConfig(config, player, world, blockState, data);
                }
            }
        }

        final Block block = blockState.getBlock();
        if (getBlockConfigProviders().containsKey(block)) {
            getBlockConfigProviders().get(block).getProbeConfig(config, player, world, blockState, data);
        }
    }

    /**
     * @return Map of {@link BlockEntity} classes or Block instances this addon provides configs for.
     */
    @Nonnull
    default ImmutableMap<Object, ITileConfigProvider> getBlockConfigProviders() {
        return ImmutableMap.of();
    }

}
