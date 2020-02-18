package xyz.baqel.bcore.profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.manager.staffmode.StaffMode;
import xyz.baqel.bcore.profile.cosmetic.CosmeticColor;
import xyz.baqel.bcore.profile.cosmetic.CosmeticTag;
import xyz.baqel.bcore.profile.grant.Grant;
import xyz.baqel.bcore.profile.grant.event.GrantAppliedEvent;
import xyz.baqel.bcore.profile.grant.event.GrantExpireEvent;
import xyz.baqel.bcore.profile.punishment.Punishment;
import xyz.baqel.bcore.profile.punishment.PunishmentType;
import xyz.baqel.bcore.profile.rank.Rank;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.event.impl.PlayerVanishEvent;

public class Profile {
	
	/* Cached */
	private static Map<UUID, Profile> profiles;
    
	/* General */
	private String username;
    private UUID uuid;
    
    private String currentAddress;
    
    private String nickName;
    
    /* Player Options */
    private boolean staffChatEnabled;
    private boolean receiveReportEnabled;
    private boolean receiveRequestEnabled;
    private boolean vanished;
    private StaffMode staffMode;

    /* Grants */
    private Grant activeGrant;
    private List<Grant> grants;
    
    /* Punishments*/
    private List<Punishment> punishments;
    
    /* Session */
    private Long firstSeen;
    private Long lastSeen;
    private boolean loaded;
    
    /* Cosmetic */
    private CosmeticColor color;
    private CosmeticTag tag;
    private CosmeticColor tagColor;
    
    static {
        Profile.profiles = new HashMap<>();
    }
    
    public Profile(UUID uuid) {
    	this.username = username;
        this.uuid = uuid;
        
        this.grants = new ArrayList<>();
        this.punishments = new ArrayList<>();
        
        this.load();
    }

    public Player getPlayer() { return Bukkit.getPlayer(this.uuid); }
    
    public String getColoredUsername() {
        return this.activeGrant.getRank().getColor() + (this.nickName == null ? this.username : this.getNickName());
    }
    
    public void refreshDisplayName() {
        Player player = this.getPlayer();
        if (player != null) {
            if (player.hasPermission("bcore.cosmetic.color") && this.color != null) {
                player.setDisplayName(Chat.formatMessages(this.color.getDisplay() + (this.nickName == null ? player.getName() : this.getNickName())));
            }
            else if (this.getActiveRank() == null) {
                player.setDisplayName(Chat.formatMessages((this.nickName == null ? player.getName() : this.getNickName())));
            } else {
                player.setDisplayName(Chat.formatMessages(this.getActiveGrant().getRank().getPrefix() + (this.nickName == null ? player.getName() : this.getNickName())));
            }
        }
    }
    
    public Punishment getActiveMute() {
        for (Punishment punishment : this.punishments) {
            if (punishment.getType() == PunishmentType.MUTE && punishment.isActive()) {
                return punishment;
            }
        }
        return null;
    }
    
    public Punishment getActiveBan() {
        for (Punishment punishment : this.punishments) {
            if (punishment.getType().isBan() && punishment.isActive()) {
                return punishment;
            }
        }
        return null;
    }
    
    public int getPunishmentCountByType(PunishmentType type) {
        int i = 0;
        for (Punishment punishment : this.punishments) {
            if (punishment.getType() == type) {
                ++i;
            }
        }
        return i;
    }
    
    public Rank getActiveRank() { return this.activeGrant.getRank(); }
    
    private void setActiveGrant(Grant grant) {
        this.activeGrant = grant;
        Player player = this.getPlayer();
        if (player != null) {
            player.setDisplayName(grant.getRank().getPrefix() + (this.nickName == null ? player.getName() : this.getNickName()));
        }
    }
    
    public void activateNextGrant() {
        List<Grant> grants = new ArrayList<>(this.grants);
        grants.sort(Comparator.comparingInt(grant -> grant.getRank().getWeight()));
        for (Grant grant : grants) {
            if (!grant.isRemoved() && !grant.hasExpired()) {
                this.setActiveGrant(grant);
            }
        }
    }
    
    void checkGrants() {
        for (Grant grant : this.grants) {
            if (!grant.isRemoved() && grant.hasExpired()) {
                grant.setRemovedAt(System.currentTimeMillis());
                grant.setRemovedReason("Grant expired");
                grant.setRemoved(true);
                if (this.activeGrant != null && this.activeGrant.equals(grant)) {
                    this.activeGrant = null;
                }
                Player player = this.getPlayer();
                if (player == null) {
                    continue;
                }
                new GrantExpireEvent(player, grant).call();
            }
        }
        if (this.activeGrant == null) {
            this.activateNextGrant();
            if (this.activeGrant != null) {
                return;
            }
            Grant grant = new Grant(UUID.randomUUID(), Rank.getDefaultRank(), null, System.currentTimeMillis(), "Default", 2147483647L);
            this.grants.add(grant);
            this.setActiveGrant(grant);
            Player player = this.getPlayer();
            if (player != null) {
                new GrantAppliedEvent(player, grant).call();
            }
        }
    }
    
