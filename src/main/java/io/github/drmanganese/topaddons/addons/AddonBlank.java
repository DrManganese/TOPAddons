package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import io.github.drmanganese.topaddons.api.ITOPAddon;
import io.github.drmanganese.topaddons.api.ItemArmorProbed;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.reference.Reference;

import java.util.ArrayList;
import java.util.List;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

public abstract class AddonBlank implements ITOPAddon {

    @Override
    public String getID() {
        String pluginName;
        TOPAddon annotation = this.getClass().getAnnotation(TOPAddon.class);
        if (annotation.fancyName().isEmpty()) {
            pluginName = annotation.dependency();
        } else {
            pluginName = annotation.fancyName();
        }

        return Reference.MOD_ID + ":" + pluginName.toLowerCase();
    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {

    }

    @Override
    public boolean hasHelmets() {
        return false;
    }

    @Override
    public void addTankNames() {

    }

    @Override
    public void addFluidColors() {

    }

    @Override
    public List<Class<? extends ItemArmorProbed>> getHelmets() {
        return new ArrayList<>();
    }

    @Override
    public void registerElements() {

    }

    IProbeInfo textPrefixed(IProbeInfo probeInfo, String prefix, String text) {
        return textPrefixed(probeInfo, prefix, text, TextFormatting.YELLOW);
    }

    IProbeInfo textPrefixed(IProbeInfo probeInfo, String prefix, String text, TextFormatting formatting) {
        return probeInfo.text(formatting + prefix + ": " + TextFormatting.WHITE + text);
    }

    @Override
    public void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {

    }

    @Override
    public void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    }

}
