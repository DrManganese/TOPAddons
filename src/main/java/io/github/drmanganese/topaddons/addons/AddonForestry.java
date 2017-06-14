package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.elements.forestry.ElementBeeHousingInventory;
import io.github.drmanganese.topaddons.elements.forestry.ElementForestryFarm;
import io.github.drmanganese.topaddons.reference.Colors;
import io.github.drmanganese.topaddons.reference.EnumChip;
import io.github.drmanganese.topaddons.reference.Names;
import io.github.drmanganese.topaddons.styles.ProgressStyleForestryMultiColored;

import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IHiveTile;
import forestry.api.arboriculture.EnumTreeChromosome;
import forestry.api.arboriculture.ITree;
import forestry.api.core.IErrorLogicSource;
import forestry.api.core.IErrorState;
import forestry.api.farming.FarmDirection;
import forestry.api.lepidopterology.EnumButterflyChromosome;
import forestry.api.lepidopterology.IButterfly;
import forestry.api.lepidopterology.IEntityButterfly;
import forestry.apiculture.items.ItemArmorApiarist;
import forestry.apiculture.multiblock.TileAlveary;
import forestry.apiculture.multiblock.TileAlvearySieve;
import forestry.apiculture.multiblock.TileAlvearySwarmer;
import forestry.apiculture.tiles.TileApiary;
import forestry.apiculture.tiles.TileBeeHousingBase;
import forestry.arboriculture.tiles.TileLeaves;
import forestry.arboriculture.tiles.TileSapling;
import forestry.arboriculture.tiles.TileTreeContainer;
import forestry.core.PluginCore;
import forestry.core.blocks.BlockBogEarth;
import forestry.core.errors.EnumErrorCode;
import forestry.core.fluids.Fluids;
import forestry.core.items.ItemArmorNaturalist;
import forestry.core.tiles.TileAnalyzer;
import forestry.core.tiles.TileEngine;
import forestry.core.tiles.TileForestry;
import forestry.core.utils.GeneticsUtil;
import forestry.energy.tiles.TileEngineBiogas;
import forestry.factory.tiles.TileFermenter;
import forestry.factory.tiles.TileMoistener;
import forestry.factory.tiles.TileRaintank;
import forestry.factory.tiles.TileStill;
import forestry.farming.tiles.TileFarm;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;

@TOPAddon(dependency = "forestry")
public class AddonForestry extends AddonBlank {

    /**
     * Errors to display even when not sneaking
     */
    private final List<IErrorState> NORMAL_STATES = Arrays.asList(
            EnumErrorCode.NO_QUEEN,
            EnumErrorCode.IS_RAINING,
            EnumErrorCode.NOT_DAY,
            EnumErrorCode.FORCED_COOLDOWN,
            EnumErrorCode.NO_SPECIMEN,
            EnumErrorCode.NOT_DARK
    );

    private void addFarmElement(IProbeInfo probeInfo, ItemStack[] farmIcons, String oneDirection, EntityPlayer player) {
        addFarmElement(probeInfo, farmIcons, oneDirection, false, new ItemStack[]{}, player);
    }

    private void addFarmElement(IProbeInfo probeInfo, ItemStack[] farmIcons, String oneDirection, boolean showInventory, ItemStack[] inventoryStacks, EntityPlayer player) {
        probeInfo.element(new ElementForestryFarm(getElementId(player, "farm"), farmIcons, oneDirection, showInventory, inventoryStacks));
    }

