package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidRegistry;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.Util;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.elements.stevescarts2.ElementCage;
import io.github.drmanganese.topaddons.reference.Colors;
import io.github.drmanganese.topaddons.styles.ProgressStyleTOPAddonGrey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IEntityDisplayOverride;
import mcjty.theoneprobe.api.ILayoutStyle;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.config.Config;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.engines.ModuleCoalBase;
import vswe.stevescarts.modules.engines.ModuleEngine;
import vswe.stevescarts.modules.engines.ModuleSolarBase;
import vswe.stevescarts.modules.engines.ModuleThermalBase;
import vswe.stevescarts.modules.realtimers.ModuleCage;
import vswe.stevescarts.modules.storages.ModuleStorage;
import vswe.stevescarts.modules.storages.tanks.ModuleTank;
import vswe.stevescarts.modules.workers.tools.ModuleTool;

@TOPAddon(dependency = "stevescarts")
public class AddonStevesCarts2 extends AddonBlank {

    private final String[] priorities = {TextFormatting.GREEN + "{*topaddons.stevescarts2:priority_high*}", TextFormatting.GOLD + "{*topaddons.stevescarts2:priority_medium*}", TextFormatting.YELLOW + "{*topaddons.stevescarts2:priority_low*}", TextFormatting.RED + "{*topaddons.stevescarts2:priority_disabled*}"};

