package io.github.drmanganese.topaddons.api;

import io.github.drmanganese.topaddons.reference.Reference;

import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.ClassUtils;

import javax.annotation.Nonnull;
import java.util.List;

public interface IAddonEntities extends IProbeInfoEntityProvider {

    @Nonnull
    @Override
    default String getID() {
        return Reference.MOD_ID + ":" + this.getClass().getDeclaredAnnotation(TOPAddon.class).dependency();
    }

    /**
     * See {@link IProbeInfoEntityProvider#addProbeEntityInfo}.
     */
    @Override
    default void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        if (entity != null) {
            //Get the entity's class and superclasses. All of these are Entity or children of Entity.
            List<Class<?>> classes = ClassUtils.getAllSuperclasses(entity.getClass());
            classes.add(entity.getClass());

            for (Class class_ : classes) {
                if (getEntities().containsKey(class_)) {
                    //noinspection unchecked
                    getEntities().get(class_).getInfo(mode, probeInfo, player, world, (Entity) class_.cast(entity), data);
                }
            }
        }
    }

    /**
     * @return Map of {@link Entity} classes this addon provides info for.
     */
    @Nonnull
    ImmutableMap<Class<? extends Entity>, IEntityInfo> getEntities();
}
