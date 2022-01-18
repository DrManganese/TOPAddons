package io.github.drmanganese.topaddons.api;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;

public interface IEntityInfo<T extends Entity> {

    /**
     * See {@link IProbeInfoEntityProvider#addProbeEntityInfo}.
     *
     * @param world
     * @param entity Entity the player is looking at, cast to associated class in map.
     */
    void getInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, T entity, IProbeHitEntityData data);
}
