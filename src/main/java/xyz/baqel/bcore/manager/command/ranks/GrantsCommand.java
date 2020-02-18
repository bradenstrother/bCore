package xyz.baqel.bcore.manager.command.ranks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.grant.menu.GrantsMenu;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class GrantsCommand extends CustomCommand {

	public GrantsCommand() {
		super("grants", "Open grants history.", new String[] {"gts"});
	}
	
	@Override
	public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
		if(this.isConsole(sender)) {
			return ResultCommand.CONSOLE_ONLY;
		}	
		
		if(!this.hasPermission(sender, "bcore.cmd.grants")) {
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
		new GrantsMenu(profile).openMenu(player);
		return ResultCommand.SUCCESS;
	}
}
