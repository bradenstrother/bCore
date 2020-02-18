package xyz.baqel.bcore.manager.command.punishment;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.network.packet.PacketBroadcastPunishment;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.punishment.Punishment;
import xyz.baqel.bcore.profile.punishment.PunishmentType;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.DurationUtil;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class MuteCommand extends CustomCommand {

	public MuteCommand() {
		super("mute", "Silence the chat.", new String[] {"silence"});
	}
	
	@Override
	public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
		if(!this.hasPermission(sender, "bcore.cmd.mute")) {
			return ResultCommand.INVALID_PERMISSION;
		}
		
		if(args.length < 3) {
			this.setUsage(label + " <player> <duration> <reason> <-s>");
			return ResultCommand.INVALID_USAGE;
		}
		
		String profileName = args[0];
		Profile profile = Profile.getByUsername(profileName);
		if(profile == null || !profile.isLoaded()) {
			Chat.sendMessage(sender, "&cCould not resolve player information...");
			return ResultCommand.SUCCESS;
		}
		
		if(profile.getActiveMute() != null) {
			Chat.sendMessage(sender, "&cThat player is already muted.");
			return ResultCommand.SUCCESS;
		}
		
		String durationTime = args[1];
		DurationUtil duration = DurationUtil.fromString(durationTime);
		if(duration.getValue() == -1L) {
			Chat.sendMessage(sender, "&cThat duration is not valid.");
			Chat.sendMessage(sender, "&cTimes: 1d2h (1 day, and 2 hours) / perm - permanent");
			return ResultCommand.SUCCESS;
		}
		
		boolean silent = args[args.length - 1].equalsIgnoreCase("-s");
		
		StringBuilder sb = new StringBuilder();

        for (int i = 2; i < (silent ? args.length - 1 : args.length); i++) {
            sb.append(args[i]).append(" ");
        }

        String reason = sb.toString().trim();

        if (reason.equalsIgnoreCase("-s")) {
           	Chat.sendMessage(sender, "&cPlease provide a valid reason.");
            return ResultCommand.SUCCESS;
        }
		
		String staffName = (sender instanceof Player) ? Profile.getProfiles().get(((Player) sender).getUniqueId()).getColoredUsername() : (Chat.DARK_RED + "Console");
		Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.MUTE, System.currentTimeMillis(), reason, duration.getValue());
		if(sender instanceof Player) {
			punishment.setAddedBy(((Player)sender).getUniqueId());
		}
		profile.getPunishments().add(punishment);
		profile.save();
		
		Player player = profile.getPlayer();
		if(player != null) {
			Chat.sendMessage(player, "&cYou have been muted by " + staffName + " &cfor: " + reason);
			if(!punishment.isPermanent()) {
				Chat.sendMessage(player, "&cThis mite will expire in &f" + punishment.getTimeRemaining());
			}
		}
		
		bCore.getPlugin().getDatabaseManager().getRedisImpl().getRedis().sendPacket(new PacketBroadcastPunishment(punishment, staffName, profile.getColoredUsername(), profile.getUuid(), silent));
		return ResultCommand.SUCCESS;
	}
}
