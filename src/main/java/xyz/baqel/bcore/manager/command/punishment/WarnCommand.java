package xyz.baqel.bcore.manager.command.punishment;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.network.packet.PacketBroadcastPunishment;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.punishment.Punishment;
import xyz.baqel.bcore.profile.punishment.PunishmentType;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class WarnCommand extends CustomCommand {

	public WarnCommand() {
		super("warn", "Warn a player");
	}
	
	@Override
	public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
		if(!this.hasPermission(sender, "bcore.cmd.warn")) {
			return ResultCommand.INVALID_PERMISSION;
		}
		
		if(args.length < 2) {
			this.setUsage(label + " <player> <reason> <-s>");
			return ResultCommand.INVALID_USAGE;
		}
		
		String profileName = args[0];
		Profile profile = Profile.getByUsername(profileName);
		if(profile == null || !profile.isLoaded()) {
			Chat.sendMessage(sender, "&cCould not resolve player information...");
			return ResultCommand.SUCCESS;
		}
		
		Player player = Bukkit.getPlayer(profileName);
	
		boolean silent = args[args.length - 1].equalsIgnoreCase("-s");
		
		StringBuilder sb = new StringBuilder();

        for (int i = 1; i < (silent ? args.length - 1 : args.length); i++) {
            sb.append(args[i]).append(" ");
        }

        String reason = sb.toString().trim();

        if (reason.equalsIgnoreCase("-s")) {
           	Chat.sendMessage(sender, "&cPlease provide a valid reason.");
            return ResultCommand.SUCCESS;
        }
		
		String staffName = (sender instanceof Player) ? Profile.getProfiles().get(((Player) sender).getUniqueId()).getColoredUsername() : (Chat.DARK_RED + "Console");
		Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.WARN, System.currentTimeMillis(), reason, -1L);
		if(sender instanceof Player) {
			punishment.setAddedBy(((Player)sender).getUniqueId());
		}
		
		profile.getPunishments().add(punishment);
		profile.save();
		
		if(player != null) {
			Chat.sendMessage(player, "&cYou have been warned for: &f" + reason);
		}
		
		bCore.getPlugin().getDatabaseManager().getRedisImpl().getRedis().sendPacket(new PacketBroadcastPunishment(punishment, staffName, profile.getColoredUsername(), profile.getUuid(), silent));
		return ResultCommand.SUCCESS;
	}
}