    private IProbeInfo addBeeHouseInventory(IProbeInfo probeInfo, boolean isApiary, ItemStack[] inventoryStacks, EntityPlayer player) {
        return probeInfo.element(new ElementBeeHousingInventory(getElementId(player, "bee_inventory"), isApiary, inventoryStacks));
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (blockState.getBlock() == PluginCore.blocks.bogEarth) {
            textPrefixed(probeInfo, "{*topaddons.forestry:maturity*}", TextStyleClass.WARNING + String.valueOf(blockState.getValue(BlockBogEarth.MATURITY) * 100 / 3) + "%");
        }

        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile instanceof TileForestry || tile instanceof TileAlveary || tile instanceof TileTreeContainer || tile instanceof TileFarm) {
            ImmutableSet<IErrorState> errorStates;
            if (tile instanceof IErrorLogicSource) {
                errorStates = ((IErrorLogicSource) tile).getErrorLogic().getErrorStates();
            } else {
                errorStates = ImmutableSet.of();
            }

            //Alveary, Apiary, Beehouse
            if (tile instanceof IBeeHousing && !(tile instanceof IHiveTile)) {
                IBeeHousing beeHousing = (IBeeHousing) tile;
                ItemStack queen = beeHousing.getBeeInventory().getQueen();

                if (queen != null) {
                    int progress = beeHousing.getBeekeepingLogic().getBeeProgressPercent();
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).item(queen).progress(progress, 100, new ProgressStyleForestryMultiColored(progress).showText(false));

                    if (mode == ProbeMode.EXTENDED) {
                        textPrefixed(probeInfo, "{*for.gui.species*}", BeeManager.beeRoot.getMember(queen).getGenome().getSpeciesRoot().getMember(queen).getDisplayName());
                    }
                }

                if (mode == ProbeMode.EXTENDED) {
                    ItemStack[] inventoryStacks = new ItemStack[(tile instanceof TileApiary) ? 12 : 9];
                    if (tile instanceof TileAlveary) {
                        for (int i = 0; i < 9; i++) {
                            ItemStack stack = ((TileAlveary) tile).getMultiblockLogic().getController().getInternalInventory().getStackInSlot(i);
                            inventoryStacks[i] = (stack != null) ? stack : new ItemStack(Blocks.BARRIER);
                        }
                    } else if (tile instanceof TileBeeHousingBase) {
                        for (int i = 0; i < 9; i++) {
                            ItemStack stack = ((TileBeeHousingBase) tile).getInternalInventory().getStackInSlot(i);
                            inventoryStacks[i] = (stack != null) ? stack : new ItemStack(Blocks.BARRIER);
                        }

                        if (tile instanceof TileApiary) {
                            for (int i = 9; i < 12; i++) {
                                ItemStack stack = ((TileApiary) tile).getInternalInventory().getStackInSlot(i);
                                inventoryStacks[i] = (stack != null) ? stack : new ItemStack(Blocks.BARRIER);
                            }
                        }
                    }

                    addBeeHouseInventory(probeInfo, tile instanceof TileApiary, reorderBeeInvStacks(inventoryStacks), player);
                }
            }

