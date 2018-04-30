package io.github.drmanganese.topaddons.addons;

import io.github.drmanganese.topaddons.Util;
import io.github.drmanganese.topaddons.api.TOPAddon;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import com.lycanitesmobs.ExtendedPlayer;
import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.core.entity.EntityCreatureBase;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.ElementInfo;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IEntityDisplayOverride;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@TOPAddon(dependency = "lycanitesmobs")
public class AddonLycanitesMobs extends AddonBlank {

    @ObjectHolder("lycanitesmobs:soulgazer")
    private static final Item GAZER = null;
    private boolean requireGazer = false;

    @Override
    public void updateConfigs(Configuration config) {
        requireGazer = config.get("lycanitesmobs", "requireGazer", false, "Is holding a Soulgazer required to see certain information.").setLanguageKey("topaddons.config:lycanitesmobs_gazer").getBoolean();
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        //
    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        if (entity instanceof EntityCreatureBase) {
            final EntityCreatureBase creature = (EntityCreatureBase) entity;
            final ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(player);
            final CreatureInfo info = creature.creatureInfo;

            //Beastiary collection status
            if (extendedPlayer.getBeastiary().creatureKnowledgeList.containsKey(info.getName())) {
                probeInfo.horizontal(new LayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .icon(new ResourceLocation("theoneprobe", "textures/gui/icons.png"), 0, 16, 16, 16, probeInfo.defaultIconStyle().width(18).height(14).textureWidth(32).textureHeight(32))
                        .text(TextStyleClass.OK + "{*topaddons.lycanites:discovered*}");

            } else {
                probeInfo.horizontal(new LayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .icon(new ResourceLocation("theoneprobe", "textures/gui/icons.png"), 16, 16, 16, 16, probeInfo.defaultIconStyle().width(18).height(14).textureWidth(32).textureHeight(32))
                        .text(TextStyleClass.WARNING + "{*topaddons.lycanites:undiscovered*} ({*" + GAZER.getUnlocalizedName() + ".name*})");
            }

            //Taming and summoning info
            if (!requireGazer || Util.isHoldingItem(player, GAZER)) {
                if (creature.getOwner() == null) {
                    if (info.isTameable()) {
                        ItemStack stack = new ItemStack(ObjectManager.getItem(info.getName() + "treat"));
                        probeInfo.horizontal(new LayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(TextStyleClass.LABEL + "{*topaddons.lycanites:tameable*}: ").item(stack).text(stack.getDisplayName());
                    } else if (info.isSummonable()) {
                        probeInfo.text(TextStyleClass.LABEL + "{*topaddons.lycanites:summonable*}");
                    }
                } else {
                    if (extendedPlayer.petManager.hasEntry(creature.petEntry)) {
                        probeInfo.text(TextStyleClass.LABEL + "{*topaddons.lycanites:soulbound*}");
                    }
                }
            }

            //Elemental type on non-elemental creatures
            if (mode == ProbeMode.EXTENDED && !creature.getClass().getPackage().getName().equals("com.lycanitesmobs.elementalmobs.entity")) {
                textPrefixed(probeInfo, "{*creature.stat.element*}", getElementString(info));
            }
        }
    }

    @Override
    public List<IEntityDisplayOverride> getEntityDisplayOverrides() {
        //Add Elemental type to display for Elemental creatures
        return Collections.singletonList(((mode, probeInfo, player, world, entity, data) -> {
            if (entity instanceof EntityCreatureBase && entity.getClass().getPackage().getName().equals("com.lycanitesmobs.elementalmobs.entity")) {
                CreatureInfo info = ((EntityCreatureBase) entity).creatureInfo;

                if (Tools.show(mode, mcjty.theoneprobe.config.Config.getRealConfig().getShowModName())) {
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .entity(entity)
                            .vertical()
                            .text(entity.getDisplayName().getFormattedText())
                            .text(TextFormatting.DARK_PURPLE.toString() + TextFormatting.ITALIC + getElementString(info) + " Elemental")
                            .text(TextStyleClass.MODNAME + Tools.getModName(entity));
                } else {
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .entity(entity)
                            .text(entity.getDisplayName().getFormattedText())
                            .text(TextStyleClass.LABEL + getElementString(info) + " Elemental");
                }
                return true;
            }

            return false;
        }));
    }

    /**
     * Get a string representation of the creature's elements. Seperated by "/" if more than one.
     * @param info The creature info
     * @return Slash-separated string of elements
     */
    private String getElementString(@Nonnull CreatureInfo info) {
        return info.elements.stream().map(ElementInfo::getTitle).collect(Collectors.joining("/"));
    }
}
