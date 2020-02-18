package xyz.baqel.bcore.manager.command.general;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

import java.util.Collection;
import java.util.Iterator;

@SuppressWarnings("InfiniteLoopStatement")
public class ListCommand extends CustomCommand {
    public ListCommand() { super("list", "View online players.", new String[] {"who"}, "list", "bcore.cmd.list"); }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        StringBuilder online = new StringBuilder();
        Collection players = Bukkit.getServer().getOnlinePlayers();
        Iterator var6 = players.iterator();
        int staff = 0;
        for (Player player1 : Bukkit.getServer().getOnlinePlayers()) {
            if (!player1.hasPermission("bcore.staff.bypass")) continue;
            ++staff;
        }
        do {
            if (!var6.hasNext()) {
                Chat.sendMessage(sender, ChatColor.AQUA + "There are currently " + ChatColor.RED + staff + ChatColor.AQUA + " staff member(s) logged on:");
                Chat.sendMessage(sender, ChatColor.AQUA + "There are currently " + ChatColor.GREEN + players.size() + ChatColor.AQUA + " player(s) logged on:");
                Chat.sendMessage(sender, online.toString());
            }
            Player player = (Player) var6.next();
            if (sender instanceof Player && !((Player) sender).canSee(player)) continue;
            if (online.length() > 0) online.append(ChatColor.GRAY + ", " + ChatColor.RESET);
            online.append(player.getDisplayName());
        } while (true);
    }
}
