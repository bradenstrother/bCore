package xyz.baqel.bcore.manager.command.general.teleport;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class TeleportHereCommand extends CustomCommand {
    public TeleportHereCommand() {
        super("teleporthere", "Teleports a player to yourself!", new String[] {"tphere", "tph"}, "/teleporthere <player>", "bcore.cmd.tphere");
    }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return ResultCommand.CONSOLE_ONLY;
        }

        if(args.length == 0) {
            return ResultCommand.INVALID_USAGE;
        }

        Player target = Bukkit.getPlayer(args[0]);
        Player player = (Player) sender;

        if(args.length == 1) {
            if(target == null) {
                Chat.sendMessage(sender, "&cPlayer '" + args[0] + "' not found.");
                return ResultCommand.SUCCESS;
            }

            if(target.equals(sender)) {
                Chat.sendMessage(sender, "&cCannot teleport you to yourself.");
                return ResultCommand.SUCCESS;
            }
            Profile profile = Profile.getProfiles().get(target.getUniqueId());
            Chat.cmdLogger(player, "&7&ohas teleported " + profile.getColoredUsername() + " &7&oto their location.");
            target.teleport((Entity) sender);
        }
        return ResultCommand.SUCCESS;
    }
}
