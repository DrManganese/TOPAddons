package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.api.TOPAddon;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

import java.util.Collections;
import java.util.List;

import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IEntityDisplayOverride;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;

@TOPAddon(dependency = "iceandfire")
public class AddonIceAndFire extends AddonBlank {

    @Override
    public List<IEntityDisplayOverride> getEntityDisplayOverrides() {
        return Collections.singletonList((mode, probeInfo, player, world, entity, data) -> {
            if (entity instanceof EntityDragonBase) {
                EntityDragonBase dragon = (EntityDragonBase) entity;
                TextFormatting color = player.getCapability(TOPAddons.OPTS_CAP, null).getBoolean("colorDragonName") ? dragon.isFire ? TextFormatting.RED : TextFormatting.AQUA : TextFormatting.RESET;
                char gender = dragon.isMale() ? '\u2642' : '\u2640';

                if (Tools.show(mode, mcjty.theoneprobe.config.Config.getRealConfig().getShowModName())) {
                    probeInfo.horizontal()
                            .entity(entity)
                            .vertical()
                            .text(color + entity.getDisplayName().getFormattedText() + color + gender)
                            .text(TextStyleClass.MODNAME + Tools.getModName(entity));
                } else {
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .entity(entity)
                            .text(color + entity.getDisplayName().getFormattedText() + color + gender);
                }

                return true;
            }

            return false;
        });
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        if (entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;

            textPrefixed(probeInfo, "Stage", String.valueOf(dragon.getDragonStage()));
            if (dragon.hasCustomName() && !player.getCapability(TOPAddons.OPTS_CAP, null).getBoolean("colorDragonName")) {
                if (dragon.isFire) {
                    probeInfo.text(TextFormatting.RED + "Fire Dragon");
                } else {
                    probeInfo.text(TextFormatting.AQUA + "Ice Dragon");
                }
            }
        }
    }
}
