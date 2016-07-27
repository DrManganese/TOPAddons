package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import io.github.drmanganese.topaddons.Config;
import io.github.drmanganese.topaddons.TOPRegistrar;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.elements.bloodmagic.ElementNodeFilter;

import WayofTime.bloodmagic.api.altar.IBloodAltar;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import WayofTime.bloodmagic.item.sigil.ItemSigilDivination;
import WayofTime.bloodmagic.routing.IMasterRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileFilteredRoutingNode;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

@TOPAddon(dependency = "BloodMagic")
public class AddonBloodMagic extends AddonBlank {

    public static int ELEMENT_NODE_FILTER;

    @Override
    public void registerElements() {
        ELEMENT_NODE_FILTER = TOPRegistrar.GetTheOneProbe.probe.registerElementFactory(ElementNodeFilter::new);
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());
        boolean holdingSigil = isDivinationSigil(player.getHeldItem(EnumHand.MAIN_HAND)) || isDivinationSigil(player.getHeldItem(EnumHand.OFF_HAND));
        if (tile != null) {
            if (tile instanceof IBloodAltar && (!Config.BloodMagic.requireSigil || holdingSigil)) {
                IBloodAltar altar = (IBloodAltar) tile;
                textPrefixed(probeInfo, "Tier", altar.getTier().toInt() + "", TextFormatting.RED);
                AddonForge.addTankElement(probeInfo, "Blood Altar", "Life Essence", altar.getCurrentBlood(), altar.getCapacity(), BlockLifeEssence.getLifeEssence().getColor(), mode);
            }

            if (tile instanceof TileFilteredRoutingNode && !(tile instanceof IMasterRoutingNode)) {
                TileFilteredRoutingNode node = (TileFilteredRoutingNode) tile;
                ItemStack filterStack = node.getFilterStack(data.getSideHit());
                if (filterStack != null) {
//                    ItemInventory filterInv = new ItemInventory(filterStack, 9, "");
//                    probeInfo.horizontal(new LayoutStyle().spacing(-4))
//                            .item(filterStack)
//                            .item(filterInv.getStackInSlot(0))
//                            .item(filterInv.getStackInSlot(1))
//                            .item(filterInv.getStackInSlot(2))
//                            .item(filterInv.getStackInSlot(3))
//                            .item(filterInv.getStackInSlot(4))
//                            .item(filterInv.getStackInSlot(5))
//                            .item(filterInv.getStackInSlot(6))
//                            .item(filterInv.getStackInSlot(7))
//                            .item(filterInv.getStackInSlot(8));
                    BlockPos sidePos = data.getPos().offset(data.getSideHit());
                    if (world.getTileEntity(sidePos) != null) {
                        IBlockState sideState = world.getBlockState(sidePos);
                        ItemStack inventoryOnSide = sideState.getBlock().getPickBlock(sideState, new RayTraceResult(data.getHitVec(), data.getSideHit().getOpposite(), sidePos), world, sidePos, player);
                        addFilterElement(probeInfo, data.getSideHit().getName(), inventoryOnSide, filterStack);
                    }
                }
            }
        }
    }

    private void addFilterElement(IProbeInfo probeInfo, String side, ItemStack inventoryOnSide, ItemStack filterStack) {
        probeInfo.element(new ElementNodeFilter(side, inventoryOnSide, filterStack));
    }

    private boolean isDivinationSigil(ItemStack heldStack) {
        return heldStack != null && heldStack.getItem() instanceof ItemSigilDivination;
    }
}
