package io.github.drmanganese.topplugins.plugins;

import net.minecraft.util.text.TextFormatting;
import io.github.drmanganese.topplugins.api.ITOPPlugin;
import io.github.drmanganese.topplugins.api.TOPPlugin;
import io.github.drmanganese.topplugins.api.ItemArmorProbed;
import io.github.drmanganese.topplugins.reference.Reference;

import java.util.ArrayList;
import java.util.List;

import mcjty.theoneprobe.api.IProbeInfo;

public abstract class PluginBlank implements ITOPPlugin {

    @Override
    public String getID() {
        String pluginName;
        TOPPlugin annotation = this.getClass().getAnnotation(TOPPlugin.class);
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
