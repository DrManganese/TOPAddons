package io.github.drmanganese.topaddons.reference;

import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;
import java.util.Map;

public final class Names {

    public static final Map<Class<? extends TileEntity>, String[]> tankNamesMap = new HashMap<>();

    public static final Map<String, Class> clientConfigOptions = new HashMap<>();

    static {
        clientConfigOptions.put("fluidGauge", Boolean.TYPE);
        clientConfigOptions.put("hideTOPTank", Boolean.TYPE);
        clientConfigOptions.put("forestryReasonCrouch", Boolean.TYPE);
        clientConfigOptions.put("showPitch", Boolean.TYPE);
        clientConfigOptions.put("ic2Progress", Boolean.TYPE);
    }
}
