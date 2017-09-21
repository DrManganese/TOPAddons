package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.Util;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.styles.ProgressStyleTOPAddonGrey;

import com.google.common.collect.Lists;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import li.cil.oc.api.Items;
import li.cil.oc.api.machine.Machine;
import li.cil.oc.api.network.Component;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.common.block.SimpleBlock;
import li.cil.oc.common.entity.Drone;
import li.cil.oc.common.tileentity.Assembler;
import li.cil.oc.common.tileentity.Charger;
import li.cil.oc.common.tileentity.Printer;
import li.cil.oc.common.tileentity.Rack;
import li.cil.oc.common.tileentity.Raid;
import li.cil.oc.common.tileentity.Transposer;
import li.cil.oc.common.tileentity.Waypoint;
import li.cil.oc.common.tileentity.traits.Environment;
import li.cil.oc.common.tileentity.Disassembler;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IBlockDisplayOverride;
import mcjty.theoneprobe.api.IEntityDisplayOverride;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.config.Config;

import static mcjty.theoneprobe.api.TextStyleClass.MODNAME;
import static mcjty.theoneprobe.api.TextStyleClass.PROGRESS;

@TOPAddon(dependency = "opencomputers")
public class AddonOpenComputers extends AddonBlank {

    private final int[][] CMYK = {{0xff00ffff, 0xff00aaaa}, {0xffff00ff, 0xffaa00aa}, {0xffffff00, 0xffaaaa00}, {0xff555555, 0xff000000}};

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        boolean analyzer = holdingAnalyzer(player);

        TileEntity tile = world.getTileEntity(data.getPos());

        if (tile instanceof Environment) {
            if (((Environment) tile).node() != null) {

                if (tile instanceof Assembler) {
                    Assembler assembler = (Assembler) tile;
                    if (assembler.isAssembling()) {
                        progressBar(probeInfo, (int) assembler.progress(), 0xff66cc66, 0xff478e47);
                    }
                }

                if (tile instanceof Disassembler) {
                    Disassembler disassembler = (Disassembler) tile;
                    if (!disassembler.isEmpty()) {
                        progressBar(probeInfo, (int) disassembler.progress(), 0xffb20000, 0xffe50404);
                    }
                }

                if (tile instanceof Charger) {
                    textPrefixed(probeInfo, "{*topaddons.opencomputers:charge_speed*}", Double.toString(Math.round(100 * ((Charger) tile).chargeSpeed()) / 100D));
                }

                if (((Environment) tile).node().reachability() != Visibility.None) {
                    textPrefixed(probeInfo, "{*option.oc.address*}", analyzer ? ((Environment) tile).node().address() : ((Environment) tile).node().address().substring(0, 8));
                }

                if (tile instanceof Component) {
                    textPrefixed(probeInfo, "{*option.oc.componentName*}", ((Component) tile).name());
                }

                if (tile instanceof Machine) {
                    Machine machine = (Machine) tile;
                    textPrefixed(probeInfo, "{*topaddons.opencomputers:connected_components*}", machine.componentCount() + "/" + machine.maxComponents());
                    if (machine.lastError() != null) {
                        textPrefixed(probeInfo, "{*topaddons.opencomputers:last_error*}", "{*oc:" + machine.lastError() + "*}", TextStyleClass.ERROR);
                    }
                }
            }
        }

