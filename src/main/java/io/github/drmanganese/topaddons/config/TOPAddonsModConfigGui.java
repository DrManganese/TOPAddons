package io.github.drmanganese.topaddons.config;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.reference.Reference;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class TOPAddonsModConfigGui extends GuiConfig {

    TOPAddonsModConfigGui(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(), Reference.MOD_ID, false, false, "TOP Addons Configuration");
    }

    private static List<IConfigElement> getConfigElements() {
        final List<IConfigElement> client_elements = new ArrayList<>();
        final List<IConfigElement> common_elements = new ArrayList<>();

        for (String name : TOPAddons.CONFIG.getCategoryNames()) {
            //Get every category's properties
            final List<Property> props = TOPAddons.CONFIG.getCategory(name).getOrderedValues();
            if (props.size() > 0) {
                //Create a ConfigElement for each property, if it's a color code, create a ColorConfigElement instead
                final List<IConfigElement> elements = props.stream()
                        .map(prop -> {
                            if (prop.getDefault().startsWith("#")) {
                                return new ColorConfigElement(prop);
                            } else {
                                return new ConfigElement(prop);
                            }
                        }).collect(Collectors.toList());

                //Categories are split into common and client, each addon's subcategory is separated by a separator element
                if (name.endsWith(".client")) {
                    client_elements.add(new SeparatorConfigElement(name));
                    client_elements.addAll(elements);
                } else {
                    common_elements.add(new SeparatorConfigElement(name));
                    common_elements.addAll(elements);
                }
            }
        }

        return Arrays.asList(
                new DummyConfigElement.DummyCategoryElement("Client", "topaddons.cfg:client", client_elements),
                new DummyConfigElement.DummyCategoryElement("Common", "topaddons.cfg:common", common_elements)
        );
    }
}
