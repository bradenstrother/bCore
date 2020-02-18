package xyz.baqel.bcore.manager.command.punishment.check;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.punishment.menu.PunishmentsMenu;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class CheckCommand extends CustomCommand {

	public CheckCommand() { super("check", "Get history of punishment to player's.", new String[] {"ce", "history"} ); }
	
	@Override
	public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
		if(this.isConsole(sender)) {
			return ResultCommand.CONSOLE_ONLY;
		}
		
		if(!this.hasPermission(sender, "bcore.cmd.check")) {
			return ResultCommand.INVALID_PERMISSION;
		}
		
		if(args.length == 0) {
			this.setUsage(label + " <player>");
			return ResultCommand.INVALID_USAGE;
		}
		
		String profileName = args[0];
		Profile profile = Profile.getByUsername(profileName);
		if(profile == null || !profile.isLoaded()) {
			Chat.sendMessage(sender, "&cCould not resolve player information...");
			return ResultCommand.SUCCESS;
		}
		
		Player player = (Player) sender;
		new PunishmentsMenu(profile).openMenu(player);
		return ResultCommand.SUCCESS;
	}
}
