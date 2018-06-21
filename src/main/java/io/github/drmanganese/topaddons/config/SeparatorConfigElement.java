package io.github.drmanganese.topaddons.config;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

public class SeparatorConfigElement extends ConfigElement {

    public SeparatorConfigElement(String category) {
        super(new Property(category, "", Property.Type.STRING));
    }

    @Override
    public Class<? extends GuiConfigEntries.IConfigEntry> getConfigEntryClass() {
        return SeparatorConfigEntry.class;
    }

    public static class SeparatorConfigEntry extends GuiConfigEntries.ListEntryBase {

        public SeparatorConfigEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
            super(owningScreen, owningEntryList, configElement);
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partial) {
            final int labelX = (this.owningEntryList.width - this.mc.fontRenderer.getStringWidth(getName())) / 2;
            this.mc.fontRenderer.drawString(
                    getName(),
                    labelX,
                    y + slotHeight / 2 - this.mc.fontRenderer.FONT_HEIGHT / 2,
                    0xffffffff);
        }

        @Override
        public boolean isDefault() {
            return false;
        }

        @Override
        public void setToDefault() {
        }

        @Override
        public void keyTyped(char eventChar, int eventKey) {
        }

        @Override
        public void updateCursorCounter() {
        }

        @Override
        public void mouseClicked(int x, int y, int mouseEvent) {
        }

        @Override
        public boolean isChanged() {
            return false;
        }

        @Override
        public void undoChanges() {

        }

        @Override
        public boolean saveConfigElement() {
            return false;
        }

        @Override
        public Object getCurrentValue() {
            return null;
        }

        @Override
        public Object[] getCurrentValues() {
            return new Object[0];
        }

    }
}
