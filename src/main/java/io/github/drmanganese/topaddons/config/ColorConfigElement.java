package io.github.drmanganese.topaddons.config;

import io.github.drmanganese.topaddons.util.ElementHelper;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

class ColorConfigElement extends ConfigElement {

    ColorConfigElement(Property property) {
        super(property);
    }

    @Override
    public Class<? extends GuiConfigEntries.IConfigEntry> getConfigEntryClass() {
        return ColorConfigEntry.class;
    }

    @Override
    public boolean requiresWorldRestart() {
        return false;
    }

    static class ColorConfigEntry extends GuiConfigEntries.StringEntry {

        ColorConfigEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
            super(owningScreen, owningEntryList, configElement);
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partial) {
            super.drawEntry(slotIndex, x, y, listWidth, slotHeight, mouseX, mouseY, isSelected, partial);

            final int colorX = textFieldValue.x + textFieldValue.width - 16;
            if (isValidValue) {
                ElementHelper.drawBox(colorX, y + 1, 16, 16, Long.decode(textFieldValue.getText()).intValue(), 0, -1);
            } else {
                ElementHelper.drawBox(colorX, y + 1, 16, 16, 0xffe400e4, 0, -1);
                ElementHelper.drawBox(colorX, y + 1, 8, 8, 0xff000000, 0, -1);
                ElementHelper.drawBox(colorX + 8, y + 9, 8, 8, 0xff000000, 0, -1);
            }
        }

        @Override
        public void keyTyped(char eventChar, int eventKey) {
            super.keyTyped(Character.toUpperCase(eventChar), eventKey);

        }
    }
}
