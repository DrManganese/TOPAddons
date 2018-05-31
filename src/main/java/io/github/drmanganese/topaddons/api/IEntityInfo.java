package io.github.drmanganese.topaddons.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;

public interface IEntityInfo<T extends Entity> {

    /**
     * See {@link IProbeInfoEntityProvider#addProbeEntityInfo}.
     *
     * @param entity Entity the player is looking at, cast to associated class in map.
     */
    void getInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, T entity, IProbeHitEntityData data);
}
