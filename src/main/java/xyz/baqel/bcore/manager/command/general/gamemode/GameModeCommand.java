package xyz.baqel.bcore.manager.command.general.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.baqel.bcore .profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class GameModeCommand extends CustomCommand {
    public GameModeCommand() { super("gamemode", "Change a players gamemode.", new String[] {"gm"}, "gamemode (player) <gamemode>", "bcore.cmd.gamemode"); }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return ResultCommand.CONSOLE_ONLY;
        }

        if(args.length < 1) {
            return ResultCommand.INVALID_USAGE;
        }

        Player player = (Player) sender;

        if(args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1")) {
            if(args.length == 1) {
                if(player.getGameMode() == GameMode.CREATIVE) {
                    Chat.sendMessage(player, "&cYou are already in gamemode creative.");
                    return ResultCommand.SUCCESS;
                }

                Chat.cmdLogger(player, "&7&ois now in &acreative &7&omode.");
                player.setGameMode(GameMode.CREATIVE);
                return ResultCommand.SUCCESS;
            }
            if(args.length == 2) {
                if(!player.hasPermission(this.getPermission() + ".others")) {
                    return ResultCommand.INVALID_PERMISSION;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if(target == null) {
                    Chat.sendMessage(player, "&cPlayer '" +  args[1] + "' not found.");
                    return ResultCommand.SUCCESS;
                }

//                if(target.equals(player)) {
//                    CM.sendMessage(player, "&cYou can not do this to yourself.");
//                    return ResultCommand.SUCCESS;
//                }

                if(target.getGameMode() == GameMode.CREATIVE) {
                    Chat.sendMessage(player, "&c" + target.getName() + " is already in gamemode creative.");
                    return ResultCommand.SUCCESS;
                }

                Chat.cmdLogger(target, "&7&ohas changed " + Profile.getProfiles().get(target.getUniqueId()).getColoredUsername() + "&7&o's gamemode to &acreative.");

                Chat.sendMessage(target, Profile.getProfiles().get(((Player) sender).getUniqueId()).getColoredUsername() + " &a&ohas changed your game mode to &acreative&a&o.");

                target.setGameMode(GameMode.CREATIVE);
                return ResultCommand.SUCCESS;
            }
            return ResultCommand.SUCCESS;
        }
        if(args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("3")) {
            if(args.length == 3) {
                if(player.getGameMode() == GameMode.SPECTATOR) {
                    Chat.sendMessage(player, "&cYou are already in gamemode spectator.");
                    return ResultCommand.SUCCESS;
                }

                Chat.cmdLogger(player, "&7&ois now in &aspectator &7&omode.");
                player.setGameMode(GameMode.SPECTATOR);
                return ResultCommand.SUCCESS;
            }
            if(args.length == 3) {
                if(!player.hasPermission(this.getPermission() + ".others")) {
                    return ResultCommand.INVALID_PERMISSION;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if(target == null) {
                    Chat.sendMessage(player, "&cPlayer '" +  args[1] + "' not found.");
                    return ResultCommand.SUCCESS;
                }

//                if(target.equals(player)) {
//                    CM.sendMessage(player, "&cYou can not do this to yourself.");
//                    return ResultCommand.SUCCESS;
//                }

                if(target.getGameMode() == GameMode.SPECTATOR) {
                    Chat.sendMessage(player, "&c" + target.getName() + " is already in gamemode spectator.");
                    return ResultCommand.SUCCESS;
                }

                Chat.cmdLogger(player, "&7&ohas changed " + Profile.getProfiles().get(target.getUniqueId()).getColoredUsername() + "&7&o's gamemode to &aspectator&7&o.");
                Chat.sendMessage(target, Profile.getProfiles().get(((Player) sender).getUniqueId()).getColoredUsername() + " &7&ohas changed your game mode to &d&ospectator&7&o.");

                target.setGameMode(GameMode.CREATIVE);
                return ResultCommand.SUCCESS;
            }
            return ResultCommand.SUCCESS;
        }
        if(args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("0")) {
            if(args.length == 1) {
                if(player.getGameMode() == GameMode.SURVIVAL) {
                    Chat.sendMessage(player, "&cYou are already in gamemode survival.");
                    return ResultCommand.SUCCESS;
                }

                if(player.hasPermission("bcore.staff.log.gm")) {
                    Chat.cmdLogger(player, "&7&ois now in &a&osurvival &7&omode.");
                }

//				CM.sendMessage(player, "&eYour game mode is now in &dsurvival&e.");

                player.setGameMode(GameMode.SURVIVAL);
                return ResultCommand.SUCCESS;
            }
            if(args.length == 2) {
                if(!player.hasPermission(this.getPermission() + ".others")) {
                    return ResultCommand.INVALID_PERMISSION;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if(target == null) {
                    Chat.sendMessage(player, "&cPlayer '" +  args[1] + "' not found.");
                    return ResultCommand.SUCCESS;
                }

                if(target.equals(player)) {
                    Chat.sendMessage(player, "&cYou can not do this to yourself.");
                    return ResultCommand.SUCCESS;
                }

                if(target.getGameMode() == GameMode.SURVIVAL) {
                    Chat.sendMessage(player, "&c" + target.getName() + " is already in gamemode survival.");
                    return ResultCommand.SUCCESS;
                }

                Chat.cmdLogger(player, "&7&ohas changed " + Profile.getProfiles().get(target.getUniqueId()).getColoredUsername() + "&7&o's gamemode to &asurvival&7&o.");

                Chat.sendMessage(target,  Profile.getProfiles().get(((Player)sender).getUniqueId()).getColoredUsername() + " &a&ohas changed your game mode to &asurvival&a&o.");

                target.setGameMode(GameMode.SURVIVAL);
                return ResultCommand.SUCCESS;
            }
            return ResultCommand.SUCCESS;
        }
        if(args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("2")) {
            if(args.length == 1) {
                if(player.getGameMode() == GameMode.ADVENTURE) {
                    Chat.sendMessage(player, "&cYou are already in gamemode adventure.");
                    return ResultCommand.SUCCESS;
                }

                Chat.cmdLogger(player, "&7&ois now in &aadventure &7&omode");
                player.setGameMode(GameMode.ADVENTURE);
                return ResultCommand.SUCCESS;
            }
            if(args.length == 2) {
                if(!player.hasPermission(this.getPermission() + ".others")) {
                    return ResultCommand.INVALID_PERMISSION;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if(target == null) {
                    Chat.sendMessage(player, "&cPlayer '" +  args[1] + "' not found.");
                    return ResultCommand.SUCCESS;
                }

                if(target.equals(player)) {
                    Chat.sendMessage(player, "&cYou can not do this to yourself.");
                    return ResultCommand.SUCCESS;
                }

                if(target.getGameMode() == GameMode.ADVENTURE) {
                    Chat.sendMessage(player, "&c" + target.getName() + " is already in gamemode adventure.");
                    return ResultCommand.SUCCESS;
                }

                Chat.cmdLogger(player, "&7&ohas changed " + Profile.getProfiles().get(target.getUniqueId()).getColoredUsername() + "&7&o's gamemode to &aadventure&7&o.");

                Chat.sendMessage(target,  Profile.getProfiles().get(player.getUniqueId()).getColoredUsername() + " &a&ohas changed your game mode to &aadventure&a&o.");

                target.setGameMode(GameMode.ADVENTURE);
                return ResultCommand.SUCCESS;
            }
        } else {
            Chat.sendMessage(player, "&cInvalid gamemode: '" + args[0] + "' not found.");
        }
        return ResultCommand.SUCCESS;
    }
}