        if (tile instanceof Printer) {
            Printer printer = (Printer) tile;
            final int[] colours = CMYK[(int) (4 * (world.getWorldTime() % 100) / 100)];
            probeInfo.progress(printer.amountMaterial(), printer.maxAmountMaterial(), new ProgressStyleTOPAddonGrey().filledColor(0xffdba6a6).alternateFilledColor(0xffd08c8c).prefix("Material: "));
            probeInfo.progress(printer.amountInk(), printer.maxAmountInk(), new ProgressStyleTOPAddonGrey().filledColor(colours[0]).alternateFilledColor(colours[1]).prefix("Ink: "));

            if (printer.isPrinting()) {
                IProbeInfo hori = probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));
                if (printer.data().stateOff().nonEmpty()) {
                    hori.item(printer.data().createItemStack());
                }
                progressBar(hori, (int) printer.progress(), 0xff66cc66, 0xff478e47);
            }
        }

        if (tile instanceof Waypoint) {
            textPrefixed(probeInfo, "{*topaddons.opencomputers:label*}", ((Waypoint) tile).label());
        }

        if (tile instanceof Raid) {
            Raid raid = (Raid) tile;
            if (!raid.isEmpty()) {
                textPrefixed(probeInfo, "{*topaddons.opencomputers:raid_space*}", raid.filesystem().get().fileSystem().spaceUsed() + "/" + raid.filesystem().get().fileSystem().spaceTotal() + " bytes");
            }
        }

        if (tile instanceof Transposer) {
            textPrefixed(probeInfo, "{*topaddons.opencomputers:side*}", data.getSideHit().getName() + " (" + data.getSideHit().getIndex() + ')');
        }

        if (tile instanceof Rack) {
            Rack rack = (Rack) tile;
            if (!rack.isEmpty() && rack.facing() == data.getSideHit()) {
                int pixelY = (int) (8 - 16 * (data.getHitVec().y - rack.yPosition()));
                if (pixelY >= 2 && pixelY < 14) {
                    final int serverIndex;
                    if (pixelY < 5) {
                        serverIndex = 0;
                    } else if (pixelY < 8) {
                        serverIndex = 1;
                    } else if (pixelY < 11) {
                        serverIndex = 2;
                    } else {
                        serverIndex = 3;
                    }

                    final li.cil.oc.api.network.Environment server = rack.getMountable(serverIndex);
                    if (server != null) {
                        IProbeInfo vert = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xff448844));
                        vert.text(TextStyleClass.INFO + "{*topaddons.opencomputers:server*} " + serverIndex);
                        textPrefixed(vert, "{*option.oc.address*}", analyzer ? server.node().address() : server.node().address().substring(0, 8));

                        if (server.node().host() instanceof Machine) {
                            Machine machine = (Machine) server.node().host();
                            textPrefixed(vert, "{*topaddons.opencomputers:connected_components*}", machine.componentCount() + "/" + machine.maxComponents());
                            if (machine.lastError() != null) {
                                textPrefixed(vert, "{*topaddons.opencomputers:last_error*}", "{*oc:" + machine.lastError() + "*}", TextStyleClass.ERROR);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        if (entity instanceof Drone) {
            Drone drone = (Drone) entity;

            if (drone.node() != null) {
                Node node = ((Drone) entity).node();

                if (Config.getRealConfig().getRFMode() == 1) {
                    probeInfo.progress(drone.globalBuffer(), drone.globalBufferSize(),
                            probeInfo.defaultProgressStyle()
                                    .suffix("RF")
                                    .filledColor(Config.rfbarFilledColor)
                                    .alternateFilledColor(Config.rfbarAlternateFilledColor)
                                    .borderColor(Config.rfbarBorderColor)
                                    .numberFormat(Config.rfFormat));
                } else {
                    probeInfo.text(PROGRESS + "RF: " + ElementProgress.format(drone.globalBuffer(), Config.rfFormat, "RF"));
                }

                textPrefixed(probeInfo, "{*option.oc.address*}", holdingAnalyzer(player) ? node.address() : node.address().substring(0, 8));

                if (drone.control().tank().tankCount() > 0) {
                    for (int i = 0; i < drone.control().tank().tankCount(); i++) {
                        AddonForge.addTankElement(probeInfo, "Tank " + (i + 1), drone.control().tank().getFluidTank(i).getInfo(), mode, player);
                    }
                }

                if (mode == ProbeMode.EXTENDED && drone.control().inventory().getSizeInventory() > 0) {
                    IInventory inventory = drone.control().inventory();
                    List<ItemStack> stacks = new ArrayList<>();
                    for (int i = 0; i < inventory.getSizeInventory(); i++) {
                        Util.mergeItemStack(inventory.getStackInSlot(i), stacks);
                    }

                    if (stacks.size() > 0) {
                        IProbeInfo hori = probeInfo.horizontal(probeInfo.defaultLayoutStyle().borderColor(Config.chestContentsBorderColor));
                        int i = 0;
                        for (ItemStack stack : stacks) {
                            if (i == drone.control().selectedSlot()) {
                                int color = new Color(173, 255, 173, 255 * ((int) world.getWorldTime() % 20) / 20).hashCode();
                                hori.horizontal(probeInfo.defaultLayoutStyle().borderColor(color)).item(stack, probeInfo.defaultItemStyle().width(16).height(16));
                            } else {
                                hori.item(stack);
                            }
                            i++;
                        }
                    }
                }

                Machine machine = (Machine) node.host();
                if (machine.lastError() != null) {
                    textPrefixed(probeInfo, "{*topaddons.opencomputers:last_error*}", "{*oc:" + machine.lastError() + "*}", TextStyleClass.ERROR);
                }
            }

            if (mode == ProbeMode.EXTENDED) {
                if (player.getCapability(TOPAddons.OPTS_CAP, null).getBoolean("ocFullComponentNames")) {
                    for (ItemStack component : drone.internalComponents()) {
                        probeInfo.text('-' + component.getDisplayName());
                    }
                } else {
                    showItemStackRows(probeInfo, Lists.newArrayList(drone.internalComponents()), 5);
                }
            }
        }
    }

    @Override
    public List<IBlockDisplayOverride> getBlockDisplayOverrides() {
        return Collections.singletonList((mode, probeInfo, player, world, blockState, data) -> {
            if (blockState.getBlock() instanceof SimpleBlock) {
                if (Tools.show(mode, Config.getRealConfig().getShowModName())) {
                    probeInfo.horizontal()
                            .item(data.getPickBlock())
                            .vertical()
                            .text(data.getPickBlock().getRarity().rarityColor + data.getPickBlock().getDisplayName())
                            .text(MODNAME + Tools.getModName(((ItemBlock) data.getPickBlock().getItem()).getBlock()));
                } else {
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .item(data.getPickBlock())
                            .text(data.getPickBlock().getRarity().rarityColor + data.getPickBlock().getDisplayName());
                }

                return true;
            }
            return false;
        });
    }

    @Override
    public List<IEntityDisplayOverride> getEntityDisplayOverrides() {
        return Collections.singletonList((mode, probeInfo, player, world, entity, data) -> {
            if (entity instanceof Drone) {
                if (Tools.show(mode, Config.getRealConfig().getShowModName())) {
                    probeInfo.horizontal()
                            .entity(entity)
                            .vertical()
                            .text(EnumRarity.values()[((Drone) entity).tier()].rarityColor + ((Drone) entity).info().name())
                            .text(TextStyleClass.MODNAME + Tools.getModName(entity));
                } else {
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .entity(entity)
                            .text(EnumRarity.values()[((Drone) entity).tier()].rarityColor + ((Drone) entity).info().name());
                }

                return true;
            }

            return false;
        });
    }

    private boolean holdingAnalyzer(EntityPlayer player) {
        for (EnumHand hand : EnumHand.values()) {
            ItemStack itemStack = player.getHeldItem(hand);
            if (!itemStack.isEmpty() && itemStack.getItem() == Items.get("analyzer").item()) {
                return true;
            }
        }

        return false;
    }
}
