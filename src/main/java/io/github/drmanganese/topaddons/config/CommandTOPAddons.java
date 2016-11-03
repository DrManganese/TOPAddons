package io.github.drmanganese.topaddons.config;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.config.network.MessageClientOption;
import io.github.drmanganese.topaddons.config.network.PacketHandler;
import io.github.drmanganese.topaddons.reference.Names;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CommandTOPAddons implements ICommand {

    @Nonnull
    @Override
    public String getCommandName() {
        return "topaddons";
    }

    @Nonnull
    @Override
    public String getCommandUsage(@Nonnull ICommandSender sender) {
        return "/topaddons [option] <0/1>";
    }

    @Nonnull
    @Override
    public List<String> getCommandAliases() {
        return Lists.newArrayList("topaddons");
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length == 0) {
            sender.addChatMessage(new TextComponentString(getCommandUsage(sender)));
        } else {

            if (args[0] != null && Names.clientConfigOptions.containsKey(args[0])) {
                if (args[1] == null || args[1].isEmpty()) {
                    sender.addChatMessage(new TextComponentString(TextFormatting.RED + "Accepted VALUES for " + args[0] + ": 0-1"));
                    return;
                }
                String option = args[0];
                int value = Integer.valueOf(args[1]);
                if (Names.clientConfigOptions.get(option) == Boolean.TYPE) {
                    if (value > 1 || value < 0) {
                        sender.addChatMessage(new TextComponentString(TextFormatting.RED + "Accepted VALUES for " + option + ": 0-1"));
                        return;
                    }
                }

                PacketHandler.INSTANCE.sendTo(new MessageClientOption(option, value), (EntityPlayerMP) sender);
                ((EntityPlayerMP) sender).getCapability(TOPAddons.OPTS_CAP, null).setOption(option, value);
                sender.addChatMessage(new TextComponentString(TextFormatting.GREEN + "Config changed!"));
            } else {
                sender.addChatMessage(new TextComponentString(TextFormatting.RED + args[0] + " is not a valid option."));
            }
        }
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender) {
        return true;
    }

    @Nonnull
    @Override
    public List<String> getTabCompletionOptions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos pos) {
        List<String> opts = new ArrayList<>();
        if (args.length == 1) {
            opts.addAll(Names.clientConfigOptions.keySet());
        } else if (args.length == 2) {
            opts.add("0");
            opts.add("1");
        }

        return CommandBase.getListOfStringsMatchingLastWord(args, opts);
    }

    @Override
    public boolean isUsernameIndex(@Nonnull String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(@Nonnull ICommand o) {
        return getCommandName().compareTo(o.getCommandName());
    }

}
