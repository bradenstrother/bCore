package xyz.baqel.bcore.manager.command.general.chat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.moderation.modifier.type.ChatSpeedModifier;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.DateUtil;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class SlowChatCommand extends CustomCommand {
    public SlowChatCommand() {
        super("slowchat", "Delay chat messages", new String[] {"delay"}, "slowchat <1y1m1w1d1h1m1s>", "bcore.cmd.slowchat");
    }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        xyz.baqel.bcore.moderation.Chat chat = bCore.getPlugin().getChat();
        Player player = (Player) sender;
        if (args.length == 0) {
            return ResultCommand.INVALID_USAGE;
        }
        long duration;
        try {
            duration = System.currentTimeMillis() - DateUtil.parseDateDiff(args[0], false);
        }
        catch (Exception e) {
            Chat.sendMessage(sender, "&cInvalid syntax: 2m3w1d2h (2 months, 3 weeks, 1 day, and 2 hours)");
            return ResultCommand.SUCCESS;
        }
        ChatSpeedModifier modifier = (ChatSpeedModifier)chat.getByClass(ChatSpeedModifier.class);
        if (duration <= 0L) {
            if (modifier == null) {
                Chat.sendMessage(sender, "&cThe chat is not currently slowed down.");
                return ResultCommand.SUCCESS;
            }
            chat.getModifiers().remove(modifier);
            Chat.broadcastMessage("&aChat delay has been removed by " + Profile.getProfiles().get(player.getUniqueId()).getColoredUsername());
        } else {
            if (duration > 60000L) {
                Chat.sendMessage(sender, "&cYou cannot slow the chat for over a minute.");
                return ResultCommand.SUCCESS;
            }
            if (modifier != null) {
                chat.getModifiers().remove(modifier);
            }
            chat.getModifiers().add(new ChatSpeedModifier(duration));
            Chat.broadcastMessage("&cChat has been slowed by " + Profile.getProfiles().get(player.getUniqueId()).getColoredUsername());
        }
        return ResultCommand.SUCCESS;
    }
}
