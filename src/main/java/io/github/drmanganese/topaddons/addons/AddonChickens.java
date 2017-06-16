package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.api.TOPAddon;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.chicken.EntityChickensChicken;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;

@TOPAddon(dependency = "chickens")
public class AddonChickens extends AddonBlank {

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        //
    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        if (entity instanceof EntityChickensChicken) {
            EntityChickensChicken chicken = (EntityChickensChicken) entity;

            textPrefixed(probeInfo, "{*topaddons.chickens:tier*}", String.valueOf(chicken.getTier()));

            if (!chicken.isChild()) {
                int progress = chicken.getLayProgress();
                textPrefixed(probeInfo, "{*topaddons.chickens:next_egg*}", String.format("%s%d min%s", (progress <= 0) ? '<' : "\u2248", progress, (progress == 1) ? "" : 's'));
            }


            if (mode == ProbeMode.EXTENDED) {
                if (chicken.getStatsAnalyzed() || ChickensMod.instance.getAlwaysShowStats()) {
                    IProbeInfo vert = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xff5555ff));
                    textPrefixed(vert, "{*topaddons.chickens:growth*}", String.valueOf(chicken.getGrowth()));
                    textPrefixed(vert, "{*topaddons.chickens:gain*}", String.valueOf(chicken.getGain()));
                    textPrefixed(vert, "{*topaddons.chickens:strength*}", String.valueOf(chicken.getStrength()));
                } else {
                    probeInfo.text(TextStyleClass.OBSOLETE + "{*topaddons.chickens:unanalyzed*}");
                }
            }
        }
    }

    public static void addChickensChickenInfo(IProbeInfo probeInfo, Entity entity) {
        if (entity instanceof EntityChickensChicken) {
            EntityChickensChicken chicken = (EntityChickensChicken) entity;
            if (chicken.getStatsAnalyzed() || ChickensMod.instance.getAlwaysShowStats()) {
                IProbeInfo vert = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xff5555ff));
                textPrefixed(vert, "{*topaddons.chickens:growth*}", String.valueOf(chicken.getGrowth()));
                textPrefixed(vert, "{*topaddons.chickens:gain*}", String.valueOf(chicken.getGain()));
                textPrefixed(vert, "{*topaddons.chickens:strength*}", String.valueOf(chicken.getStrength()));
            }
        }
    }
}
