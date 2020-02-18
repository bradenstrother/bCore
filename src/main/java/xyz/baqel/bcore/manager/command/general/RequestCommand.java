package xyz.baqel.bcore.manager.command.general;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.network.packet.PacketRequest;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class RequestCommand extends CustomCommand {

	private TObjectLongMap<UUID> requestCooldown;
	
	public RequestCommand() {
		super("request", "Send a message of assistance to the staff.", new String[] {"helpop"}, "request (message)", "bcore.cmd.request");
		this.requestCooldown = new TObjectLongHashMap<>();
	}

	@Override
	public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
		if(this.isConsole(sender)) {
			return ResultCommand.CONSOLE_ONLY;
		}

		if(args.length < 1) {
			this.setUsage(label + " <message>");
			return ResultCommand.INVALID_USAGE;
		}

		Player player = (Player) sender;
		Profile profile = Profile.getProfiles().get(player.getUniqueId());

		UUID uuid = player.getUniqueId();
    	long timestamp = requestCooldown.get(uuid);
		long millis = System.currentTimeMillis();
		long remaining = timestamp == requestCooldown.getNoEntryValue() ? -1L : timestamp - millis;

		if(remaining > 0) {
			Chat.sendMessage(sender, "&cPlease wait before doing this again.");
		} else {
			String message = StringUtils.join(args, ' ', 0, args.length);
			this.requestCooldown.put(uuid, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30L));
			bCore.getPlugin().getDatabaseManager().getRedisImpl().getRedis().sendPacket(new PacketRequest(profile.getColoredUsername(), message));
			Chat.sendMessage(sender, "&aYour request has been submitted! Please be patient.");
		}
		return ResultCommand.SUCCESS;
	}
}
