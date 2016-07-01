package io.github.drmanganese.topplugins.plugins;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTankInfo;
import io.github.drmanganese.topplugins.api.ItemArmorProbed;
import io.github.drmanganese.topplugins.api.TOPPlugin;
import io.github.drmanganese.topplugins.helmets.ItemProbedApiaristArmor;
import io.github.drmanganese.topplugins.helmets.ItemProbedArmorNaturalist;
import io.github.drmanganese.topplugins.reference.Names;
import io.github.drmanganese.topplugins.styles.ProgressStyleForestryMultiColored;
import io.github.drmanganese.topplugins.styles.ProgressStyleTank;

import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.List;

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
import forestry.apiculture.multiblock.TileAlveary;
import forestry.arboriculture.tiles.TileLeaves;
import forestry.arboriculture.tiles.TileSapling;
import forestry.arboriculture.tiles.TileTreeContainer;
import forestry.core.PluginCore;
import forestry.core.errors.EnumErrorCode;
import forestry.core.tiles.ILiquidTankTile;
import forestry.core.tiles.TileAnalyzer;
import forestry.core.tiles.TileEngine;
import forestry.core.tiles.TileForestry;
import forestry.core.utils.GeneticsUtil;
import forestry.factory.tiles.TileMoistener;
import forestry.farming.tiles.TileFarm;
import forestry.farming.tiles.TileFarmValve;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

@TOPPlugin(dependency = "forestry")
public class PluginForestry extends PluginBlank {

    /**
     * Errors to display even when not sneaking
     */
    private final List<IErrorState> NORMAL_STATES = Arrays.asList(
            EnumErrorCode.NO_QUEEN,
            EnumErrorCode.IS_RAINING,
            EnumErrorCode.NOT_DAY,
            EnumErrorCode.FORCED_COOLDOWN,
            EnumErrorCode.NO_RECIPE,
            EnumErrorCode.NO_SPECIMEN,
            EnumErrorCode.NOT_DARK
    );

    @Override
    public boolean hasHelmets() {
        return true;
    }

