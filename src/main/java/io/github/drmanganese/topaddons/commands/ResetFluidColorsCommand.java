package io.github.drmanganese.topaddons.commands;

import io.github.drmanganese.topaddons.network.PacketHandler;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ResetFluidColorsCommand implements Command<CommandSourceStack> {

    private static final ResetFluidColorsCommand INSTANCE = new ResetFluidColorsCommand();


    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("resetFluidColors")
            .executes(INSTANCE);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
        PacketHandler.sendResetColorsMapsMessage(serverPlayer);
        return 0;
    }
}
