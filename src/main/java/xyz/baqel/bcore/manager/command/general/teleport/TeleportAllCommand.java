package xyz.baqel.bcore.manager.command.general.teleport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class TeleportAllCommand extends CustomCommand {
    public TeleportAllCommand() {
        super("TeleportAllCommand", "Teleports all players on the server to you!", new String[] {"tpall"}, "/teleportall", "bcore.cmd.tpall");
    }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return ResultCommand.CONSOLE_ONLY;
        }
        Player player = (Player) sender;

            if(player.hasPermission("bcore.staff.log.tp.all")) {
                Chat.cmdLogger(player, "&7&ohas teleported all players to their location.");

                if (!player.equals(player)) {
                    player.canSee(player);
                }
            }
        return ResultCommand.SUCCESS;
    }
}
