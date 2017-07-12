package io.github.drmanganese.topaddons.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.reference.Reference;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class TOPAddonsGuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new TOPAddonsConfigGuiScreen(parentScreen);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    public static class TOPAddonsConfigGuiScreen extends GuiConfig {

        public TOPAddonsConfigGuiScreen(GuiScreen parent) {
            super(parent, getConfigElements(), Reference.MOD_ID, false, false, I18n.format("topaddons.config:title"));
        }

        private static List<IConfigElement> getConfigElements() {
            List<IConfigElement> categories = new ArrayList<>();
            List<IConfigElement> subCategories = Lists.newArrayList();

            categories.add(new DummyConfigElement.DummyCategoryElement("topaddonsClientCfg", "topaddons.config:category_client", ClientEntry.class));
            categories.add(new DummyConfigElement.DummyCategoryElement("topaddonsServerCfg", "topaddons.config:category_server", subCategories));
            for (String s : TOPAddons.config.getCategoryNames()) {
                subCategories.add(new DummyConfigElement.DummyCategoryElement("topaddonsServerCfg" + s, "topaddons.config:category_server_" + s, new ConfigElement(TOPAddons.config.getCategory(s)).getChildElements()));

            }
            return categories;
        }

        public static class ClientEntry extends GuiConfigEntries.CategoryEntry {

            public ClientEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
                super(owningScreen, owningEntryList, configElement);
            }

            @Override
            protected GuiScreen buildChildScreen() {
                return new GuiConfig(this.owningScreen,
                        new ConfigElement(TOPAddons.configClient.getCategory("Client Options")).getChildElements(),
                        Reference.MOD_ID,
                        false, false, GuiConfig.getAbridgedConfigPath(TOPAddons.configClient.toString()));
            }
        }
    }
}
