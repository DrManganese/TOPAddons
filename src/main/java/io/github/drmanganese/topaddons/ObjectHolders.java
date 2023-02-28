package io.github.drmanganese.topaddons;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ObjectHolder;

public final class ObjectHolders {

    public static final class BloodMagic {
        @ObjectHolder(registryName = "item", value = "bloodmagic:seersigil")
        public static Item SEER_SIGIL;
        @ObjectHolder(registryName = "item", value = "bloodmagic:divinationsigil")
        public static Item DIVINATION_SIGIL;
    }

    public static final class IndustrialForegoing {
        @ObjectHolder(registryName = "item", value = "industrialforegoing:mob_imprisonment_tool")
        public static Item MOB_IMPRISONMENT_TOOL;
    }
}
