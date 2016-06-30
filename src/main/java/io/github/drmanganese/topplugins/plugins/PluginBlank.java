package io.github.drmanganese.topplugins.plugins;

import io.github.drmanganese.topplugins.api.ITOPPlugin;
import io.github.drmanganese.topplugins.api.TOPPlugin;
import io.github.drmanganese.topplugins.reference.Reference;

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

        return Reference.MOD_ID + ":" + pluginName;
    }
}
