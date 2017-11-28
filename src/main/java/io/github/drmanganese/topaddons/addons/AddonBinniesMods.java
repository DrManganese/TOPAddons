package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import io.github.drmanganese.topaddons.addons.subaddons.binniesmods.SubAddonBotany;
import io.github.drmanganese.topaddons.addons.subaddons.binniesmods.SubAddonGenetics;
import io.github.drmanganese.topaddons.addons.subaddons.binniesmods.SubAddonTrees;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.elements.binnies.ElementFlowerColor;
import io.github.drmanganese.topaddons.reference.Colors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import binnie.genetics.item.GeneticLiquid;
import mcjty.theoneprobe.api.IBlockDisplayOverride;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

@TOPAddon(dependency = "binniecore", fancyName = "Binnie's Mods")
public class AddonBinniesMods extends AddonBlank {

    private final Set<AddonBlank> SUB_ADDONS = new HashSet<>();

    public AddonBinniesMods() {
        if (Loader.isModLoaded("botany")) {
            SUB_ADDONS.add(new SubAddonBotany());
        }

        if (Loader.isModLoaded("genetics")) {
            SUB_ADDONS.add(new SubAddonGenetics());
        }

        if (Loader.isModLoaded("extratrees")) {
            SUB_ADDONS.add(new SubAddonTrees());
        }
    }

    @Override
    public void addFluidColors() {
        for (GeneticLiquid geneticLiquid : GeneticLiquid.values()) {
            Colors.FLUID_NAME_COLOR_MAP.put(geneticLiquid.getType().getIdentifier(),
                    geneticLiquid.getType().getContainerColor() + 0xff000000);
        }
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        for (AddonBlank subAddon : SUB_ADDONS) {
            subAddon.addProbeInfo(mode, probeInfo, player, world, blockState, data);
        }
    }

    @Override
    public List<IBlockDisplayOverride> getBlockDisplayOverrides() {
        List<IBlockDisplayOverride> list = new ArrayList<>();
        SUB_ADDONS.stream().map(AddonBlank::getBlockDisplayOverrides).forEach(list::addAll);
        return list;
    }

    @Override
    public void registerElements() {
        registerElement("flowerColor", ElementFlowerColor::new);
    }
}
