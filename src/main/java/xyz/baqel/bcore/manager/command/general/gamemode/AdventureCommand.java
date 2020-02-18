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

public class AdventureCommand extends CustomCommand {
    public AdventureCommand() {
        super("adventure", "Changes the users gamemode to adventure mode.", new String[] {"gma"}, "adventure (player)", "bcore.cmd.gamemode");
    }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return ResultCommand.CONSOLE_ONLY;
        }

        Player player = (Player) sender;

        if(args.length == 0) {
            if(player.getGameMode() == GameMode.ADVENTURE) {
                Chat.sendMessage(player, "&cYou are already in gamemode adventure.");
                return ResultCommand.SUCCESS;
            }

            if(player.hasPermission("bcore.staff.log.gm")) {
                Chat.cmdLogger(player, "&c&ois now in adventure mode.");
            }

//			CM.sendMessage(player, "&eYour game mode is now in &dadventure&e.");
            player.setGameMode(GameMode.ADVENTURE);
            return ResultCommand.SUCCESS;
        }

        if(args.length == 1) {
            if(!player.hasPermission("bcore.cmd.gamemode.others")) {
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

            if(target.getGameMode() == GameMode.ADVENTURE) {
                Chat.sendMessage(player, Profile.getProfiles().get(target.getUniqueId()).getColoredUsername() + " is already in gamemode adventure.");
                return ResultCommand.SUCCESS;
            }

                if(player.hasPermission("bcore.staff.log.gm")) {
                    Chat.cmdLogger(player, "&c&ohas changed " + Profile.getProfiles().get(target.getUniqueId()).getColoredUsername() + "&c&o's gamemode to adventure.");
            }

            Chat.sendMessage(target, Profile.getProfiles().get(player.getUniqueId()).getColoredUsername() + " &a&ohas changed your game mode to &d&oadventure&a&o.");

            target.setGameMode(GameMode.ADVENTURE);
            return ResultCommand.SUCCESS;
        }
        return ResultCommand.SUCCESS;    }
}
