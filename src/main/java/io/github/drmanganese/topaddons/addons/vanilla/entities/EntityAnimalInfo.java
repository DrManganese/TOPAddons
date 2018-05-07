package io.github.drmanganese.topaddons.addons.vanilla.entities;

import io.github.drmanganese.topaddons.api.IEntityInfo;
import io.github.drmanganese.topaddons.util.Formatting;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;

public class EntityAnimalInfo implements IEntityInfo<EntityAnimal> {

    @Override
    public void getInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, EntityAnimal entity, IProbeHitEntityData data) {
        final int age = entity.getGrowingAge();
        if (age > 0) {
            probeInfo.text(TextStyleClass.LABEL + "{*topaddons.vanilla:nobreed*}: " + Formatting.timeInSeconds(age, Formatting.TimeUnit.TICKS));
        }
    }
}
