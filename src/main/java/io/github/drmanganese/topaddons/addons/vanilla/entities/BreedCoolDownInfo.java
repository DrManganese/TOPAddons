package io.github.drmanganese.topaddons.addons.vanilla.entities;

import io.github.drmanganese.topaddons.api.IEntityInfo;
import io.github.drmanganese.topaddons.util.Formatting;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

public class BreedCoolDownInfo implements IEntityInfo<AnimalEntity> {

    @Override
    public void getInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, AnimalEntity entity, IProbeHitEntityData data) {
        final int age = entity.getGrowingAge();
        if (age > 0)
            InfoHelper.textPrefixed(probeInfo, "{*topaddons.vanilla:nobreed*}", Formatting.timeInSeconds(age, Formatting.TimeUnit.TICKS));
    }
}
