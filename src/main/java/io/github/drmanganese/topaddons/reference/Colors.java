package io.github.drmanganese.topaddons.reference;

import net.minecraftforge.fluids.Fluid;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class Colors {

    public static final Map<Fluid, Integer> fluidColorMap = new HashMap<>();
    public static final Map<String, Integer> fluidNameColorMap = new HashMap<>();

    static {
        /** Using Color here to preview in IDE */
        /** IC² */
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
    }
}
