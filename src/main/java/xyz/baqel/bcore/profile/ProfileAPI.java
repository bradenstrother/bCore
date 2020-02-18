package xyz.baqel.bcore.profile;

import org.bukkit.entity.Player;

import xyz.baqel.bcore.util.chat.Chat;

public class ProfileAPI {
	
    public static String getPlayerRank(Player player, boolean colored) {
    	Profile profile = Profile.getProfiles().get(player.getUniqueId());
    	return Chat.formatMessages((colored ? profile.getActiveGrant().getRank().getColoredName() : profile.getActiveGrant().getRank().getDisplayName()));
    }
    
    public static String getPlayerAndPrefix(Player player) {
    	Profile profile = Profile.getProfiles().get(player.getUniqueId());
    	return Chat.formatMessages(profile.getActiveGrant().getRank().getPrefix() + (profile.getNickName() == null ? player.getName() : profile.getNickName()));
    }
    
    public static String getPlayerColoredName(Player player) {
    	Profile profile = Profile.getProfiles().get(player.getUniqueId());
    	return Chat.formatMessages(profile.getColoredUsername());
    }
    
    public static String getPrefixedPlayer(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        String prefix = profile.getActiveGrant().getRank().getPrefix();
        String color = null;
        String tag = null;
        if (player.hasPermission("bcore.cosmetic.color") && profile.getColor() != null) {
            color = profile.getColor().getDisplay();
        }
        if (player.hasPermission("bcore.cosmetic.tag") && profile.getTag() != null) {
            if (profile.getTagColor() != null) {
                tag = Chat.formatMessages(profile.getTagColor().getDisplay() + profile.getTag().getDisplay());
            } else {
                tag = Chat.formatMessages(profile.getTag().getDisplay());
            }
        }
        return Chat.formatMessages(((tag == null) ? "" : (tag + " ")) + prefix + ((color == null) ? "" : (color)) + (profile.getNickName() == null ? player.getName() : profile.getNickName()));
    }
    
    public static String getColoredName(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (player.hasPermission("bcore.cosmetic.color") && profile.getColor() != null) {
            return Chat.formatMessages(profile.getColor().getDisplay() + (profile.getNickName() == null ? player.getName() : profile.getNickName()));
        }
        return Chat.formatMessages(profile.getActiveGrant().getRank().getColoredName() + (profile.getNickName() == null ? player.getName() : profile.getNickName()));
    }
    
    public static String getColoredTag(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (player.hasPermission("bcore.cosmetic.color") && profile.getColor() != null) {
            return Chat.formatMessages(profile.getColor().getDisplay());
        }
        return Chat.formatMessages(profile.getActiveGrant().getRank().getColor().toString());
    }
}
