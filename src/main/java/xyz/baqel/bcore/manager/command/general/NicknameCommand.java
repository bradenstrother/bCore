package xyz.baqel.bcore.manager.command.general;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class NicknameCommand extends CustomCommand {

	public NicknameCommand() {
		super("nickname", "Sets a players nickname", new String[] {"nick"});
	}

	@Override
	public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
		if(this.isConsole(sender)) {
			return ResultCommand.CONSOLE_ONLY;
		}
		if(!this.hasPermission(sender, "bcore.cmd.nickname")) {
			return ResultCommand.INVALID_PERMISSION;
		}
		
		Player player = (Player) sender;
		
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		
		if(args.length == 0) {
			if(profile.getNickName() == null) {
				Chat.sendMessage(sender, "&9You do not currently have a nickname, to set a nickname use:");
				Chat.sendMessage(sender, "&9Syntax: &f(/" + label + " <name/none>)");
				return ResultCommand.SUCCESS;
			}
			
			Chat.sendMessage(sender, "&9Your nickname is currently &r" + profile.getNickName() + " &9to reset your nickname use:");
			Chat.sendMessage(sender, "&9Syntax: &f(/" + label + " none)");
			return ResultCommand.SUCCESS;
		}
		
		String nickname = args[0];
		
		if(args.length == 1) {
			if(nickname.equalsIgnoreCase("none")) {
				profile.setNickName(null);
				profile.refreshDisplayName();
				Chat.sendMessage(sender, "&9Your nickname has been reset.");
				return ResultCommand.SUCCESS;
			}
			if(!nickname.equalsIgnoreCase(player.getName()) && (nickname.isEmpty() || nickname.length() > 16)) {
				Chat.sendMessage(sender, "&cInvalid nickname.");
				return ResultCommand.SUCCESS;
			}
			
			if(nickname.equalsIgnoreCase(player.getName())) {
				Chat.sendMessage(sender, "&cA player already exists with that name.");
				return ResultCommand.SUCCESS;
			}
			
			profile.setNickName(nickname);
			profile.refreshDisplayName();
			Chat.sendMessage(sender, "&9Your nickname is now &r" + profile.getNickName());
			return ResultCommand.SUCCESS;
		}
		return ResultCommand.SUCCESS;
	}
}
