package xyz.baqel.bcore.manager.command.ranks;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.network.packet.PacketAddGrant;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.grant.Grant;
import xyz.baqel.bcore.profile.grant.event.GrantAppliedEvent;
import xyz.baqel.bcore.profile.rank.Rank;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.DurationUtil;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class GrantCommand extends CustomCommand {

	public GrantCommand() {
		super("grant", "Grant a player rank.", new String[] {"gt"}, "grant (player) (rank) (time) (reason)", "bcore.cmd.grant");
	}
	
	@Override
	public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
		if(!this.hasPermission(sender, "bcore.cmd.grant")) {
			return ResultCommand.INVALID_PERMISSION;
		}
		
		if(args.length != 3) {
			Chat.sendMessage(sender, "&cIncorrect Syntax: /grant (player) (rank) (time) (reason)");
		}
		String profileName = args[0];
		Profile profile = Profile.getByUsername(profileName);
		if(profile == null || !profile.isLoaded()) {
			Chat.sendMessage(sender, "&cCould not resolve player information...");
			return ResultCommand.SUCCESS;
		}

		String rankName = args[1];
		Rank rank = Rank.getRankByDisplayName(rankName);
		if(rank == null) {
			Chat.sendMessage(sender, "&cA rank with that name does not exist.");
			return ResultCommand.SUCCESS;
		}

		String durationTime = args[2];
		DurationUtil duration = DurationUtil.fromString(durationTime);
		if(duration.getValue() == -1L) {
			Chat.sendMessage(sender, "&cThat duration is not valid.");
			Chat.sendMessage(sender, "&cTimes: 1d2h (1 day, and 2 hours) / perm - permanent");
			return ResultCommand.SUCCESS;
		}
		
		UUID addedBy = (sender instanceof Player) ? ((Player)sender).getUniqueId() : null;
		Grant grant = new Grant(UUID.randomUUID(), rank, addedBy, System.currentTimeMillis(), this.getMessage(args), duration.getValue());
		
		profile.getGrants().add(grant);
		profile.activateNextGrant();
		
		bCore.getPlugin().getDatabaseManager().getRedisImpl().getRedis().sendPacket(new PacketAddGrant(profile.getUuid(), grant));
		
		Chat.sendMessage(sender, "&bYou applied a &r" + rank.getColoredName() + "&b grant to &r" + profile.getColoredUsername());
		
		Player player = profile.getPlayer();
		if(player != null) {
			new GrantAppliedEvent(player, grant).call();
			profile.refreshDisplayName();
			player.kickPlayer(Chat.formatMessages("&cYou have been granted a rank " + rank.getDisplayName() + ", please re-join the server."));
		}
		profile.save();
		return ResultCommand.SUCCESS;
	}
	
	private String getMessage(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 3; i < args.length; ++i) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString();
    }
}
