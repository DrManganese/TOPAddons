package io.github.drmanganese.topaddons.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.reference.Reference;

public class TOPAddonsConfigGUI extends GuiConfig {

    public TOPAddonsConfigGUI(GuiScreen parentScreen) {
        super(parentScreen,
                new ConfigElement(TOPAddons.configClient.getCategory(Configuration.CATEGORY_CLIENT)).getChildElements(),
                Reference.MOD_ID,
                false, false, GuiConfig.getAbridgedConfigPath(TOPAddons.configClient.toString()));
    }
}
