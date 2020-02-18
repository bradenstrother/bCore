package xyz.baqel.bcore.manager.command.general;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.network.packet.PacketReport;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class ReportCommand extends CustomCommand {

	private TObjectLongMap<UUID> reportCooldown;
	
	public ReportCommand() {
		super("report", "Send a report to the staff.", new String[] {}, "report (player) (reason)", "bcore.cmd.report");
		this.reportCooldown = new TObjectLongHashMap<>();
	}

	public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
		if(this.isConsole(sender)) {
			return ResultCommand.CONSOLE_ONLY;
		}

		if(args.length < 2) {
			this.setUsage(label + " <target> <reason>");
			return ResultCommand.INVALID_USAGE;
		}

		Player player = (Player) sender;
		Profile profile = Profile.getProfiles().get(player.getUniqueId());

		Player target = Bukkit.getPlayer(args[0]);
		if(target == null) {
			this.setPlayer(args[0]);
			return ResultCommand.INVALID_PLAYER;
		}
		Profile profileTarget = Profile.getProfiles().get(target.getUniqueId());

		UUID uuid = player.getUniqueId();
    	long timestamp = reportCooldown.get(uuid);
		long millis = System.currentTimeMillis();
		long remaining = timestamp == reportCooldown.getNoEntryValue() ? -1L : timestamp - millis;

		if(remaining > 0) {
			Chat.sendMessage(sender, "&cPlease wait before doing this again.");
		} else {
			String reason = StringUtils.join(args, ' ', 1, args.length);
			this.reportCooldown.put(uuid, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30L));
			bCore.getPlugin().getDatabaseManager().getRedisImpl().getRedis().sendPacket(new PacketReport(profile.getColoredUsername(),profileTarget.getColoredUsername(), reason));
			Chat.sendMessage(sender, "&aThank you! Your report has been submitted, be patient.");
		}
		return ResultCommand.SUCCESS;
	}
}
