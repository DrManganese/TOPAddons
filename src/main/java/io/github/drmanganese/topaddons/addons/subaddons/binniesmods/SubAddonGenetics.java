package io.github.drmanganese.topaddons.addons.subaddons.binniesmods;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.addons.AddonBlank;
import io.github.drmanganese.topaddons.styles.ProgressStyleGeneticsMachine;

import java.util.List;

import binnie.core.machines.Machine;
import binnie.core.machines.TileEntityMachine;
import binnie.core.machines.power.ComponentProcess;
import binnie.core.machines.power.ComponentProcessIndefinate;
import binnie.genetics.genetics.SequencerItem;
import binnie.genetics.machine.acclimatiser.AcclimatiserLogic;
import binnie.genetics.machine.analyser.AnalyserLogic;
import binnie.genetics.machine.genepool.GenepoolLogic;
import binnie.genetics.machine.incubator.IncubatorLogic;
import binnie.genetics.machine.inoculator.InoculatorLogic;
import binnie.genetics.machine.isolator.IsolatorLogic;
import binnie.genetics.machine.polymeriser.PolymeriserLogic;
import binnie.genetics.machine.sequencer.Sequencer;
import binnie.genetics.machine.sequencer.SequencerLogic;
import binnie.genetics.machine.splicer.SplicerLogic;
import com.google.common.collect.Lists;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

public class SubAddonGenetics extends AddonBlank {

    private static final List<Class<? extends ComponentProcessIndefinate>> machineProcesses = Lists.newArrayList(
            AnalyserLogic.class,
            PolymeriserLogic.class,
            IsolatorLogic.class,
            SplicerLogic.class,
            AcclimatiserLogic.class,
            IncubatorLogic.class,
            SequencerLogic.class,
            InoculatorLogic.class
    );

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity tile = world.getTileEntity(data.getPos());

        if (tile instanceof TileEntityMachine) {
            Machine machine = ((TileEntityMachine) tile).getMachine();

            if (machine != null) {
                for (Class<? extends ComponentProcessIndefinate> process : machineProcesses) {
                    if (machine.hasComponent(process)) {
                        ComponentProcessIndefinate logic = machine.getComponent(process);
                        if (logic.isInProgress()) {
                            if (logic instanceof ComponentProcess) {
                                probeInfo.progress(Math.round(((ComponentProcess) logic).getProgress()), 100, new ProgressStyleGeneticsMachine());
                            } else {
                                probeInfo.progress(100, 100, new ProgressStyleGeneticsMachine(world.getWorldTime() % 2 == 0));
                            }

                            if (process == SequencerLogic.class) {
                                SequencerItem gene = SequencerItem.create(logic.getInventory().getStackInSlot(Sequencer.SLOT_TARGET));
                                if (gene != null && gene.isAnalysed()) {
                                    textPrefixed(probeInfo, gene.getBreedingSystem().getChromosomeName(gene.getGene().getChromosome()), gene.getGene().getName(), TextFormatting.GOLD);
                                }
                            }
                        }

                        break;
                    }
                }

                if (machine.hasComponent(GenepoolLogic.class)) {
                    GenepoolLogic genepoolLogic = machine.getComponent(GenepoolLogic.class);
                    if (genepoolLogic.inProgress()) {
                        probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                                .item(genepoolLogic.getInventory().getStackInSlot(0))
                                .progress(Math.round(genepoolLogic.getProgress()), 100, new ProgressStyleGeneticsMachine().width(81));
                    }
                }
            }
        }


    }
}
