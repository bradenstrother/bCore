package xyz.baqel.bcore.manager.command.general.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class SpectatorCommand extends CustomCommand {
    public SpectatorCommand() { super("spectator", "Changes the users gamemode to spectator.", new String[] {"gmsp"}, "spectator (player)", "bcore.cmd.gamemode"); }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return ResultCommand.CONSOLE_ONLY;
        }

        Player player = (Player) sender;

        if(args.length == 0) {
            if(player.getGameMode() == GameMode.SPECTATOR) {
                Chat.sendMessage(player, "&cYou are already in gamemode spectator.");
                return ResultCommand.SUCCESS;
            }

            if(player.hasPermission("bcore.staff.log.gm")) {
                Chat.cmdLogger(player, "&c&ois now in spectator mode.");
            }

//			Chat.sendMessage(player, "&eYour game mode is now in &dsurvival&e.");
            player.setGameMode(GameMode.SPECTATOR);
            return ResultCommand.SUCCESS;
        }

        if(args.length == 1) {
            if(!player.hasPermission(this.getPermission() + ".others")) {
                return ResultCommand.INVALID_PERMISSION;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if(target == null) {
                Chat.sendMessage(player, "&cPlayer '" +  args[0] + "' not found.");
                return ResultCommand.SUCCESS;
            }

            if(target.equals(player)) {
                Chat.sendMessage(player, "&cYou can not do this to yourself.");
                return ResultCommand.SUCCESS;
            }

            if(target.getGameMode() == GameMode.SPECTATOR) {
                Chat.sendMessage(player, Profile.getProfiles().get(target.getUniqueId()).getColoredUsername() + " &cis already in gamemode spectator.");
                return ResultCommand.SUCCESS;
            }

            if(target.hasPermission("bcore.staff.log.gm")) {
                Chat.cmdLogger(target, "&c&ohas changed " + Profile.getProfiles().get(target.getUniqueId()).getColoredUsername() + "&c&o's gamemode to spectator.");
            }

            Chat.sendMessage(target, Profile.getProfiles().get(player.getUniqueId()).getColoredUsername() + " &a&ohas changed your game mode to &d&ospectator&a&o.");

            target.setGameMode(GameMode.SURVIVAL);
            return ResultCommand.SUCCESS;
        }
        return ResultCommand.SUCCESS;
    }
}
