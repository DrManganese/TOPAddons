package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.elements.moofluids.ElementFluidCowEntity;

import java.util.Collections;
import java.util.List;

import com.robrit.moofluids.common.entity.EntityFluidCow;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IEntityDisplayOverride;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.Config;
import static mcjty.theoneprobe.api.TextStyleClass.MODNAME;
import static mcjty.theoneprobe.api.TextStyleClass.NAME;

@TOPAddon(dependency = "moofluids")
public class AddonMooFluids extends AddonBlank {

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    }


    @Override
    public List<IEntityDisplayOverride> getEntityDisplayOverrides() {
        return Collections.singletonList((mode, probeInfo, player, world, entity, data) -> {
            if (entity instanceof EntityFluidCow) {
                if (Tools.show(mode, Config.getRealConfig().getShowModName())) {
                    probeInfo.horizontal()
                            .element(new ElementFluidCowEntity(getElementId(player, "moo_fluid_cow"), ((EntityFluidCow) entity).getEntityFluid()))
                            .vertical()
                            .text(NAME + entity.getDisplayName().getFormattedText())
                            .text(MODNAME + Tools.getModName(entity));
                } else {
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .entity(entity)
                            .text(NAME + entity.getDisplayName().getFormattedText());
                }
                return true;
            }

            return false;
        });
    }

    @Override
    public void registerElements() {
        registerElement("moo_fluid_cow", ElementFluidCowEntity::new);
    }
}
