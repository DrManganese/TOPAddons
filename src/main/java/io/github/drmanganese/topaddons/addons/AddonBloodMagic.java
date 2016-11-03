package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import io.github.drmanganese.topaddons.TOPRegistrar;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.config.Config;
import io.github.drmanganese.topaddons.elements.bloodmagic.ElementAltarCrafting;
import io.github.drmanganese.topaddons.elements.bloodmagic.ElementNodeFilter;
import io.github.drmanganese.topaddons.reference.EnumChip;
import io.github.drmanganese.topaddons.reference.Names;

import java.util.HashMap;
import java.util.Map;

import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.api.altar.IBloodAltar;
import WayofTime.bloodmagic.api.iface.IAltarReader;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.item.armour.ItemSentientArmour;
import WayofTime.bloodmagic.item.sigil.ItemSigilHolding;
import WayofTime.bloodmagic.item.sigil.ItemSigilSeer;
import WayofTime.bloodmagic.routing.IMasterRoutingNode;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.tile.TileIncenseAltar;
import WayofTime.bloodmagic.tile.routing.TileFilteredRoutingNode;
import WayofTime.bloodmagic.util.helper.NumeralHelper;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

@TOPAddon(dependency = "BloodMagic")
public class AddonBloodMagic extends AddonBlank {

    public static int ELEMENT_NODE_FILTER;
    public static int ELEMENT_ALTAR_CRAFTING;

    @Override
    public boolean hasSpecialHelmets() {
        return true;
    }

    @Override
    public Map<Class<? extends ItemArmor>, EnumChip> getSpecialHelmets() {
        Map<Class<? extends ItemArmor>, EnumChip> map = new HashMap<>();
        map.put(ItemLivingArmour.class, EnumChip.STANDARD);
        map.put(ItemSentientArmour.class, EnumChip.STANDARD);
        return map;
    }

    @Override
    public void registerElements() {
        ELEMENT_NODE_FILTER = TOPRegistrar.GetTheOneProbe.probe.registerElementFactory(ElementNodeFilter::new);
        ELEMENT_ALTAR_CRAFTING = TOPRegistrar.GetTheOneProbe.probe.registerElementFactory(ElementAltarCrafting::new);
    }

    @Override
    public void addTankNames() {
        Names.tankNamesMap.put(TileAltar.class, new String[]{"Blood Altar"});
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        boolean holdingSigil = !Config.BloodMagic.requireSigil || isAltarSeer(player.getHeldItem(EnumHand.MAIN_HAND)) || isAltarSeer(player.getHeldItem(EnumHand.OFF_HAND));
        boolean holdingSeer = !Config.BloodMagic.requireSigil  || holdingSeer(player.getHeldItem(EnumHand.MAIN_HAND)) || holdingSeer(player.getHeldItem(EnumHand.OFF_HAND));

        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile != null) {
            if (tile instanceof IBloodAltar && holdingSigil) {
                IBloodAltar altar = (IBloodAltar) tile;
                textPrefixed(probeInfo, "Tier", NumeralHelper.toRoman(altar.getTier().toInt()), TextFormatting.RED);
                //AddonForge.addTankElement(probeInfo, "Blood Altar", "Life Essence", altar.getCurrentBlood(), altar.getCapacity(), "LP", BlockLifeEssence.getLifeEssence().getColor(), mode);

                if (altar instanceof TileAltar && altar.isActive() && holdingSeer) {
                    BloodAltar bloodAltar = ReflectionHelper.getPrivateValue(TileAltar.class, (TileAltar) altar, "bloodAltar");
                    ItemStack input = ((TileAltar) altar).getStackInSlot(0);
                    ItemStack result = ReflectionHelper.getPrivateValue(BloodAltar.class, bloodAltar, "result");
                    if (input != null && result != null)
                        addAltarCraftingElement(probeInfo, input, result, bloodAltar.getProgress(), bloodAltar.getLiquidRequired(), bloodAltar.getConsumptionRate());
                }
            }

            if (tile instanceof TileFilteredRoutingNode && !(tile instanceof IMasterRoutingNode)) {
                TileFilteredRoutingNode node = (TileFilteredRoutingNode) tile;
                ItemStack filterStack = node.getFilterStack(data.getSideHit());
                if (filterStack != null) {
                    BlockPos sidePos = data.getPos().offset(data.getSideHit());
                    if (world.getTileEntity(sidePos) != null) {
                        IBlockState sideState = world.getBlockState(sidePos);
                        ItemStack inventoryOnSide = sideState.getBlock().getPickBlock(sideState, new RayTraceResult(data.getHitVec(), data.getSideHit().getOpposite(), sidePos), world, sidePos, player);
                        addFilterElement(probeInfo, data.getSideHit().getName(), inventoryOnSide, filterStack);
                    }
                }
            }

            if (tile instanceof TileIncenseAltar && holdingSigil) {
                TileIncenseAltar altar = (TileIncenseAltar) tile;
                textPrefixed(probeInfo, "Tranquility", (int) ((100D * (int) (100 * altar.tranquility)) / 100D) + "", TextFormatting.RED);
                textPrefixed(probeInfo, "Bonus", (int) (altar.incenseAddition * 100) + "%", TextFormatting.RED);
            }
        }
    }

    private void addFilterElement(IProbeInfo probeInfo, String side, ItemStack inventoryOnSide, ItemStack filterStack) {
        probeInfo.element(new ElementNodeFilter(side, inventoryOnSide, filterStack));
    }

    private void addAltarCraftingElement(IProbeInfo probeInfo, ItemStack input, ItemStack result, int progress, int required, float consumption) {
        probeInfo.element(new ElementAltarCrafting(input, result, progress, required, consumption));
    }

    @SuppressWarnings("ConstantConditions")
    private boolean isAltarSeer(ItemStack heldStack) {
        if (heldStack == null)
            return false;

        if (heldStack.getItem() instanceof IAltarReader) {
            if (heldStack.getItem() instanceof ItemSigilHolding) {
                ItemStack currentHoldingStack = ItemSigilHolding.getItemStackInSlot(heldStack, (ItemSigilHolding.getCurrentItemOrdinal(heldStack)));
                return currentHoldingStack != null && currentHoldingStack.getItem() instanceof IAltarReader;
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean holdingSeer(ItemStack heldStack) {
        return heldStack != null && heldStack.getItem() instanceof ItemSigilSeer;
    }
}
