package io.github.drmanganese.topaddons.addons.vanilla.entities;

import io.github.drmanganese.topaddons.api.IEntityInfo;
import io.github.drmanganese.topaddons.util.Formatting;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

public class BreedCoolDownInfo implements IEntityInfo<Animal> {

    @Override
    public void getInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, Animal entity, IProbeHitEntityData data) {
        final int age = entity.getAge();
        if (age > 0)
            InfoHelper.textPrefixed(probeInfo, "{*topaddons.vanilla:nobreed*}", Formatting.timeInSeconds(age, Formatting.TimeUnit.TICKS));
    }
}
