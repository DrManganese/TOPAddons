package io.github.drmanganese.topaddons.reference;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import java.util.HashMap;
import java.util.Map;

public final class Colors {

    public static final Map<String, Integer> FLUID_NAME_COLOR_MAP = new HashMap<>();

    static {
        /* Actually Additions */
        FLUID_NAME_COLOR_MAP.put("canolaoil", 0xffb9a12d);
        FLUID_NAME_COLOR_MAP.put("refinedcanolaoil", 0xff51471a);
        FLUID_NAME_COLOR_MAP.put("crystaloil", 0xff783c22);
        FLUID_NAME_COLOR_MAP.put("empoweredoil", 0xffd33c52);
    }

    public static int getHashFromFluid(FluidStack fluidStack) {
        /*
         * Fluid color: - If the fluid doesn't return white in getColor, use this value;
         *              - if the fluid's name is stored in {@link Colors.FLUID_NAME_COLOR_MAP}, use that value;
         *              - otherwise use 0xff777777 (gray-ish)
         */
        Fluid fluid = fluidStack.getFluid();
        if (fluid.getColor(fluidStack) != 0xffffffff) {
            return fluid.getColor(fluidStack);
        } else {
            return FLUID_NAME_COLOR_MAP.getOrDefault(fluidStack.getFluid().getName(), 0xff777777);
        }
    }

    public static int getHashFromFluid(IFluidTankProperties tank) {
        return getHashFromFluid(tank.getContents());
    }
}
