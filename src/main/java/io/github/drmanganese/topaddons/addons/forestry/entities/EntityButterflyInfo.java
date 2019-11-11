package io.github.drmanganese.topaddons.addons.forestry.entities;

import io.github.drmanganese.topaddons.api.IEntityInfo;
import io.github.drmanganese.topaddons.util.InfoHelper;

import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import forestry.api.lepidopterology.EnumButterflyChromosome;
import forestry.api.lepidopterology.IButterfly;
import forestry.lepidopterology.entities.EntityButterfly;

import java.util.ArrayList;
import java.util.List;

public class EntityButterflyInfo implements IEntityInfo<EntityButterfly> {

    private static final String[] ALLELES = new String[]{"Hybrid", "Fertilized", "Size",
            "Production", "Metabolism", "Climate",
            "Humidity", "Nocturnal", "Tolerant Flyer"};

    @Override
    public void getInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, EntityButterfly entity, IProbeHitEntityData data) {
        final IButterfly butterfly = entity.getButterfly();
        if (mode == ProbeMode.EXTENDED) {
            if (butterfly.isAnalyzed()) {
                final List<String> lines = new ArrayList<>();
                butterfly.addTooltip(lines);

                int i = 0;
                if (butterfly.isPureBred(EnumButterflyChromosome.SPECIES)) i++;
                if (butterfly.getMate() == null) i++;

                for (String line : lines) {
                    InfoHelper.textPrefixed(probeInfo, ALLELES[i], line);
                    i++;
                }
            } else {
                probeInfo.text(TextStyleClass.OBSOLETE + "Not Analyzed");
            }
        }
    }
}
