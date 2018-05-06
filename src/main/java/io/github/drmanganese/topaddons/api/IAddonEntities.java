package io.github.drmanganese.topaddons.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableMap;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;

import javax.annotation.Nonnull;

public interface IAddonEntities extends IProbeInfoEntityProvider {

    /**
     * See {@link IProbeInfoEntityProvider#addProbeEntityInfo}.
     */
    @Override
    default void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        if (entity != null && getEntities().containsKey(entity.getClass())) {
            //noinspection unchecked
            getEntities().get(entity.getClass()).getInfo(mode, probeInfo, player, world, entity.getClass().cast(entity), data);
        }

        //TODO Info based on predicates
    }

    @Nonnull
    ImmutableMap<Class<? extends Entity>, IEntityInfo> getEntities();
}
