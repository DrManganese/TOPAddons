package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.TOPRegistrar;
import io.github.drmanganese.topaddons.api.ITOPAddon;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.reference.ElementSync;
import io.github.drmanganese.topaddons.reference.EnumChip;
import io.github.drmanganese.topaddons.reference.Reference;
import io.github.drmanganese.topaddons.styles.ProgressStyleTOPAddonGrey;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mcjty.theoneprobe.api.IBlockDisplayOverride;
import mcjty.theoneprobe.api.IElementFactory;
import mcjty.theoneprobe.api.IEntityDisplayOverride;
import mcjty.theoneprobe.api.ILayoutStyle;
import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;

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
    public void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
    }

    @Override
    public void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
    }

    @Override
    public void updateConfigs(Configuration config) {
    }

    @Override
    public void registerElements() {
    }

    @Override
    public void addFluidColors() {
    }

    @Override
    public void addTankNames() {
    }

    @Override
    public Map<Class<? extends ItemArmor>, EnumChip> getSpecialHelmets() {
        return new HashMap<>(0);
    }

    @Override
    public List<IEntityDisplayOverride> getEntityDisplayOverrides() {
        return Collections.emptyList();
    }

    @Override
    public List<IBlockDisplayOverride> getBlockDisplayOverrides() {
        return Collections.emptyList();
    }

    void registerElement(String name, IElementFactory factory) {
        int id = TOPRegistrar.GetTheOneProbe.probe.registerElementFactory(factory);
        ElementSync.elementIds.put(name, id);
    }

    static int getElementId(EntityPlayer player, String name) {
        return player.getCapability(TOPAddons.OPTS_CAP, null).getElementId(name);
    }

    /* Shortcut methods */

    IProbeInfo textPrefixed(IProbeInfo probeInfo, String prefix, String text, TextFormatting formatting) {
        return probeInfo.text(formatting + prefix + ": " + TextStyleClass.INFO + text);
    }

    IProbeInfo textPrefixed(IProbeInfo probeInfo, String prefix, String text) {
        return textPrefixed(probeInfo, prefix, text, TextStyleClass.LABEL);
    }

    IProbeInfo textPrefixed(IProbeInfo probeInfo, String prefix, String text, TextStyleClass styleClass) {
        return probeInfo.text(styleClass + prefix + ": " + TextStyleClass.INFO + text);
    }

    IProbeInfo progressBar(IProbeInfo probeInfo, int current, int color1, int color2) {
        return probeInfo.progress(current, 100, new ProgressStyleTOPAddonGrey().filledColor(color1).alternateFilledColor(color2).suffix("%").prefix("Progress: "));
    }

    IProbeInfo showItemStackRows(IProbeInfo probeInfo, List<ItemStack> stacks, int rowWidth, ILayoutStyle layoutStyle) {
        IProbeInfo vert = probeInfo.vertical(layoutStyle);
        IProbeInfo hori = vert.horizontal(probeInfo.defaultLayoutStyle());
        int j = 0;
        for (ItemStack stack : stacks) {
            hori.item(stack);
            j++;
            if (j > rowWidth) {
                j = 0;
                hori = vert.horizontal(probeInfo.defaultLayoutStyle());
            }
        }

        return probeInfo;
    }

    IProbeInfo showItemStackRows(IProbeInfo probeInfo, List<ItemStack> stacks, int rowWidth) {
        return showItemStackRows(probeInfo, stacks, rowWidth, probeInfo.defaultLayoutStyle());
    }
}
