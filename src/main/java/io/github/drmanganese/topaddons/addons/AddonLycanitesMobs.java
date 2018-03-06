package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import io.github.drmanganese.topaddons.api.TOPAddon;

import com.lycanitesmobs.ExtendedPlayer;
import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.core.entity.EntityCreatureBase;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.item.ItemSoulgazer;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;

@TOPAddon(dependency = "lycanitesmobs")
public class AddonLycanitesMobs extends AddonBlank {

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
            final ItemSoulgazer soulgazer = (ItemSoulgazer) ObjectManager.getItem("soulgazer");
            final EntityCreatureBase creature = (EntityCreatureBase) entity;
            final ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(player);
            final CreatureInfo info = creature.creatureInfo;

            if (extendedPlayer.getBeastiary().creatureKnowledgeList.containsKey(info.getName())) {
                probeInfo.horizontal(new LayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .icon(new ResourceLocation("theoneprobe", "textures/gui/icons.png"), 0, 16, 16, 16, probeInfo.defaultIconStyle().width(18).height(14).textureWidth(32).textureHeight(32))
                        .text(TextStyleClass.OK + "{*topaddons.lycanites:discovered*}");

            } else {
                probeInfo.horizontal(new LayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .icon(new ResourceLocation("theoneprobe", "textures/gui/icons.png"), 16, 16, 16, 16, probeInfo.defaultIconStyle().width(18).height(14).textureWidth(32).textureHeight(32))
                        .text(TextStyleClass.WARNING + "{*topaddons.lycanites:undiscovered*} ({*" + soulgazer.getUnlocalizedName() + ".name*})");
            }

            if (!requireGazer || player.getHeldItemMainhand().getItem() == ObjectManager.getItem("soulgazer") || player.getHeldItemOffhand().getItem() == ObjectManager.getItem("soulgazer")) {
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
        }
    }
}
