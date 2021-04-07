package io.github.drmanganese.topaddons.addons.bloodmagic;

import io.github.drmanganese.topaddons.addons.TopAddon;
import io.github.drmanganese.topaddons.addons.bloodmagic.tiles.TileAlchemicalReactorInfo;
import io.github.drmanganese.topaddons.addons.bloodmagic.tiles.TileAltarInfo;
import io.github.drmanganese.topaddons.addons.bloodmagic.tiles.TileIncenseAltarInfo;
import io.github.drmanganese.topaddons.api.*;
import io.github.drmanganese.topaddons.capabilities.ElementSync;
import io.github.drmanganese.topaddons.elements.bloodmagic.BloodAltarProgressElement;
import io.github.drmanganese.topaddons.util.PlayerHelper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ObjectHolder;

import com.google.common.collect.ImmutableMultimap;
import mcjty.theoneprobe.api.ITheOneProbe;
import wayoftime.bloodmagic.tile.TileAlchemicalReactionChamber;
import wayoftime.bloodmagic.tile.TileAltar;
import wayoftime.bloodmagic.tile.TileIncenseAltar;

import javax.annotation.Nonnull;

public class BloodMagicAddon extends TopAddon implements IAddonBlocks, IAddonElements, IAddonConfig {

    public static final String PROGRESS_ELEMENT_ID = "bm_altar_progress";

    @ObjectHolder("bloodmagic:seersigil")
    private static Item SEER_SIGIL;
    @ObjectHolder("bloodmagic:divinationsigil")
    private static Item DIVINATION_SIGIL;

    private static final ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> TILES = ImmutableMultimap.of(
        TileAltar.class, new TileAltarInfo(),
        TileIncenseAltar.class, new TileIncenseAltarInfo(),
        TileAlchemicalReactionChamber.class, new TileAlchemicalReactorInfo()
    );

    public static ForgeConfigSpec.BooleanValue altarsRequireSigil;

    public BloodMagicAddon() {
        super("bloodmagic");
    }

    public static boolean isHoldingSigil(PlayerEntity player) {
        return PlayerHelper.isPlayerHolding(player, SEER_SIGIL) || PlayerHelper.isPlayerHolding(player, DIVINATION_SIGIL);
    }

    @Nonnull
    @Override
    public ImmutableMultimap<Class<? extends TileEntity>, ITileInfo> getTileInfos() {
        return TILES;
    }

    @Override
    public void registerElements(ITheOneProbe probe) {
        ElementSync.registerElement(probe, PROGRESS_ELEMENT_ID, BloodAltarProgressElement::new);
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder, ModConfig.Type type) {
        if (type == ModConfig.Type.COMMON) {
            builder.push(this.name);
            altarsRequireSigil = builder.comment("Seeing blood/incense altar info requires holding a divination or seer sigil in any hand").define("altarsRequireSigil", false);
            builder.pop();
        }
    }
}
