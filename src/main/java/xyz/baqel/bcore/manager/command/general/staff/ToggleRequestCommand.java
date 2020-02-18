package xyz.baqel.bcore.manager.command.general.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class ToggleRequestCommand extends CustomCommand {

	public ToggleRequestCommand() {
		super("togglerequest", "Toggle request message.");
	}

	@Override
	public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
		if(this.isConsole(sender)) {
			return ResultCommand.CONSOLE_ONLY;
		}
		
		if(!this.hasPermission(sender, "bcore.cmd.togglerequest")) {
			return ResultCommand.INVALID_PERMISSION;
		}
		
		Player player = (Player) sender;
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		profile.setReceiveRequestEnabled(!profile.isReceiveRequestEnabled());
		
		Chat.sendMessage(sender, (profile.isReceiveRequestEnabled() ? "&bNow you can receive requests." : "&cYou will not be able to receive requests."));
		return ResultCommand.SUCCESS;
	}
}
