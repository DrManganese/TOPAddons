package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.reference.EnumChip;

import java.util.HashMap;
import java.util.Map;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumHelm;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelHelm;
import vazkii.botania.common.item.equipment.armor.manaweave.ItemManaweaveHelm;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;

@TOPAddon(dependency = "Botania")
public class AddonBotania extends AddonBlank {

    @Override
    public Map<Class<? extends ItemArmor>, EnumChip> getSpecialHelmets() {
        Map<Class<? extends ItemArmor>, EnumChip> map = new HashMap<>();
        map.put(ItemElementiumHelm.class, EnumChip.EYE_RIGHT);
        map.put(ItemManasteelHelm.class, EnumChip.EYE_RIGHT);
        map.put(ItemManaweaveHelm.class, EnumChip.STANDARD);
        map.put(ItemTerrasteelHelm.class, EnumChip.EYE_RIGHT);
        return map;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    }
}
