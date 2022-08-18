package io.github.drmanganese.topaddons.addons.bloodmagic;


import io.github.drmanganese.topaddons.ObjectHolders;
import io.github.drmanganese.topaddons.TopAddons;
import io.github.drmanganese.topaddons.addons.TopAddon;
import io.github.drmanganese.topaddons.addons.bloodmagic.tiles.TileAlchemicalReactorInfo;
import io.github.drmanganese.topaddons.addons.bloodmagic.tiles.TileAltarInfo;
import io.github.drmanganese.topaddons.addons.bloodmagic.tiles.TileIncenseAltarInfo;
import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.IAddonConfig;
import io.github.drmanganese.topaddons.api.IAddonElements;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.addons.bloodmagic.elements.BloodAltarProgressElement;
import io.github.drmanganese.topaddons.util.PlayerHelper;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import com.google.common.collect.ImmutableMultimap;
import mcjty.theoneprobe.api.IElement;
import org.apache.commons.lang3.tuple.Pair;
import wayoftime.bloodmagic.common.tile.TileAlchemicalReactionChamber;
import wayoftime.bloodmagic.common.tile.TileAltar;
import wayoftime.bloodmagic.common.tile.TileIncenseAltar;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class BloodMagicAddon extends TopAddon implements IAddonBlocks, IAddonElements, IAddonConfig {

    public static final ResourceLocation PROGRESS_ELEMENT_ID = new ResourceLocation(TopAddons.MOD_ID, "bloodmagic.altar_progress");

    private static final ImmutableMultimap<Class<? extends BlockEntity>, ITileInfo> TILES = ImmutableMultimap.of(
        TileAltar.class, new TileAltarInfo(),
        TileIncenseAltar.class, new TileIncenseAltarInfo(),
        TileAlchemicalReactionChamber.class, new TileAlchemicalReactorInfo()
    );

    public static ForgeConfigSpec.BooleanValue altarsRequireSigil;

    public BloodMagicAddon() {
        super("bloodmagic");
    }

    public static boolean isHoldingSigil(Player player) {
        return PlayerHelper.isPlayerHolding(player, ObjectHolders.BloodMagic.SEER_SIGIL) ||
            PlayerHelper.isPlayerHolding(player, ObjectHolders.BloodMagic.DIVINATION_SIGIL);
    }

    @Nonnull
    @Override
    public ImmutableMultimap<Class<? extends BlockEntity>, ITileInfo> getTileInfos() {
        return TILES;
    }

    @Override
    public List<Pair<ResourceLocation, Function<FriendlyByteBuf, IElement>>> getElements() {
        return Collections.singletonList(Pair.of(PROGRESS_ELEMENT_ID, BloodAltarProgressElement::new));
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