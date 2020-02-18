package xyz.baqel.bcore.manager.command.general.staff;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.ClickableUtil;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class ProfileCommand extends CustomCommand {

	public ProfileCommand() {
		super("profile", "Check information about a player.", new String[] {"whois", "info", "checkinfo", "ci"}, "/profile (player)", "bcore.cmd.profile");
	}
	
	@Override
	public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
		if(!this.hasPermission(sender, "bcore.cmd.profile") || !this.hasPermission(sender, "bcore.*")) {
			return ResultCommand.INVALID_PERMISSION;
		}
		
		if(args.length == 0) {
			this.setUsage(label + " <player>");
			return ResultCommand.INVALID_USAGE;
		}
		
		String targetName = args[0];
		Player target = Bukkit.getPlayer(targetName);
		Profile profile = Profile.getProfiles().get(target.getUniqueId());
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(profile.getUuid());
		if(!(target.isOnline())) {
			Chat.sendMessage(sender, "&cThe player must be connected to the server, in order to perform this.");
			return ResultCommand.SUCCESS;
		}
		if(profile == null) {
			Chat.sendMessage(sender, "&cAn error occurred while loading this profile.");
			return ResultCommand.SUCCESS;
		}

		Chat.sendMessage(sender, Chat.CHAT_BAR);
		Chat.sendMessage(sender, profile.getColoredUsername() + "&b's Information profile.");
		Chat.sendMessage(sender, "&bRank&7: &r" + profile.getActiveRank().getColoredName());
		Chat.sendMessage(sender, "&bStatus&7: " + (target.isOnline() ? "&aOnline" : "&cOffline"));
		Chat.sendMessage(sender, "&bGamemode&7: " + target.getGameMode());
		Chat.sendMessage(sender, "&bFly Speed&7: " + target.getFlySpeed());
		Chat.sendMessage(sender, "&bWalk Speed&7: " + target.getWalkSpeed());
		Chat.sendMessage(sender, "&bHealth&7: " + target.getHealth() + "/" + target.getMaxHealth());
		Chat.sendMessage(sender, "&bHunger&7: " + target.getFoodLevel() + "/20 (" + target.getSaturation() + " saturation)");
		Chat.sendMessage(sender, "&bOperator&7: " + (target.isOp() ? "&aYes" : "&cNo"));
		
		ClickableUtil clickableUtil = new ClickableUtil("&aStaffOptions&7: &7&o(Hover)",
				"&bVanished&7: " + (profile.isVanished() ? "&aYes" : "&cNo") + "\n" +
				"&bStaffChat&7: " + (profile.isStaffChatEnabled() ? "&aYes" : "&cNo") + "\n" +
				"&bIpAddress&7: " + (sender.isOp() ? profile.getCurrentAddress() : "&cYou are not an operator."), "");
		clickableUtil.sendToPlayer(sender);
		
//		Chat.sendMessage(sender, "&9Handle Latency&7: &r" + (target).getHandle().ping + "ms");
		if (sender.hasPermission("bcore.cmd.profile.ip")) {
			Chat.sendMessage(sender, "&bHost&7: " + target.getAddress().getHostName());
			Chat.sendMessage(sender, "&bIP Address&7: " + profile.getCurrentAddress());
		}
//		Chat.sendMessage(sender, "&9Grants&7: ");
//		for (Grant grant : profile.getGrants()) {
//			Chat.sendMessage(sender, " &f- &r" + grant.getRank().getColoredName() + (grant.isRemoved() ? " &c(Removed)" : (grant.hasExpired() ? " &c(Expired)" : " &a(Active)")));
//		}
		Chat.sendMessage(sender, Chat.CHAT_BAR);
		return ResultCommand.SUCCESS;
	}
}
