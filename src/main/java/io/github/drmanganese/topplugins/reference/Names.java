package io.github.drmanganese.topplugins.reference;

import java.util.HashMap;
import java.util.Map;

import forestry.core.tiles.ILiquidTankTile;
import forestry.energy.tiles.TileEngineBiogas;
import forestry.factory.tiles.TileStill;

public final class Names {

    public static Map<Class<? extends ILiquidTankTile>, String[]> tankNamesMap = new HashMap<>();

    static {
        tankNamesMap.put(TileEngineBiogas.class, new String[]{"Fuel", "Heating", "Burner"});
        tankNamesMap.put(TileStill.class, new String[]{"In", "Out"});
    }
}
