package io.github.drmanganese.topaddons.commands;

import io.github.drmanganese.topaddons.TopAddons;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import com.mojang.brigadier.CommandDispatcher;

public final class TopAddonsCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal(TopAddons.MOD_ID)
                .then(ResetFluidColorsCommand.register(dispatcher))
        );
    }
}
