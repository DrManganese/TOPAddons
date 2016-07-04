package io.github.drmanganese.topaddons.reference;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import forestry.core.fluids.Fluids;

public final class Colors {

    public static final Map<Fluid, Color> fluidColorMap = new HashMap<>();

    static {
        //Forestry liquids
        for (Fluids fluid : Fluids.values()) {
            fluidColorMap.put(fluid.getFluid(), fluid.getParticleColor());
        }

        fluidColorMap.put(FluidRegistry.WATER, new Color(52, 95, 218));
        fluidColorMap.put(FluidRegistry.LAVA, new Color(230, 145, 60));
    }
}
