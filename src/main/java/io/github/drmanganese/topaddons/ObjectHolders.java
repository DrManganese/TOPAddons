package io.github.drmanganese.topaddons;

import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;

public final class ObjectHolders {

    public static final class BloodMagic {
        @ObjectHolder("bloodmagic:seersigil")
        public static Item SEER_SIGIL;
        @ObjectHolder("bloodmagic:divinationsigil")
        public static Item DIVINATION_SIGIL;
    }

    public static final class IndustrialForegoing {
        @ObjectHolder("industrialforegoing:mob_imprisonment_tool")
        public static Item MOB_IMPRISONMENT_TOOL;
    }
}
