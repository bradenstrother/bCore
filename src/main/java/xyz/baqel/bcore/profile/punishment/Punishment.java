package xyz.baqel.bcore.profile.punishment;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.TimeUtil;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class Punishment {
	
    public static PunishmentJsonSerializer SERIALIZER;
    public static PunishmentJsonDeserializer DESERIALIZER;
    private UUID uuid;
    private PunishmentType type;
    private UUID addedBy;
    private long addedAt;
    private String addedReason;
    private long duration;
    private UUID pardonedBy;
    private long pardonedAt;
    private String pardonedReason;
    private boolean pardoned;
    
    static {
        Punishment.SERIALIZER = new PunishmentJsonSerializer();
        Punishment.DESERIALIZER = new PunishmentJsonDeserializer();
    }
    
    public Punishment(UUID uuid, PunishmentType type, long addedAt, String addedReason, long duration) {
        this.uuid = uuid;
        this.type = type;
        this.addedAt = addedAt;
        this.addedReason = addedReason;
        this.duration = duration;
    }
    
    public boolean isPermanent() { return this.type == PunishmentType.BLACKLIST || this.duration == 2147483647L; }
    
    public boolean isActive() { return !this.pardoned && (this.isPermanent() || this.getMillisRemaining() < 0L); }
    
    private long getMillisRemaining() { return System.currentTimeMillis() - (this.addedAt + this.duration); }
    
    public String getTimeRemaining() {
        if (this.pardoned) {
            return "Pardoned";
        }
        if (this.isPermanent()) {
            return "Permanent";
        }
        if (!this.isActive()) {
            return "Expired";
        }
        return TimeUtil.millisToRoundedTime(this.addedAt + this.duration - System.currentTimeMillis());
    }
    
    public String getAddedAtFormatted() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(this.addedAt));

		return calendar.getTime().toString();
	}
    
    private String getContext() {
        if (this.type != PunishmentType.BAN && this.type != PunishmentType.MUTE) {
            return this.pardoned ? this.type.getUndoContext() : this.type.getContext();
        }
        if (this.isPermanent()) {
            return this.pardoned ? this.type.getUndoContext() : ("permanently " + this.type.getContext());
        }
        return this.pardoned ? this.type.getUndoContext() : ("temporarily " + this.type.getContext());
    }
    
    public void broadcast(String sender, String target, boolean silent) {
    	for(Player players : Bukkit.getOnlinePlayers()) {
    		if(players.hasPermission("bcore.staff.bypass")) {
    			ComponentBuilder builder = new ComponentBuilder(Chat.formatMessages((silent ? "&7[Silent] " : "") + "&r" + target + " &ahas been " + this.getContext() + " by &r" + sender));
    			builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Chat.formatMessages("&cReason&7: &f" + (this.pardoned ? this.getPardonedReason() : this.getAddedReason()) + "\n" + "&cDuration&7: &f" + this.getTimeRemaining())).create()));
    			players.spigot().sendMessage(builder.create());
    		} else {
    			if(!silent) {
    				Chat.sendMessage(players, "&r" + target + " &ahas been " + this.getContext() + " by &r" + sender);
    			}
    		}
    	}
    }
    
    public String getKickMessage() {
        String kickMessage = "&cYour account has been suspended from ClockHQ\n\n" + "&cAppeal on Discord @ https://discord.gg/6na9rpm";
        return Chat.formatMessages(kickMessage);
    }
    
    public UUID getUuid() { return this.uuid; }
    
    public PunishmentType getType() { return this.type; }
    
    public UUID getAddedBy() { return this.addedBy; }
    
    public void setAddedBy(UUID addedBy) { this.addedBy = addedBy; }
    
    public long getAddedAt() { return this.addedAt; }
    
    public String getAddedReason() { return this.addedReason; }
    
    public long getDuration() { return this.duration; }
    
    public UUID getPardonedBy() { return this.pardonedBy; }
    
    public void setPardonedBy(UUID pardonedBy) { this.pardonedBy = pardonedBy; }
    
    public long getPardonedAt() { return this.pardonedAt; }
    
    public void setPardonedAt(long pardonedAt) { this.pardonedAt = pardonedAt; }
    
    public String getPardonedReason() { return this.pardonedReason; }
    
    public void setPardonedReason(String pardonedReason) { this.pardonedReason = pardonedReason; }
    
    public boolean isPardoned() { return this.pardoned; }
    
    public void setPardoned(boolean pardoned) { this.pardoned = pardoned; }
}
