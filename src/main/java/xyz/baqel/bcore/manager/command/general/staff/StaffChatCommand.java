package xyz.baqel.bcore.manager.command.general.staff;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.network.packet.PacketStaffChat;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class StaffChatCommand extends CustomCommand {

	public StaffChatCommand() {
		super("staffchat", "Change your chat mode to staff or global.", new String[] {"sc"});
	}

	@Override
	public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
		if(this.isConsole(sender)) {
			return ResultCommand.CONSOLE_ONLY;
		}
		
		if(!this.hasPermission(sender, "bcore.cmd.staffchat")) {
			return ResultCommand.INVALID_PERMISSION;
		}

		Player player = (Player) sender;
		Profile profile = Profile.getProfiles().get(player.getUniqueId());

		if(args.length == 0) {
			profile.setStaffChatEnabled(!profile.isStaffChatEnabled());
			Chat.sendMessage(sender, "&bYour chat mode has been changed to " + (profile.isStaffChatEnabled() ? "&aStaff" : "&cGlobal"));
			return  ResultCommand.SUCCESS;
		}
		profile.setStaffChatEnabled(true);
		String server = bCore.getPlugin().getRootConfig().getConfiguration().getString("server.id");
		String message = StringUtils.join(args, ' ', 0, args.length);
		bCore.getPlugin().getDatabaseManager().getRedisImpl().getRedis().sendPacket(new PacketStaffChat(server, profile.getColoredUsername(), message));

		return ResultCommand.SUCCESS;
	}

}
