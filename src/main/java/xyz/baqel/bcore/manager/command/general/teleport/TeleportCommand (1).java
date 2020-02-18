package xyz.baqel.bcore.manager.command.general.teleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import xyz.baqel.bcore.util.BukkitUtil;
import xyz.baqel.bcore.util.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class TeleportCommand extends CustomCommand {
    public TeleportCommand() {
        super("teleport", "Teleport to a player or  position", new String[] {"tp"}, "/tp <player/location> <target>", "bcore.cmd.tp");
    }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1 || args.length > 4) {
            return ResultCommand.INVALID_USAGE;
        }
        Player targetA;
        if (args.length == 1 || args.length == 3) {
            if (!(sender instanceof Player)) {
                return ResultCommand.INVALID_USAGE;
            }
            targetA = (Player)sender;
        }
        else {
            targetA = BukkitUtil.playerWithNameOrUUID(args[0]);
        }
        if (targetA == null) {
            Chat.sendMessage(sender, "&cPlayer '" + args[0] + "' not found.");
            return ResultCommand.SUCCESS;
        }
        if (args.length < 3) {
            Player targetALocation = Bukkit.getPlayer(args[args.length - 1]);
            if (targetALocation == null) {
                Chat.sendMessage(sender, "&cPlayer '" + args[args.length - 1] + "' not found.");
                return ResultCommand.SUCCESS;
            }
            if (targetA.equals(targetALocation)) {
                Chat.sendMessage(sender, "&cThe teleportee and teleported are the same player.");
                return ResultCommand.SUCCESS;
            }
            if (targetA.teleport(targetALocation, PlayerTeleportEvent.TeleportCause.COMMAND)) {
                    if(targetA.hasPermission("bcore.staff.log.tp")) {
                        Chat.cmdLogger(targetA.getPlayer(), "&7&ohas teleported to " + targetALocation.getName() + "&7&o.");
                    }
            } else {
                Chat.sendMessage(sender, "&cFailed to teleport you to " + targetALocation.getName() + '.');
            }
        }
        else if (targetA.getWorld() != null) {
            Location targetALocation = targetA.getLocation();
            double x = this.getCoordinate(sender, targetALocation.getX(), args[args.length - 3]);
            double y = this.getCoordinate(sender, targetALocation.getY(), args[args.length - 2], 0, 0);
            double z = this.getCoordinate(sender, targetALocation.getZ(), args[args.length - 1]);
            if (x == -3.0000001E7 || y == -3.0000001E7 || z == -3.0000001E7) {
                Chat.sendMessage(sender, "&cPlease provide a valid location.");
                return ResultCommand.SUCCESS;
            }
            targetALocation.setX(x);
            targetALocation.setY(y);
            targetALocation.setZ(z);
            if (targetA.teleport(targetALocation, PlayerTeleportEvent.TeleportCause.COMMAND)) {
                if(targetA.hasPermission("bcore.staff.log.tp")) {
                    Chat.cmdLogger(targetA.getPlayer(), String.format("&7&ohas been teleported to&a %.2f, %.2f, %.2f", targetA.getName(), x, y, z));
                }
            } else {
                Chat.sendMessage(sender, "&cFailed to teleport you.");
            }
        }
        return ResultCommand.SUCCESS;
    }

    private double getCoordinate(CommandSender sender, double current, String input) {
        return this.getCoordinate(sender, current, input, -30000000, 30000000);
    }

    private double getCoordinate(CommandSender sender, double current, String input, int min, int max) {
        boolean relative = input.startsWith("~");
        double n2 = 0;
        if (relative) {
            n2 = current;
        }
        double result = n2;
        if (!relative || input.length() > 1) {
            boolean exact = input.contains(".");
            if (relative) {
                input = input.substring(1);
            }
            double testResult;
            if ((testResult = VanillaCommand.getDouble(sender, input)) == -3.0000001E7) {
                return -3.0000001E7;
            }
            result += testResult;
            if (!exact && !relative) {
                result += 0.5;
            }
        }
        if (min != 0 || max != 0) {
            if (result < min) {
                result = -3.0000001E7;
            }
            if (result > max) {
                result = -3.0000001E7;
            }
        }
        return result;
    }
}
