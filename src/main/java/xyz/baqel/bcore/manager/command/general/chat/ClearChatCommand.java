package xyz.baqel.bcore.manager.command.general.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class ClearChatCommand extends CustomCommand {
    public ClearChatCommand() {
        super("clearchat", "Clears the chat", new String[] {"cc"}, "clearchat", "bcore.cmd.clearchat");
    }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return ResultCommand.CONSOLE_ONLY;
        }
        Player player = (Player) sender;
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (!players.hasPermission("bcore.staff.bypass")) {
                for (int i = 0; i < 1000; ++i) {
                    players.sendMessage(" ");
                }
            }
        }
        Chat.broadcastMessage("&cChat has been cleared by " + Profile.getProfiles().get(player.getUniqueId()).getColoredUsername());
        return ResultCommand.SUCCESS;
    }
}