    @Override
    public List<Class<? extends ItemArmorProbed>> getHelmets() {
        return Arrays.asList(ItemProbedApiaristArmor.class, ItemProbedArmorNaturalist.class);
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());
        if (tile != null && (tile instanceof TileForestry || tile instanceof TileAlveary || tile instanceof TileTreeContainer || tile instanceof TileFarm)) {

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
                        probeInfo.text(TextFormatting.YELLOW + "Species: " + TextFormatting.WHITE + BeeManager.beeRoot.getMember(queen).getGenome().getSpeciesRoot().getMember(queen).getDisplayName());
                    }
                }

                //TODO special apiary inventory renderer
            }

            //Analyzer
            if (tile instanceof TileAnalyzer) {
                TileAnalyzer analyzer = (TileAnalyzer) tile;
                if (analyzer.getIndividualOnDisplay() != null) {
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).item(analyzer.getIndividualOnDisplay()).progress(analyzer.getProgressScaled(100), 100, new ProgressStyleForestryMultiColored(analyzer.getProgressScaled(100)));
                }
            }

            /* Forestry tree leaves
                Show fruit ripeness if the tree has them
                Show when a leaf is pollinated (only when wearing spectacles)
             */
            if (tile instanceof TileLeaves) {
                TileLeaves leaves = (TileLeaves) tile;
                if (leaves.hasFruit()) {
                    textPrefixed(probeInfo, "Fruit", leaves.getTree().getGenome().getFruitProvider().getDescription() + " - " +(leaves.getRipeness() >= 1.0F ? TextFormatting.GREEN + "Ripe" : TextFormatting.RED + "Unripe"));
                }

                //TODO probe spectacles
                if (leaves.isPollinated() && GeneticsUtil.hasNaturalistEye(player)) {
                    probeInfo.text(TextFormatting.GREEN + "Pollinated");
                }
            }

            //Only show genome info if the Individual was analyzed (+sneaking)
            if (tile instanceof TileSapling) {
                ITree tree = ((TileSapling) tile).getTree();
                if (mode == ProbeMode.EXTENDED) {
                    if (tree.isAnalyzed()) {
                        textPrefixed(probeInfo, "Saplings", tree.getGenome().getActiveAllele(EnumTreeChromosome.FERTILITY).getName());
                        textPrefixed(probeInfo, "Maturation", tree.getGenome().getActiveAllele(EnumTreeChromosome.MATURATION).getName());
                        textPrefixed(probeInfo, "Height", tree.getGenome().getActiveAllele(EnumTreeChromosome.HEIGHT).getName());
                        textPrefixed(probeInfo, "Girth", tree.getGenome().getActiveAllele(EnumTreeChromosome.GIRTH).toString());
                        textPrefixed(probeInfo, "Yield", tree.getGenome().getActiveAllele(EnumTreeChromosome.YIELD).getName());
                        textPrefixed(probeInfo, "Sappiness", tree.getGenome().getActiveAllele(EnumTreeChromosome.SAPPINESS).getName());
                    } else {
                        probeInfo.text("Unknown Genome");
                    }
                }
            }

            //All blocks with internal tanks
            if (tile instanceof ILiquidTankTile) {
                FluidTankInfo[] tanks = ((ILiquidTankTile) tile).getTankInfo(data.getSideHit());
                String tankName = "Tank";
                int i = 0;
                for (FluidTankInfo tank : tanks) {
                    tankGauge(probeInfo, tile, tank, i);
                    i++;
                }
            }

            //All engines (sneaking only)
            if (tile instanceof TileEngine) {
                TileEngine engine = ((TileEngine) tile);
                if (mode == ProbeMode.EXTENDED) {
                    textPrefixed(probeInfo, "Stored", engine.getEnergyManager().getEnergyStored(null) + " RF");
                    textPrefixed(probeInfo, "Heat", engine.getHeat() / 10 + " C" + (errorStates.contains(EnumErrorCode.FORCED_COOLDOWN) ? " (Cooling down)" : ""));
                }
            }

            //lol, moist
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
                textPrefixed(probeInfo, "Speed", (speed == 0 ? TextFormatting.RED.toString() : "") + speed);

                /**
                 *  Wheat consumption process
                 *   1) Loop through inventory slots 0-9 to count the amount of each "wheat-type"
                 *   2) Get the type of wheat in slot 9 (working slot), the preceding '▶' will be white
                 *   3) Display an ItemStack with the stackSize from 1) for each wheat-type with '▶' in between
                 *   4) If there is a valid recipe in slot 10, display the ItemStacks of slots 10 and 11
                 *   with a progress bar in between
                 */
                ItemStack[] wheats = new ItemStack[] {
                        new ItemStack(Items.WHEAT, 0),
                        new ItemStack(PluginCore.items.mouldyWheat, 0),
                        new ItemStack(PluginCore.items.decayingWheat, 0),
                        new ItemStack(PluginCore.items.mulch, 0)
                };

                TextFormatting[] arrowColors = new TextFormatting[] {
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
                            .progress(15 - moistener.getProductionProgressScaled(15), 15, probeInfo.defaultProgressStyle().showText(false))
                            .item(moistener.getInternalInventory().getStackInSlot(10));
                }
            }


            if (tile instanceof TileFarm) {
                TileFarm farm = (TileFarm) tile;

                EnumFacing facing = player.getHorizontalFacing();
                ItemStack[] farmIcons = new ItemStack[4];
                for (int i = 0; i < 4; i++) {
                    farmIcons[i] = farm.getMultiblockLogic().getController().getFarmLogic(FarmDirection.getFarmDirection(facing)).getIconItemStack();
                    facing = facing.rotateY();
                }
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().spacing(25)).text("").item(farmIcons[0]);
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().spacing(25)).item(farmIcons[3]).text("").item(farmIcons[1]);
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().spacing(25)).text("").item(farmIcons[2]);

                if (!(farm instanceof TileFarmValve) && mode == ProbeMode.EXTENDED)
                    tankGauge(probeInfo, farm, farm.getMultiblockLogic().getController().getTankManager().getTank(0).getInfo());
            }

            /**
             * Forestry error states (correspond to the "Ledgers" you see on the left of guis)
             * Show all errors if sneaking
             * Show important errors always (defined in {@link NORMAL_STATES}
             */
            if (errorStates.size() > 0 && !(tile instanceof TileLeaves)) {
                errorStates.forEach(state -> {
                    if (mode == ProbeMode.EXTENDED || NORMAL_STATES.contains(state))
                        probeInfo.text(TextFormatting.RED + I18n.format(state.getUnlocalizedDescription()));
                });
            }
        }


    }


    private IProbeInfo tankGauge(IProbeInfo probeInfo, TileEntity tile, FluidTankInfo tank) {
        return tankGauge(probeInfo, tile, tank, 0);
    }

    private IProbeInfo tankGauge(IProbeInfo probeInfo, TileEntity tile, FluidTankInfo tank, int index) {
        String tankName = "Tank";
        if (tile instanceof ILiquidTankTile && Names.tankNamesMap.containsKey(((ILiquidTankTile) tile).getClass())) {
            tankName = Names.tankNamesMap.get(((ILiquidTankTile) tile).getClass())[index];
        }

        if (tank.fluid == null) {
            textPrefixed(probeInfo, tankName, "empty");
        } else {
            probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(TextFormatting.YELLOW + tankName + ": ").progress(tank.fluid.amount, tank.capacity, new ProgressStyleTank().withFluid(tank.fluid));
        }

        return probeInfo;
    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        if (world != null && entity != null) {
            if (entity instanceof IEntityButterfly) {
                IButterfly butterfly = ((IEntityButterfly) entity).getButterfly();
                if (!butterfly.isPureBred(EnumButterflyChromosome.SPECIES)) {
                    probeInfo.text("Hybrid (" + butterfly.getGenome().getInactiveAllele(EnumButterflyChromosome.SPECIES).getName());
                }

                if (mode == ProbeMode.EXTENDED) {
                    if (butterfly.isAnalyzed()) {
                        textPrefixed(probeInfo, "Size", butterfly.getGenome().getActiveAllele(EnumButterflyChromosome.SIZE).getName());
                        textPrefixed(probeInfo, "Production", butterfly.getGenome().getActiveAllele(EnumButterflyChromosome.SPEED).getName());
                        textPrefixed(probeInfo, "Lifespan", butterfly.getGenome().getActiveAllele(EnumButterflyChromosome.LIFESPAN).getName());
                        textPrefixed(probeInfo, "Fertility", butterfly.getGenome().getActiveAllele(EnumButterflyChromosome.FERTILITY).getName());
                    } else {
                        probeInfo.text("Unknown Genome");
                    }
                }
            }
        }
    }

    @Override
    public void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
    }

    @Override
    public void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (config != null && blockState != null
                && (world.getTileEntity(data.getPos()) instanceof IBeeHousing) || world.getTileEntity(data.getPos()) instanceof TileMoistener) {
            config.showChestContents(IProbeConfig.ConfigMode.NOT);
        } else {
            config.showChestContents(IProbeConfig.ConfigMode.EXTENDED);
        }
    }
}
