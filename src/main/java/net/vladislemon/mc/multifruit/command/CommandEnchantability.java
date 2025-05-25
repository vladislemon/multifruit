package net.vladislemon.mc.multifruit.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

public class CommandEnchantability extends CommandBase {

    @Override
    public String getCommandName() {
        return "enchantability";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        if (sender instanceof EntityPlayer) {
            return "/" + getCommandName() + " [item]";
        } else {
            return "/" + getCommandName() + " <item>";
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayer) {
            if (args.length > 1) {
                throw new WrongUsageException(getCommandUsage(sender));
            }
            if (args.length == 1) {
                printEnchantability(sender, getItemByText(sender, args[0]));
            } else {
                printEnchantability(sender, getPlayerHeldItem((EntityPlayer) sender));
            }
        } else {
            if (args.length != 1) {
                throw new WrongUsageException(getCommandUsage(sender));
            }
            printEnchantability(sender, getItemByText(sender, args[0]));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return args.length == 1 ? getListOfStringsFromIterableMatchingLastWord(args, Item.itemRegistry.getKeys())
            : null;
    }

    private static ItemStack getPlayerHeldItem(EntityPlayer player) {
        if (player.getHeldItem() == null) {
            throw new CommandException("commands.enchantability.playerDoesNotHoldAnyItem", player.getDisplayName());
        }
        return player.getHeldItem();
    }

    private static void printEnchantability(ICommandSender sender, Item item) {
        printEnchantability(sender, new ItemStack(item));
    }

    private static void printEnchantability(ICommandSender sender, ItemStack itemStack) {
        int itemEnchantability = itemStack.getItem()
            .getItemEnchantability(itemStack);
        sender.addChatMessage(new ChatComponentText(String.valueOf(itemEnchantability)));
    }
}
