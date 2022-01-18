package io.github.drmanganese.topaddons.api;

import io.github.drmanganese.topaddons.addons.TopAddon;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import com.google.common.collect.ImmutableMap;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;
import org.apache.commons.lang3.ClassUtils;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Addons which implement this interface provide information for entities.
 */
public interface IAddonEntities {

    /**
     * See {@link IProbeInfoEntityProvider#addProbeEntityInfo} and {@link TopAddon#asEntityInfoProvider()}.
     */
    default void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, Entity entity, IProbeHitEntityData data) {
        if (entity != null) {
            //Get the entity's class and superclasses. All of these are Entity or children of Entity.
            final List<Class<?>> classes = ClassUtils.getAllSuperclasses(entity.getClass());
            classes.add(entity.getClass());

            for (final Class class_ : classes) {
                if (getEntityInfos().containsKey(class_)) {
                    //noinspection unchecked
                    getEntityInfos().get(class_).getInfo(mode, probeInfo, player, world, (Entity) class_.cast(entity), data);
                }
            }
        }
    }

    /**
     * @return Map of {@link Entity} classes this addon provides info for.
     */
    @Nonnull
    ImmutableMap<Class<? extends Entity>, IEntityInfo> getEntityInfos();
}