    public void setupPermissionsAttachment(bCore plugin, Player player) {
    	for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            if (attachmentInfo.getAttachment() == null) {
                continue;
            }
            attachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> attachmentInfo.getAttachment().unsetPermission(permission));
        }
        PermissionAttachment attachment = player.addAttachment(plugin);
        for (String perm : this.activeGrant.getRank().getAllPermissions()) {
            attachment.setPermission(perm, true);
        }
        player.recalculatePermissions();
    }
    
    /**
	 * Load the player's profile.
	 */
    private void load() {
//    	Document document = (Defaultocument)bCore.getPlugin().getDatabaseManager().getMongodImpl().getProfiles().find(eq("uuid", this.uuid.toString())).first();
        Document document = bCore.getPlugin().getDatabaseManager().getMongodImpl().getProfile(this.uuid);
    	if (document != null) {
            if (this.username == null) {
                this.username = document.getString("username");
            }
            this.firstSeen = document.getLong("firstSeen");
            this.lastSeen = document.getLong("lastSeen");
            this.currentAddress = document.getString("currentAddress");
            this.nickName = document.getString("nickName");
            this.receiveRequestEnabled = document.getBoolean("receiveRequest");
            this.receiveReportEnabled = document.getBoolean("receiveReport");
            this.staffChatEnabled = document.getBoolean("staffchat");
            this.vanished = document.getBoolean("vanished");
            if(document.containsKey("cosmetic")) {
            	Document cosmetic = (Document) document.get("cosmetic");
            	if(cosmetic.containsKey("color")) {
            		this.color = CosmeticColor.valueOf(cosmetic.getString("color"));
            	}
            	if(cosmetic.containsKey("tag")) {
            		this.tag = CosmeticTag.valueOf(cosmetic.getString("tag"));
            	}
            	if(cosmetic.containsKey("tag_color")) {
            		this.tagColor = CosmeticColor.valueOf(cosmetic.getString("tag_color"));
            	}
            }
            JsonArray grants = new JsonParser().parse(document.getString("grants")).getAsJsonArray();
            for (JsonElement jsonElement : grants) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Rank rank = Rank.getRankByUuid(UUID.fromString(jsonObject.get("rank").getAsString()));
                if (rank != null) {
                    this.grants.add(Grant.DESERIALIZER.deserialize(jsonObject));
                }
            }
            JsonArray punishments = new JsonParser().parse(document.getString("punishments")).getAsJsonArray();
            for (JsonElement json : punishments) {
                JsonObject object = json.getAsJsonObject();
                this.punishments.add(Punishment.DESERIALIZER.deserialize(object));
            }
        }
        this.checkGrants();
        this.loaded = true;
    }
    
    /**
	 * Saves the player's profile.
	 */
    public void save() {
//    	Document document = new Document();
    	Document document = bCore.getPlugin().getDatabaseManager().getMongodImpl().getProfile(this.uuid);
    	if(document == null) {
    		document = new Document();
    	}
        
        document.put("uuid", this.uuid.toString());
        document.put("username", this.username);
        document.put("firstSeen", this.firstSeen);
        document.put("lastSeen", this.lastSeen);
        document.put("currentAddress", this.currentAddress);
        
        document.put("nickName", this.nickName);
        document.put("receiveRequest", this.receiveRequestEnabled);
        document.put("receiveReport", this.receiveReportEnabled);
        document.put("vanished", this.vanished);
        document.put("staffchat", this.staffChatEnabled);
        JsonArray grants = new JsonArray();
        for (Grant grant : this.grants) {
            grants.add(Grant.SERIALIZER.serialize(grant));
        }
        Document cosmetic = new Document();
        if(this.color != null) {
        	cosmetic.put("color", this.color.name());
        }
        if(this.tag != null) {
        	cosmetic.put("tag", this.tag.name());
        }
        if(this.tagColor != null) {
        	cosmetic.put("tag_color", this.tagColor.name());
        }
        document.put("grants", grants.toString());
        JsonArray punishments = new JsonArray();
        for (Punishment punishment : this.punishments) {
            punishments.add(Punishment.SERIALIZER.serialize(punishment));
        }
        document.put("punishments", punishments.toString());
        document.put("cosmetic", cosmetic);
        
        bCore.getPlugin().getDatabaseManager().getMongodImpl().replacePlayer(this, document);
//        bCore.getPlugin().getDatabaseManager().getMongodImpl().getProfiles().replaceOne(eq("uuid", this.uuid.toString()), document, new UpdateOptions().upsert(true));
    }
    
    /**
	 * Retrieves a cached instance of Profile or creates and returns a new instance.
	 *
	 * @param uuid the player identifier
	 *
	 * @return the player's Profile instance
	 * */
    public static Profile getByUuid(UUID uuid) {
        if (Profile.profiles.containsKey(uuid)) {
            return Profile.profiles.get(uuid);
        }
        return new Profile(uuid);
    }
    
    /**
	 * This method should only be called asynchronously as it could fetch results from Redis.
	 *
	 * @param username the name
	 *
	 * @return A Profile instance if results were fetched
	 * */
    public static Profile getByUsername(String username) {
        Player player = Bukkit.getPlayer(username);
        if (player != null) {
            return Profile.profiles.get(player.getUniqueId());
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
        if (offlinePlayer.hasPlayedBefore()) {
            if (Profile.profiles.containsKey(offlinePlayer.getUniqueId())) {
                return Profile.profiles.get(offlinePlayer.getUniqueId());
            }
            return new Profile(offlinePlayer.getUniqueId());
        } else {
            UUID uuid = bCore.getPlugin().getProfileManager().getUuidCache().getUuid(username);
            if (uuid == null) {
                return null;
            }
            if (Profile.profiles.containsKey(uuid)) {
                return Profile.profiles.get(uuid);
            }
            return new Profile(uuid);
        }
    }
    
    public static Profile getByPlayer(Player player) {
        Profile profile = profiles.get(player.getUniqueId());

        if (profile == null) {
            profile = new Profile(player.getUniqueId());
        }
        return profile;
    }
    
    public static Map<UUID, Profile> getProfiles() { return Profile.profiles; }
    
    public String getUsername() { return this.username; }
    
    public void setUsername(String username) { this.username = username; }
    
    public UUID getUuid() { return this.uuid; }
    
    public Long getFirstSeen() { return this.firstSeen; }
    
    public void setFirstSeen(Long firstSeen) { this.firstSeen = firstSeen; }
    
    public Long getLastSeen() { return this.lastSeen; }
    
    public void setLastSeen(Long lastSeen) { this.lastSeen = lastSeen; }
    
    public String getCurrentAddress() { return this.currentAddress; }
    
    public void setCurrentAddress(String currentAddress) { this.currentAddress = currentAddress; }
    
    public Grant getActiveGrant() { return this.activeGrant; }
    
    public List<Grant> getGrants() { return this.grants; }
    
    public List<Punishment> getPunishments() { return this.punishments; }
    
    public boolean isLoaded() { return this.loaded; }
    
    public void setLoaded(boolean loaded) { this.loaded = loaded; }
    
    public CosmeticColor getColor() { return color; }
    
    public void setColor(CosmeticColor color) {
        this.color = color;
        if (this.color != null) {
            this.refreshDisplayName();
        }
    }
    
    public CosmeticTag getTag() { return tag; }
    
    public void setTag(CosmeticTag tag) { this.tag = tag; }
    
    public CosmeticColor getTagColor() { return tagColor; }
    
    public void setTagColor(CosmeticColor tagColor) { this.tagColor = tagColor; }
    
    public String getNickName() { return nickName; }
    
    public void setNickName(String nickName) { this.nickName = nickName; }
    
    public boolean isReceiveReportEnabled() { return receiveReportEnabled; }
    
    public void setReceiveReportEnabled(boolean receiveReportEnabled) {
		this.receiveReportEnabled = receiveReportEnabled;
	}
    
    public boolean isReceiveRequestEnabled() { return receiveRequestEnabled; }
    
    public void setReceiveRequestEnabled(boolean receiveRequestEnabled) {
		this.receiveRequestEnabled = receiveRequestEnabled;
	}
    
    public boolean isStaffChatEnabled() { return staffChatEnabled; }
    
    public void setStaffChatEnabled(boolean staffChatEnabled) { this.staffChatEnabled = staffChatEnabled; }

    public StaffMode getStaffMode() { return this.staffMode; }

    public StaffMode setStaffMode(StaffMode staffMode) { return this.staffMode = staffMode; }
    
    public boolean isVanished() { return this.vanished; }

    public void setVanished(boolean vanished) { this.setVanished(vanished, true); }

    public void setVanished() { this.setVanished(!this.isVanished(), true); }

    private void setVanished(boolean vanished, boolean update) { this.setVanished(this.getPlayer(), vanished, update); }

    private void setVanished(Player player, boolean vanished, boolean notifyPlayerList) {
        if (this.vanished != vanished) {
            if (player != null) {
                PlayerVanishEvent event = new PlayerVanishEvent(player, notifyPlayerList ? new HashSet<>(Bukkit.getOnlinePlayers()) : Collections.emptySet(), vanished);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return;
                }
                if (notifyPlayerList) this.updateVanishedState(player, event.getViewers(), vanished);
            }
            this.vanished = vanished;
        }
    }

    public void updateVanishedState(Player player, boolean vanished) {
        this.updateVanishedState(player, new ArrayList<>(Bukkit.getOnlinePlayers()), vanished);
    }

    private void updateVanishedState(Player player, Collection<Player> viewers, boolean vanished) {
        player.spigot().setCollidesWithEntities(!vanished);
        for (Player target : viewers) {
            if (player.equals(target)) {
                continue;
            }
            Profile profile = Profile.getProfiles().get(target.getUniqueId());
            if (profile.isVanished()) {
                if (!vanished) {
                    player.hidePlayer(target);
                } else {
                    player.showPlayer(target);
                }
            } else if (vanished) {
                target.hidePlayer(player);
            } else{
                target.showPlayer(player);
            }
        }
    }
}
