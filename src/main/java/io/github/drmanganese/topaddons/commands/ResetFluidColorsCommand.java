package io.github.drmanganese.topaddons.commands;

import io.github.drmanganese.topaddons.network.PacketHandler;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ResetFluidColorsCommand implements Command<CommandSource> {

    private static final ResetFluidColorsCommand INSTANCE = new ResetFluidColorsCommand();


    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("resetFluidColors")
            .executes(INSTANCE);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        final ServerPlayerEntity serverPlayer = context.getSource().asPlayer();
        PacketHandler.sendResetColorsMapsMessage(serverPlayer);
        return 0;
    }
}
