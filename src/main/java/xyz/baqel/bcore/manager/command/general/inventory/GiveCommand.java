package xyz.baqel.bcore.manager.command.general.inventory;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.JavaUtil;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class GiveCommand extends CustomCommand {
    public GiveCommand() {
        super("give", "Gives an item to a player.", new String[] {"item"}, "give (player) <item> <quantity>", "bcore.cmd.give");
    }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return ResultCommand.CONSOLE_ONLY;
        }

        if (args.length < 2) {
            return ResultCommand.INVALID_USAGE;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            Chat.sendMessage(sender, "&cPlayer '" + args[0] + "' not found.");
            return ResultCommand.SUCCESS;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (bCore.getPlugin().getItemDB().get(args[1]) == null) {
            Chat.sendMessage(sender, "&cThe item named or with id '" + args[1] + "' not found.");
            return ResultCommand.SUCCESS;
        }
        if (args.length == 2) {
            if (!target.getInventory().addItem(new ItemStack[] { bCore.getPlugin().getItemDB().get(args[1], bCore.getPlugin().getItemDB().get(args[1]).getMaxStackSize()) }).isEmpty()) {
                Chat.sendMessage(sender, "&cThe inventory of the player is full.");
                return ResultCommand.SUCCESS;
            }
            if (target.hasPermission("bcore.staff.log.give")) {
                Chat.cmdLogger(target, "&7&ohas given &b&o" + bCore.getPlugin().getItemDB().get(args[1]).getItemMeta().getDisplayName() + " &7&oto " + Profile.getProfiles().get(target.getUniqueId()).getColoredUsername() + "&7&o.");
            }
        }
        if (args.length == 3) {
            Integer number = JavaUtil.tryParseInt(args[2]);
            if(number == null) {
                Chat.sendMessage(sender, "&cInvalid syntax: Integer valid '1-9999'");
                return ResultCommand.SUCCESS;
            }

            if (!target.getInventory().addItem(new ItemStack[] { bCore.getPlugin().getItemDB().get(args[1], number) }).isEmpty()) {
                Chat.sendMessage(sender, "&cThe inventory of the player is full.");
            }
            if (target.hasPermission("bcore.staff.log.give")) {
                Chat.cmdLogger(target, "&7&ohas given &b&o" + args[2] + "&7&o, &b&o" + bCore.getPlugin().getItemDB().get(args[1]).getItemMeta().getDisplayName() + " &7&oto " + Profile.getProfiles().get(target.getUniqueId()).getColoredUsername() + "&7&o.");
            }
        }
        return ResultCommand.SUCCESS;
    }
}
