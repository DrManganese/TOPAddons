package io.github.drmanganese.topaddons.reference;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class Colors {

    public static final Map<Fluid, Integer> fluidColorMap = new HashMap<>();
    public static final Map<String, Integer> fluidNameColorMap = new HashMap<>();

    static {
        /* Using Color here to preview in IDE */
        /* IC² */
        fluidNameColorMap.put("ic2uu_matter", new Color(59, 11, 53).hashCode());
        fluidNameColorMap.put("ic2construction_foam", new Color(31, 31, 30).hashCode());
        fluidNameColorMap.put("ic2coolant", new Color(20, 80, 91).hashCode());
        fluidNameColorMap.put("ic2hot_coolant", new Color(145, 39, 43).hashCode());
        fluidNameColorMap.put("ic2pahoehoe_lava", new Color(113, 113, 113).hashCode());
        fluidNameColorMap.put("ic2biomass", new Color(48, 92, 32).hashCode());
        fluidNameColorMap.put("ic2biogas", new Color(163, 148, 73).hashCode());
        fluidNameColorMap.put("ic2distilled_water", new Color(54, 71, 177).hashCode());
        fluidNameColorMap.put("ic2superheated_steam", new Color(193, 201, 200).hashCode());
        fluidNameColorMap.put("ic2steam", new Color(181, 180, 181).hashCode());
        fluidNameColorMap.put("ic2hot_water", new Color(50, 179, 179).hashCode());
        fluidNameColorMap.put("ic2weed_ex", new Color(18, 49, 23).hashCode());
        fluidNameColorMap.put("ic2air", new Color(108, 100, 107).hashCode());
        /* Actually Additions */
        fluidNameColorMap.put("canolaoil", new Color(185, 161, 45).hashCode());
        fluidNameColorMap.put("refinedcanolaoil", new Color(81, 71, 26).hashCode());
        fluidNameColorMap.put("crystaloil", new Color(120, 60, 34).hashCode());
        fluidNameColorMap.put("empoweredoil", new Color(211, 60, 82).hashCode());
        /* Thermal Foundation */
        fluidNameColorMap.put("crude_oil", new Color(35, 35, 35).hashCode());
        fluidNameColorMap.put("redstone", new Color(138, 10, 9).hashCode());
        fluidNameColorMap.put("glowstone", new Color(198, 175, 7).hashCode());
        fluidNameColorMap.put("ender", new Color(17, 96, 94).hashCode());
        fluidNameColorMap.put("pyrotheum", new Color(221, 187, 30).hashCode());
        fluidNameColorMap.put("cryotheum", new Color(14, 190, 202).hashCode());
        fluidNameColorMap.put("aerotheum", new Color(120, 118, 84).hashCode());
        fluidNameColorMap.put("petrotheum", new Color(25, 19, 16).hashCode());
        fluidNameColorMap.put("mana", new Color(9, 82, 119).hashCode());
        fluidNameColorMap.put("steam", new Color(131, 133, 130).hashCode());
        fluidNameColorMap.put("creosote", new Color(81, 85, 2).hashCode());
        fluidNameColorMap.put("coal", new Color(57, 57, 57).hashCode());
        fluidNameColorMap.put("refined_oil", new Color(195, 106, 31).hashCode());
        fluidNameColorMap.put("refined_fuel", new Color(195, 165, 21).hashCode());
        fluidNameColorMap.put("sap", new Color(126, 82, 21).hashCode());
        fluidNameColorMap.put("syrup", new Color(134, 57, 3).hashCode());
        fluidNameColorMap.put("resin", new Color(181, 124, 4).hashCode());
        fluidNameColorMap.put("tree_oil", new Color(180, 150, 69).hashCode());
        fluidNameColorMap.put("mushroom_stew", new Color(200, 169, 143).hashCode());

    }

    public static int getHashFromFluid(FluidStack fluidStack) {
        /*
         * Fluid color: - If the fluid doesn't return white in getColor, use this value;
         *              - if the fluid is registered by an addon, use its color;
         *              - if the fluid's name is stored in {@link Colors.fluidNameColorMap}, use that value;
         *              - otherwise use 0xff777777 (gray-ish)
         */
        Fluid fluid = fluidStack.getFluid();
        if (fluid.getColor(fluidStack) != 0xffffffff) {
            return fluid.getColor(fluidStack);
        } else if (Colors.fluidColorMap.containsKey(fluid)) {
            return Colors.fluidColorMap.get(fluid);
        } else if (Colors.fluidNameColorMap.containsKey(fluidStack.getFluid().getName())) {
            return Colors.fluidNameColorMap.get(fluidStack.getFluid().getName());
        } else {
            return 0xff777777;
        }
    }

    public static int getHashFromFluid(IFluidTankProperties tank) {
        return getHashFromFluid(tank.getContents());
    }
}
