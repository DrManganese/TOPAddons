package io.github.drmanganese.topaddons.addons.bloodmagic.tiles;

import io.github.drmanganese.topaddons.addons.bloodmagic.BloodMagicAddon;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.elements.bloodmagic.BloodAltarProgressElement;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import wayoftime.bloodmagic.altar.BloodAltar;
import wayoftime.bloodmagic.common.item.BloodOrb;
import wayoftime.bloodmagic.common.item.IBindable;
import wayoftime.bloodmagic.common.item.IBloodOrb;
import wayoftime.bloodmagic.common.tile.TileAltar;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.recipe.RecipeBloodAltar;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Optional;

public class TileAltarInfo implements ITileInfo<TileAltar> {

    private static Field bloodAltarField;
    private static Field recipeField;

    static {
        try {
            bloodAltarField = TileAltar.class.getDeclaredField("bloodAltar");
            bloodAltarField.setAccessible(true);
            recipeField = BloodAltar.class.getDeclaredField("recipe");
            recipeField.setAccessible(true);
        } catch (final NoSuchFieldException ignored) {
        }
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull TileAltar tile) {
        // Tier
        probeInfo.text(CompoundText.createLabelInfo("{*topaddons.bloodmagic:tier*}: ", tile.getTier()));

        if (BloodMagicAddon.altarsRequireSigil.get() && !BloodMagicAddon.isHoldingSigil(player)) return;

        final ItemStack inputStack = tile.getItem(0);
        if (inputStack.isEmpty()) return;

        if (inputStack.getItem() instanceof IBloodOrb) {
            // Orb
            final Binding binding = ((IBindable) inputStack.getItem()).getBinding(inputStack);
            if (binding != null) {
                final SoulNetwork network = NetworkHelper.getSoulNetwork(binding.getOwnerId());
                final BloodOrb orb = ((IBloodOrb) inputStack.getItem()).getOrb(inputStack);
                addAltarCraftingElement(
                    probeInfo,
                    network.getCurrentEssence(),
                    orb.getCapacity(),
                    inputStack,
                    ItemStack.EMPTY,
                    0);
            }
        } else if (tile.isActive()) {
            // Crafting
            final Optional<BloodAltar> bloodAltar = getBloodAltar(tile);
            final Optional<RecipeBloodAltar> recipe = bloodAltar.flatMap(TileAltarInfo::getRecipe);
            recipe.map(RecipeBloodAltar::getOutput).ifPresent(
                outputStack -> addAltarCraftingElement(
                    probeInfo,
                    tile.getProgress(),
                    tile.getLiquidRequired() * inputStack.getCount(),
                    inputStack,
                    outputStack,
                    tile.getConsumptionRate()
                )
            );
        }
    }

    private static void addAltarCraftingElement(IProbeInfo probeInfo, int progress, int maxProgress, ItemStack inputStack, ItemStack outputStack, float consumption) {
        probeInfo.element(new BloodAltarProgressElement(progress, maxProgress, inputStack, outputStack, consumption));
    }

    private static Optional<BloodAltar> getBloodAltar(TileAltar tile) {
        return Optional.ofNullable(bloodAltarField).flatMap(field -> {
            try {
                return Optional.of((BloodAltar) field.get(tile));
            } catch (final IllegalAccessException ignored) {
                return Optional.empty();
            }
        });

    }

    private static Optional<RecipeBloodAltar> getRecipe(BloodAltar bloodAltar) {
        return Optional.ofNullable(recipeField).flatMap(field -> {
            try {
                return Optional.ofNullable((RecipeBloodAltar) field.get(bloodAltar));
            } catch (final IllegalAccessException ignored) {
                return Optional.empty();
            }
        });

    }
}