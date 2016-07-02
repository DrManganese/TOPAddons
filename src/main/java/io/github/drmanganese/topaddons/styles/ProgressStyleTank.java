package io.github.drmanganese.topaddons.styles;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;

import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;

import static io.github.drmanganese.topaddons.reference.Colors.fluidColorMap;

public class ProgressStyleTank extends ProgressStyle {

    private String suffix = " mB";

    @Override
    public int getBorderColor() {
        return 0xff7f0000;
    }

    public ProgressStyle withFluid(FluidStack fluid) {
        suffix += " (" + fluid.getLocalizedName() + ")";

        if (fluidColorMap.containsKey(fluid.getFluid())) {
            Color color = fluidColorMap.get(fluid.getFluid());
            return filledColor(color.hashCode()).alternateFilledColor(color.darker().hashCode());
        } else {
            return this;
        }
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

    @Override
    public int getWidth() {
        return 35 + Minecraft.getMinecraft().fontRendererObj.getStringWidth(suffix);
    }
}
