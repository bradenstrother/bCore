package xyz.baqel.bcore.manager.command.punishment.undo;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.network.packet.PacketBroadcastPunishment;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.punishment.Punishment;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class UnBanCommand extends CustomCommand {

	public UnBanCommand() {
		super("unban", "UnBan a player");
	}
	
	@Override
	public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
		if(!this.hasPermission(sender, "bcore.cmd.unban")) {
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
		
		if(profile.getActiveBan() == null) {
			Chat.sendMessage(sender, "&cThat player is not banned.");
			return ResultCommand.SUCCESS;
		}
		
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
		Punishment punishment = profile.getActiveBan();
		
		punishment.setPardonedAt(System.currentTimeMillis());
		punishment.setPardonedReason(reason);
		punishment.setPardoned(true);
		
		if(sender instanceof Player) {
			punishment.setPardonedBy(((Player)sender).getUniqueId());
		}
		
		profile.save();
		bCore.getPlugin().getDatabaseManager().getRedisImpl().getRedis().sendPacket(new PacketBroadcastPunishment(punishment, staffName, profile.getColoredUsername(), profile.getUuid(), silent));
		return ResultCommand.SUCCESS;
	}
}
