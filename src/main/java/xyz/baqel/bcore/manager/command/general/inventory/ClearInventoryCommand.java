package xyz.baqel.bcore.manager.command.general.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;
import xyz.baqel.bcore.util.item.ItemMaker;

public class ClearInventoryCommand extends CustomCommand {
    public ClearInventoryCommand() {
        super("clear", "Clears a players inventory.", new String[] {"ci"}, "/clear <player>", "bcore.cmd.clear");
    }
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return ResultCommand.CONSOLE_ONLY;
        }
        if (args.length == 0) {
            Player p = (Player) sender;
            PlayerInventory inv = p.getInventory();

            inv.clear();
            inv.setArmorContents(new ItemStack[] { new ItemMaker(Material.AIR).build(), new ItemMaker(Material.AIR).build(), new ItemMaker(Material.AIR).build(), new ItemMaker(Material.AIR).build()});
            Chat.sendMessage(p, "&aCleared inventory");
            return ResultCommand.SUCCESS;
        }
        if (args.length == 1) {
            if (!(sender.hasPermission("bcore.cmd.clear.others"))) {
                return ResultCommand.INVALID_PERMISSION;
            }
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null) {
                Chat.sendMessage(sender, "&cPlayer '" + args[0] + "' was not found!");
                return ResultCommand.SUCCESS;
            }
            PlayerInventory inv = p.getInventory();

            inv.clear();
            inv.setArmorContents(new ItemStack[] { new ItemMaker(Material.AIR).build(), new ItemMaker(Material.AIR).build(), new ItemMaker(Material.AIR).build(), new ItemMaker(Material.AIR).build()});
            Chat.cmdLogger(p, "&7&ocleared " + Profile.getProfiles().get(p.getUniqueId()).getColoredUsername() + "&7&o's inventory.");
            return ResultCommand.SUCCESS;
        }
        return ResultCommand.SUCCESS;
    }
}