            //Analyzer
            if (tile instanceof TileAnalyzer) {
                TileAnalyzer analyzer = (TileAnalyzer) tile;
                if (analyzer.getIndividualOnDisplay() != null) {
                    IProbeInfo horizontal = probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));
                    horizontal.item(analyzer.getIndividualOnDisplay());
                    horizontal.progress(analyzer.getProgressScaled(100), 100, new ProgressStyleForestryMultiColored(analyzer.getProgressScaled(100)));
                }
            }

            /* Forestry tree leaves
                Show fruit ripeness if the tree has them
                Show when a leaf is pollinated (only when wearing spectacles)
             */
            if (tile instanceof TileLeaves) {
                TileLeaves leaves = (TileLeaves) tile;
                if (leaves.hasFruit()) {
                    String ripeness = leaves.getRipeness() >= 1.0F ? TextStyleClass.OK + "{*topaddons.forestry:ripe*}" : TextFormatting.RED + "{*topaddons.forestry:unripe*}";
                    textPrefixed(probeInfo, "{*topaddons.forestry:fruit*}", leaves.getTree().getGenome().getFruitProvider().getDescription() + " - " + ripeness);
                }

                if (leaves.isPollinated() && GeneticsUtil.hasNaturalistEye(player)) {
                    probeInfo.text(TextStyleClass.LABEL + "{*topaddons.forestry:pollinated*}");
                }
            }

            //Only show genome info if the Individual was analyzed (+sneaking)
            if (tile instanceof TileSapling) {
                ITree tree = ((TileSapling) tile).getTree();
                if (mode == ProbeMode.EXTENDED) {
                    if (tree != null && tree.isAnalyzed()) {
                        textPrefixed(probeInfo, "{*for.gui.saplings*}", tree.getGenome().getActiveAllele(EnumTreeChromosome.FERTILITY).getName());
                        textPrefixed(probeInfo, "{*for.gui.maturity*}", tree.getGenome().getActiveAllele(EnumTreeChromosome.MATURATION).getName());
                        textPrefixed(probeInfo, "{*for.gui.height*}", tree.getGenome().getActiveAllele(EnumTreeChromosome.HEIGHT).getName());
                        textPrefixed(probeInfo, "{*for.gui.girth*}", tree.getGenome().getActiveAllele(EnumTreeChromosome.GIRTH).toString());
                        textPrefixed(probeInfo, "{*for.gui.yield*}", tree.getGenome().getActiveAllele(EnumTreeChromosome.YIELD).getName());
                        textPrefixed(probeInfo, "{*for.gui.sappiness*}", tree.getGenome().getActiveAllele(EnumTreeChromosome.SAPPINESS).getName());
                    } else {
                        probeInfo.text(TextStyleClass.OBSOLETE + "{*for.gui.unknown*}");
                    }
                }
            }

            if (tile instanceof TileFarm) {
                TileFarm farm = (TileFarm) tile;

                EnumFacing facing = player.getHorizontalFacing();
                ItemStack[] farmIcons = new ItemStack[5];
                ItemStack socket = farm.getMultiblockLogic().getController().getSocket(0);
                farmIcons[4] = (socket != null) ? socket : new ItemStack(Blocks.BARRIER);
                for (int i = 0; i < 4; i++) {
                    farmIcons[i] = farm.getMultiblockLogic().getController().getFarmLogic(FarmDirection.getFarmDirection(facing)).getIconItemStack();
                    facing = facing.rotateY();
                }

                if (mode == ProbeMode.NORMAL) {
                    addFarmElement(probeInfo, farmIcons, facing.getName(), player);
                }

                if (mode == ProbeMode.EXTENDED) {
                    ItemStack[] inventoryStacks = new ItemStack[20];
                    for (int i = 0; i < 20; i++) {
                        if (farm.getInternalInventory().getStackInSlot(i) == null) {
                            inventoryStacks[i] = new ItemStack(Blocks.BARRIER);
                        } else {
                            inventoryStacks[i] = farm.getInternalInventory().getStackInSlot(i);
                        }
                    }
                    addFarmElement(probeInfo, farmIcons, facing.getName(), true, inventoryStacks, player);
                }
            }

            if (tile instanceof TileMoistener) {
                TileMoistener moistener = (TileMoistener) tile;

                //Moistening speed
                int light = world.getLight(data.getPos().up());
                int speed;
                if (light > 11) {
                    speed = 0;
                } else if (light >= 9) {
                    speed = 1;
                } else if (light >= 7) {
                    speed = 2;
                } else if (light >= 5) {
                    speed = 3;
                } else {
                    speed = 4;
                }
                textPrefixed(probeInfo, "{*topaddons.forestry:moistener_speed*}", (speed == 0 ? TextStyleClass.WARNING.toString() : "") + speed);

                /*
                 *  Wheat consumption process
                 *   1) Loop through inventory slots 0-9 to count the amount of each "wheat-type"
                 *   2) Get the type of wheat in slot 9 (working slot), the preceding '▶' will be white
                 *   3) Display an ItemStack with the stackSize from 1) for each wheat-type with '▶' in between
                 *   4) If there is a valid recipe in slot 10, display the ItemStacks of slots 10 and 11
                 *   with a progress bar in between
                 */
                ItemStack[] wheats = new ItemStack[]{
                        new ItemStack(Items.WHEAT, 0),
                        new ItemStack(PluginCore.items.mouldyWheat, 0),
                        new ItemStack(PluginCore.items.decayingWheat, 0),
                        new ItemStack(PluginCore.items.mulch, 0)
                };

                TextFormatting[] arrowColors = new TextFormatting[]{
                        TextFormatting.DARK_GRAY,
                        TextFormatting.DARK_GRAY,
                        TextFormatting.DARK_GRAY
                };

                for (int i = 0; i < 10; i++) {
                    ItemStack stack = moistener.getInternalInventory().getStackInSlot(i);
                    if (stack != null) {
                        for (int j = 0; j < wheats.length; j++) {
                            if (stack.isItemEqual(wheats[j])) {
                                wheats[j].stackSize += stack.stackSize;
                                if (i == 9) {
                                    arrowColors[j] = TextFormatting.WHITE;
                                }
                            }
                        }
                    }
                }

                //\u25b6 = ▶
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .item(wheats[0])
                        .text(arrowColors[0] + "\u25b6")
                        .item(wheats[1])
                        .text(arrowColors[1] + "\u25b6")
                        .item(wheats[2])
                        .text(arrowColors[2] + "\u25b6")
                        .item(wheats[3]);

                if (moistener.getInternalInventory().getStackInSlot(11) != null) {
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .item(moistener.getInternalInventory().getStackInSlot(11))
                            .progress(15 - moistener.getProductionProgressScaled(15), 15, probeInfo.defaultProgressStyle().showText(false).width(60))
                            .item(moistener.getInternalInventory().getStackInSlot(10));
                }
            }

            if (tile instanceof TileEngine) {
                TileEngine engine = ((TileEngine) tile);
                if (mode == ProbeMode.EXTENDED) {
                    textPrefixed(probeInfo, "{*topaddons.forestry:engine_stored*}", engine.getEnergyManager().getEnergyStored() + " RF");
                    textPrefixed(probeInfo, "{*topaddons.forestry:engine_heat*}", engine.getHeat() / 10 + " C" + (errorStates.contains(EnumErrorCode.FORCED_COOLDOWN) ? TextStyleClass.ERROR + " ({*for.errors.forced_cooldown.desc*})" : ""));
                }
                probeInfo.text(TextStyleClass.LABEL + "{*topaddons:generating*} " + TextStyleClass.INFOIMP + engine.getCurrentOutput() + TextStyleClass.LABEL + " RF/t");
            }

            /*
             * Forestry error states (correspond to the "Ledgers" you see on the left of guis)
             * Show all errors if sneaking
             * Show important errors always (defined in {@link NORMAL_STATES}
             *
             * Using deprecated I18n here because it exists on the server.
             * \u21aa = ↪
             */
            if (errorStates.size() > 0) {
                probeInfo.text(TextStyleClass.ERROR + "{*topaddons.forestry:errors_nowork*}");
                errorStates.forEach(state -> {
                    if (mode == ProbeMode.EXTENDED || NORMAL_STATES.contains(state) && !player.getCapability(TOPAddons.OPTS_CAP, null).getBoolean("forestryReasonCrouch"))
                        probeInfo.text(TextStyleClass.ERROR + "\u21aa " + TextStyleClass.WARNING + IProbeInfo.STARTLOC + state.getUnlocalizedDescription() + IProbeInfo.ENDLOC);
                });
            }
        }


    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        if (entity instanceof IEntityButterfly) {
            IButterfly butterfly = ((IEntityButterfly) entity).getButterfly();
            if (!butterfly.isPureBred(EnumButterflyChromosome.SPECIES)) {
                textPrefixed(probeInfo, "{*for.gui.hybrid*}", butterfly.getGenome().getInactiveAllele(EnumButterflyChromosome.SPECIES).getName());
            }

            if (mode == ProbeMode.EXTENDED) {
                if (butterfly.isAnalyzed()) {
                    textPrefixed(probeInfo, "{*for.gui.size*}", butterfly.getGenome().getActiveAllele(EnumButterflyChromosome.SIZE).getName());
                    textPrefixed(probeInfo, "{*for.gui.speed*}", butterfly.getGenome().getActiveAllele(EnumButterflyChromosome.SPEED).getName());
                    textPrefixed(probeInfo, "{*for.gui.lifespan*}", butterfly.getGenome().getActiveAllele(EnumButterflyChromosome.LIFESPAN).getName());
                    textPrefixed(probeInfo, "{*for.gui.fertility*}", butterfly.getGenome().getActiveAllele(EnumButterflyChromosome.FERTILITY).getName());
                } else {
                    probeInfo.text(TextStyleClass.OBSOLETE + "{*for.gui.unknown*}");
                }
            }
        }
    }

    @Override
    public Map<Class<? extends ItemArmor>, EnumChip> getSpecialHelmets() {
        Map<Class<? extends ItemArmor>, EnumChip> map = new HashMap<>(2);
        map.put(ItemArmorApiarist.class, EnumChip.STANDARD);
        map.put(ItemArmorNaturalist.class, EnumChip.SPECTACLES);
        return map;
    }

    @Override
    public void registerElements() {
        registerElement("farm", ElementForestryFarm::new);
        registerElement("bee_inventory", ElementBeeHousingInventory::new);
    }

    @Override
    public void addFluidColors() {
        for (Fluids fluid : Fluids.values()) {
            Colors.fluidColorMap.put(fluid.getFluid(), fluid.getParticleColor().hashCode());
        }
    }

    @Override
    public void addTankNames() {
        Names.tankNamesMap.put(TileEngineBiogas.class, new String[]{"Fuel", "Heating", "Burner"});
        Names.tankNamesMap.put(TileStill.class, new String[]{"In", "Out"});
        Names.tankNamesMap.put(TileFermenter.class, new String[]{"Resource Tank", "Product Tank"});
        Names.tankNamesMap.put(TileRaintank.class, new String[]{"Reservoir"});
    }

    @Override
    public void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());
        if ((tile instanceof IBeeHousing && !(tile instanceof TileAlvearySieve || tile instanceof TileAlvearySwarmer)) || tile instanceof TileMoistener || tile instanceof TileFarm) {
            config.showChestContents(IProbeConfig.ConfigMode.NOT);
            config.showChestContentsDetailed(IProbeConfig.ConfigMode.NOT);
        } else {
            config.showChestContents(IProbeConfig.ConfigMode.EXTENDED);
            config.showChestContentsDetailed(IProbeConfig.ConfigMode.EXTENDED);
        }
    }

    private ItemStack[] reorderBeeInvStacks(ItemStack[] old) {
        ItemStack[] ret = new ItemStack[old.length];
        ret[0] = old[0];
        ret[1] = old[1];
        ret[2] = old[7];
        ret[3] = old[6];
        ret[4] = old[8];
        ret[5] = old[2];
        ret[6] = old[5];
        ret[7] = old[3];
        ret[8] = old[4];
        if (old.length > 9) {
            ret[9] = old[9];
            ret[10] = old[10];
            ret[11] = old[11];
        }

        return ret;
    }
}
