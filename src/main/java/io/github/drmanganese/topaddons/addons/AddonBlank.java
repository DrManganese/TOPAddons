package io.github.drmanganese.topaddons.addons;

import net.minecraft.util.text.TextFormatting;
import io.github.drmanganese.topaddons.api.ITOPAddon;
import io.github.drmanganese.topaddons.api.ItemArmorProbed;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.reference.Reference;

import java.util.ArrayList;
import java.util.List;

import mcjty.theoneprobe.api.IProbeInfo;

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
    public boolean hasHelmets() {
        return false;
    }

    @Override
    public List<Class<? extends ItemArmorProbed>> getHelmets() {
        return new ArrayList<>();
    }

    IProbeInfo textPrefixed(IProbeInfo probeInfo, String prefix, String text) {
        return textPrefixed(probeInfo, prefix, text, TextFormatting.YELLOW);
    }

    IProbeInfo textPrefixed(IProbeInfo probeInfo, String prefix, String text, TextFormatting formatting) {
        return probeInfo.text(formatting + prefix + ": " + TextFormatting.WHITE + text);
    }
}
