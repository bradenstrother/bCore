package xyz.baqel.bcore.manager.command.general.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class ToggleReportCommand extends CustomCommand {

	public ToggleReportCommand() {
		super("togglereport", "Toggle report messages.");
	}

	@Override
	public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
		if(this.isConsole(sender)) {
			return ResultCommand.CONSOLE_ONLY;
		}
		
		if(!this.hasPermission(sender, "bcore.cmd.togglereport")) {
			return ResultCommand.INVALID_PERMISSION;
		}
		
		Player player = (Player) sender;
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		profile.setReceiveReportEnabled(!profile.isReceiveReportEnabled());
		Chat.sendMessage(sender, (profile.isReceiveReportEnabled() ? "&bNow you can receive reports" : "&cYou will not be able to receive the reports."));
		return ResultCommand.SUCCESS;
	}
}