    private boolean showChestsContents = true;

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile instanceof TileEntityCartAssembler) {
            TileEntityCartAssembler assembler = (TileEntityCartAssembler) tile;
            if (assembler.getIsAssembling()) {
                textPrefixed(probeInfo, "{*topaddons.stevescarts2:assembly_time*}", Util.hoursMinsSecsFromTicks(Math.round((assembler.getMaxAssemblingTime() - assembler.getAssemblingTime()) / assembler.getEfficiency()), 'h', 'm', 's'));
            }

            probeInfo.progress(assembler.getFuelLevel(), assembler.getMaxFuelLevel(), new ProgressStyleTOPAddonGrey().prefix("Fuel: ").suffix("/" + assembler.getMaxFuelLevel()).filledColor(0xFF1BC3F0).alternateFilledColor(0xFF1288A8));
        }
    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        if (entity instanceof EntityMinecartModular) {
            final List<ModuleTank> tanks = new ArrayList<>();
            final List<ModuleEngine> engines = new ArrayList<>();
            final List<ItemStack> inventory = new ArrayList<>();
            final List<ModuleBase> otherModules = new ArrayList<>();

            ItemStack cage = ItemStack.EMPTY;
            ModuleTool tool = null;

            for (ModuleBase moduleBase : ((EntityMinecartModular) entity).getModules()) {
                if (moduleBase instanceof ModuleTank) {
                    tanks.add((ModuleTank) moduleBase);
                } else if (moduleBase instanceof ModuleCage) {
                    cage = moduleBase.getData().getItemStack();
                } else if (moduleBase instanceof ModuleEngine) {
                    engines.add((ModuleEngine) moduleBase);
                } else if (moduleBase instanceof ModuleTool) {
                    tool = (ModuleTool) moduleBase;
                } else if (moduleBase instanceof ModuleStorage && showChestsContents) {
                    for (int i = 0; i < moduleBase.getInventorySize(); i++) {
                        ItemStack stack = moduleBase.getStack(i);
                        if (!stack.isEmpty()) {
                            boolean merged = false;
                            for (ItemStack itemStack : inventory) {
                                if (itemStack.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(itemStack, stack)) {
                                    itemStack.setCount(itemStack.getCount() + stack.getCount());
                                    merged = true;
                                    break;
                                }
                            }
                            if (!merged) {
                                inventory.add(stack.copy());
                            }
                        }
                    }
                } else if (player.getCapability(TOPAddons.OPTS_CAP, null).getBoolean("stevesOtherModules")) {
                    otherModules.add(moduleBase);
                }
            }

            if (tool != null) {
                int durability = 100 * tool.getCurrentDurability() / tool.getMaxDurability();
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).item(tool.getData().getItemStack()).text(tool.getData().getItemStack().getDisplayName() + " (" + durability + "%)");
            }

            if (mode == ProbeMode.EXTENDED) {
                if (inventory.size() > 0) {
                    showItemStackRows(probeInfo, inventory, 9, probeInfo.defaultLayoutStyle().borderColor(Config.chestContentsBorderColor));
                }
            }

            if (!cage.isEmpty()) {
                if (((EntityMinecartModular) entity).getCartRider() != null) {
                    Entity passenger = ((EntityMinecartModular) entity).getCartRider();
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).element(new ElementCage(getElementId(player, "steves_cage"), passenger)).text(passenger.getDisplayName().getFormattedText());
                } else {
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).item(cage).text("{*topaddons:tank_empty*}");
                }
            }

            for (ModuleTank tank : tanks) {
                if (tank.getFluid() != null) {
                    AddonForge.addTankElement(probeInfo, tank.getData().getName(), tank.getFluid().getLocalizedName(), tank.getFluid().amount, tank.getInfo().capacity, "mB", Colors.getHashFromFluid(tank.getFluid()), mode, player);
                } else {
                    AddonForge.addTankElement(probeInfo, tank.getData().getName(), "", 0, 0, "mB", 0, mode, player);
                }
            }

            if (engines.size() > 0) {
                IProbeInfo engineVert = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xFF777777).spacing(0));
                engines.sort(Comparator.comparingInt(ModuleEngine::getPriority));

                for (ModuleEngine engine : engines) {
                    IProbeInfo engineHori = engineVert.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .item(engine.getData().getItemStack())
                            .vertical(probeInfo.defaultLayoutStyle().spacing(-1));

                    int fuel = engine.getTotalFuel();
                    if (engine instanceof ModuleThermalBase) {
                        if (engine.getCart().drain(FluidRegistry.WATER, 1, false) == 0) {
                            engineHori.text("{*topaddons.stevescarts2:engine_outofwater*}");
                        } else if (engine.getCart().drain(FluidRegistry.LAVA, 1, false) == 0) {
                            engineHori.text("{*topaddons.stevescarts2:engine_outoflava*}");
                        } else {
                            engineHori.text("{*topaddons.stevescarts2:engine_powered*}");
                        }
                    } else if (engine instanceof ModuleSolarBase) {
                        engineHori.text(fuel > 0 ? String.valueOf(fuel) : "{*topaddons.stevescarts2:engine_outofpower*}");
                    } else if (engine instanceof ModuleCoalBase) {
                        engineHori.text(fuel > 0 ? engine.getFuelLevel() + " (" + Util.metricPrefixise(fuel) + ")" : "{*topaddons.stevescarts2:engine_outoffuel*}");
                    }

                    engineHori.text(priorities[engine.getPriority()]);
                }
            }

            if (mode == ProbeMode.EXTENDED && otherModules.size() > 0) {
                showItemStackRows(probeInfo, otherModules.stream().map(m -> m.getData().getItemStack()).collect(Collectors.toList()), 9);
            }
        }
    }

    @Override
    public void registerElements() {
        registerElement("steves_cage", ElementCage::new);
    }

    @Override
    public void updateConfigs(Configuration config) {
        showChestsContents = config.get("stevescarts", "showChestsContents", true, "Show cart inventory contents in TOP.").setLanguageKey("topaddons.config:stevescarts_chest_contents").getBoolean();
    }

    @Override
    public List<IEntityDisplayOverride> getEntityDisplayOverrides() {
        return Collections.singletonList((mode, probeInfo, player, world, entity, data) -> {
            if (entity instanceof EntityMinecartModular) {
                EntityMinecartModular cart = (EntityMinecartModular) entity;

                if (Tools.show(mode, mcjty.theoneprobe.config.Config.getRealConfig().getShowModName())) {
                    probeInfo.horizontal()
                            .entity(cart)
                            .vertical()
                            .text(cart.getCartName())
                            .text(TextStyleClass.MODNAME + Tools.getModName(entity));
                } else {
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .entity(cart)
                            .text(cart.getDisplayName().getFormattedText());
                }

                return true;
            } else {
                return false;
            }
        });
    }

    private IProbeInfo showItemStackRows(IProbeInfo probeInfo, List<ItemStack> stacks, int rowWidth, ILayoutStyle layoutStyle) {
        IProbeInfo vert = probeInfo.vertical(layoutStyle);
        IProbeInfo hori = vert.horizontal(probeInfo.defaultLayoutStyle());
        int j = 0;
        for (ItemStack stack : stacks) {
            hori.item(stack);
            j++;
            if (j > rowWidth) {
                j = 0;
                hori = vert.horizontal(probeInfo.defaultLayoutStyle());
            }
        }

        return probeInfo;
    }

    private IProbeInfo showItemStackRows(IProbeInfo probeInfo, List<ItemStack> stacks, int rowWidth) {
        return showItemStackRows(probeInfo, stacks, rowWidth, probeInfo.defaultLayoutStyle());
    }
}
