package xyz.baqel.bcore.manager.command.general.chat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.moderation.modifier.type.ChatMuteModifier;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.DateUtil;
import xyz.baqel.bcore.util.command.CustomCommand;
import xyz.baqel.bcore.util.command.ResultCommand;

public class MuteChatCommand extends CustomCommand {
    public MuteChatCommand() {
        super("mutechat", "Mute the chat", new String[] {"mc"}, "mutechat <1y1m1w1d1h1m1s>", "bcore.cmd.mutechat");
    }

    @Override
    public ResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        xyz.baqel.bcore.moderation.Chat chat = bCore.getPlugin().getChat();
        Player player = (Player) sender;
        ChatMuteModifier modifier = (ChatMuteModifier)chat.getByClass(ChatMuteModifier.class);
        if (modifier != null) {
            chat.getModifiers().remove(modifier);
            Chat.broadcastMessage("&cChat has been &aunmuted &cby " + Profile.getProfiles().get(player.getUniqueId()).getColoredUsername());
        }
        else if (args.length > 0) {
            long duration;
            try {
                duration = System.currentTimeMillis() - DateUtil.parseDateDiff(args[0], false);
            }
            catch (Exception e) {
                Chat.sendMessage(sender, "&cInvalid syntax: 2m3w1d2h (2 months, 3 weeks, 1 day, and 2 hours)");
                return ResultCommand.SUCCESS;
            }
            if (duration == 0L) {
                Chat.sendMessage(sender, "&cInvalid syntax: 2m3w1d2h (2 months, 3 weeks, 1 day, and 2 hours)");
                return ResultCommand.SUCCESS;
            }
            chat.getModifiers().add(new ChatMuteModifier(duration));
            Chat.broadcastMessage("&cChat has been &4temporarily muted &cby " + Profile.getProfiles().get(player.getUniqueId()).getColoredUsername());
        } else {
            chat.getModifiers().add(new ChatMuteModifier());
            Chat.broadcastMessage("&cChat has been &4muted &cby " + Profile.getProfiles().get(player.getUniqueId()).getColoredUsername());
        }
        return ResultCommand.SUCCESS;
    }
}
