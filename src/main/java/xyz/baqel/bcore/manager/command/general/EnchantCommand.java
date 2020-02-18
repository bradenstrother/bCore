package xyz.baqel.bcore.manager.command.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.primitives.Ints;

import xyz.baqel.bcore.util.BukkitUtil;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class EnchantCommand extends CustomCommand {

    public EnchantCommand() {
        super("enchant", "Unsafely enchant an item.", new String[] {"ench"}, "/enchant (Enchantment) (Level) " +
                "<Player>", "bcore.cmd.enchant");
    }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
    	if(!this.hasPermission(sender, "bcore.cmd.enchantment")) {
			return ResultCommand.INVALID_PERMISSION;
		}
    	
    	if (args.length < 2) {
            this.setUsage(label + " <enchantment> <level> [playerName]");
            return ResultCommand.INVALID_USAGE;
        }
        Player target;
        if (args.length > 2 && sender.hasPermission("bcore.cmd.enchantment.others")) {
            target = BukkitUtil.playerWithNameOrUUID(args[2]);
        } else {
            if (!(sender instanceof Player)) {
                this.setUsage(label + " <enchantment> <level> [playerName]");
                return ResultCommand.INVALID_USAGE;
            }
            target = (Player) sender;
        }
        if (target == null) {
        	this.setPlayer(args[2]);
        	return ResultCommand.INVALID_PLAYER;
        }
        Enchantment enchantment = Enchantment.getByName(args[0]);
        if (enchantment == null) {
            sender.sendMessage(ChatColor.RED + "No enchantment named '" + args[0] + "' found.");
            return ResultCommand.SUCCESS;
        }
        ItemStack stack = target.getItemInHand();
        if (stack == null || stack.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + target.getName() + " is not holding an item.");
            return ResultCommand.SUCCESS;
        }
        Integer level = Ints.tryParse(args[1]);
        if (level == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[1] + "' is not a number.");
            return ResultCommand.SUCCESS;
        }
        int maxLevel = enchantment.getMaxLevel();
        if (level > maxLevel && !sender.hasPermission("bcore.cmd.enchantment.abovemaxlevel")) {
            sender.sendMessage(ChatColor.RED + "The maximum enchantment level for " + enchantment.getName() + " is " + maxLevel + '.');
            return ResultCommand.SUCCESS;
        }
        if (!enchantment.canEnchantItem(stack) && !sender.hasPermission("bcore.cmd.enchantment.anyitem")) {
            sender.sendMessage(ChatColor.RED + "Enchantment " + enchantment.getName() + " cannot be applied to that item.");
            return ResultCommand.SUCCESS;
        }
        stack.addUnsafeEnchantment(enchantment, level);
        String itemName;
        try {
            itemName = stack.toString();
        } catch (Error var12) {
            itemName = stack.getType().name();
        }
        for(Player players : Bukkit.getOnlinePlayers()) {
            if (players.hasPermission("bcore.staff.log")) {
                Command.broadcastCommandMessage(sender, ChatColor.AQUA + "Applied " + enchantment.getName() + " at level " + level + " onto " + itemName + " of " + target.getName() + '.');
                return ResultCommand.SUCCESS;
            }
        }
        return ResultCommand.SUCCESS;
    }

    @Override
    public List onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1: {
                Enchantment[] enchantments = Enchantment.values();
                ArrayList results = new ArrayList(enchantments.length);
                for (int var8 = enchantments.length, var9 = 0; var9 < var8; ++var9) {
                    Enchantment enchantment = enchantments[var9];
                    results.add(enchantment.getName());
                }
                return BukkitUtil.getCompletions(args, results);
            }
            case 3: {
                return null;
            }
            default: {
                return Collections.emptyList();
            }
        }
    }
}
