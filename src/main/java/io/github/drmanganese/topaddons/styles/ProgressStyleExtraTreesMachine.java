package io.github.drmanganese.topaddons.styles;


import binnie.core.machines.power.ComponentProcess;
import binnie.extratrees.machines.brewery.BreweryLogic;
import binnie.extratrees.machines.distillery.DistilleryLogic;
import binnie.extratrees.machines.fruitpress.FruitPressLogic;
import binnie.extratrees.machines.lumbermill.LumbermillLogic;
import com.google.common.collect.ImmutableMap;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;

public class ProgressStyleExtraTreesMachine extends ProgressStyle {

    private static final ImmutableMap<Class<? extends ComponentProcess>, int[]> COLORS = ImmutableMap.of(
            DistilleryLogic.class, new int[]{0x0f0f0f, 0xff351a1a, 0xff45231a},
            BreweryLogic.class, new int[]{0x4e4e4e, 0xff52462f, 0xffa38b5d},
            FruitPressLogic.class, new int[]{0x353535, 0xff351a1a, 0xff45231a},
            LumbermillLogic.class, new int[]{0x190000, 0xff4a1300, 0xff632200}
    );

    public ProgressStyleExtraTreesMachine(Class<? extends ComponentProcess> machine) {
        int[] colors = COLORS.get(machine);
        borderColor(colors[0] + 0xff000000);
        backgroundColor(colors[0] + 0x88000000);
        filledColor(colors[1]);
        alternateFilledColor(colors[2]);
    }


    @Override
    public String getSuffix() {
        return "%";
    }

    @Override
    public String getPrefix() {
        return "Progress: ";
    }
}


