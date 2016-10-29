package io.github.drmanganese.topaddons.helmets;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import io.github.drmanganese.topaddons.AddonManager;
import io.github.drmanganese.topaddons.Config;

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
        return "topaddons blacklist <add/remove>";
    }

    @Nonnull
    @Override
    public List<String> getCommandAliases() {
        return Lists.newArrayList("topaddons");
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (!Config.Helmets.allHelmetsProbable) {
            sender.addChatMessage(new TextComponentString(TextFormatting.RED + TextFormatting.ITALIC.toString() + "AllHelmetsProbable config is set to false"));
            return;
        }

        if ("blacklist".equals(args[0]) && ("add".equals(args[1]) || "remove".equals(args[1]))) {
            if (sender instanceof EntityPlayer) {
                ItemStack stack = (((EntityPlayer) sender).getHeldItemMainhand());
                if (stack == null || !(stack.getItem() instanceof ItemArmor)) {
                    sender.addChatMessage(new TextComponentString(TextFormatting.RED + "Hold a helmet in your main hand to add/remove it to the blacklist"));
                    return;
                }

                ResourceLocation loc = stack.getItem().getRegistryName();
                if ("add".equals(args[1])) {
                    if (AddonManager.SPECIAL_HELMETS.containsKey(((ItemArmor)stack.getItem()).getClass())) {
                        sender.addChatMessage(new TextComponentString(TextFormatting.RED + "This helmet cannot be added to the blacklist"));
                        return;
                    }

                    if (Config.Helmets.helmetBlacklistSet.contains(loc)) {
                        sender.addChatMessage(new TextComponentString(TextFormatting.RED + "This helmet is already on the blacklist"));
                    } else {
                        Config.Helmets.helmetBlacklistSet.add(loc);
                        sender.addChatMessage(new TextComponentString(TextFormatting.GREEN + "Helmet was added to the blacklist"));
                    }
                } else {
                    if (Config.Helmets.helmetBlacklistSet.contains(loc)) {
                        Config.Helmets.helmetBlacklistSet.remove(loc);
                        sender.addChatMessage(new TextComponentString(TextFormatting.GREEN + "Helmet was removed from the blacklist"));
                    } else {
                        sender.addChatMessage(new TextComponentString(TextFormatting.RED + "This helmet is not on the blacklist"));
                    }
                }

                Config.updateHelmetBlacklistConfig();
            }
        }
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender) {
        return sender.canCommandSenderUseCommand(4, this.getCommandName());
    }

    @Nonnull
    @Override
    public List<String> getTabCompletionOptions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos pos) {
        List<String> opts = new ArrayList<>();
        if (args.length == 1) {
            opts.add("blacklist");
        } else if (args.length == 2 && "blacklist".equals(args[0])) {
            opts.add("add");
            opts.add("remove");
        }

        return opts;
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
