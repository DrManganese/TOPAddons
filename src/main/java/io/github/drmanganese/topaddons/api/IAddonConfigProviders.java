package io.github.drmanganese.topaddons.api;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeConfigProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.ClassUtils;

import javax.annotation.Nonnull;
import java.util.List;

public interface IAddonConfigProviders extends IProbeConfigProvider {

    /**
     * See {@link IProbeConfigProvider#getProbeConfig}.
     */
    @Override
    default void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        //Get the tileentity's class and superclasses. All of these are TileEntity or children of TileEntity.
        final TileEntity tile = world.getTileEntity(data.getPos());
        if (tile != null) {
            List<Class<?>> classes = ClassUtils.getAllSuperclasses(tile.getClass());
            classes.add(tile.getClass());

            for (Class class_ : classes) {
                if (getBlockConfigProviders().containsKey(class_)) {
                    //noinspection unchecked
                    getBlockConfigProviders().get(class_).getProbeConfig(config, player, world, blockState, data);
                }
            }
        }

        final Block block = blockState.getBlock();
        if (getBlockConfigProviders().containsKey(block)) {
            getBlockConfigProviders().get(block).getProbeConfig(config, player, world, blockState, data);
        }
    }

    @Override
    default void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {

    }

    /**
     * @return Map of {@link TileEntity} classes or Block instances this addon provides configs for.
     */
    @Nonnull
    default ImmutableMap<Object, ITileConfigProvider> getBlockConfigProviders() {
        return ImmutableMap.of();
    }

}
