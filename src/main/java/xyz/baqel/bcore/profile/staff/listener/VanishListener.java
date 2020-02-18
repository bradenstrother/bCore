package xyz.baqel.bcore.profile.staff.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.BukkitUtil;
import xyz.baqel.bcore.util.chat.Chat;

public class VanishListener implements Listener {
	
    private static void handleFakeChest(Player player, Chest chest, boolean open) {
        Inventory chestInventory = chest.getInventory();
        if (chestInventory instanceof DoubleChestInventory) {
            chest = (Chest) ((DoubleChestInventory) chestInventory).getHolder().getLeftSide();
        }
//        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutBlockAction(new BlockPosition(chest.getX(), chest.getY(), chest.getZ()), Blocks.CHEST, 1, (open ? 1 : 0)));
        player.playSound(chest.getLocation(), open ? Sound.CHEST_OPEN : Sound.CHEST_CLOSE, 1.0f, 1.0f);
    }
    
    private Map<UUID, Location> fakeChestLocationMap;

    public VanishListener() { this.fakeChestLocationMap = new HashMap<>(); }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.updateVanishedState(player, profile.isVanished());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.updateVanishedState(player, profile.isVanished());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity instanceof Player) {
            Player player = event.getPlayer();
            if (!player.isSneaking() && player.hasPermission("bcore.cmd.inspect") && Profile.getProfiles().get(player.getUniqueId()).isVanished()) {
                player.openInventory(((Player) entity).getInventory());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getReason() == EntityTargetEvent.TargetReason.CUSTOM) {
            return;
        }
        Entity target = event.getTarget();
        Entity entity = event.getEntity();
        if ((entity instanceof ExperienceOrb || entity instanceof LivingEntity) && target instanceof Player) {
            Player targetPlayer = (Player) target;
            if (targetPlayer.isOnline()) {
                Profile profile = Profile.getProfiles().get(targetPlayer.getUniqueId());
                if (profile == null || profile.isVanished()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if(player.isOnline()) {
            if (Profile.getProfiles().get(player.getUniqueId()).isVanished() && !player.isSneaking()) {
                e.setCancelled(true);
                Chat.sendMessage(player, "&cYou may want to keep that. Hold sneak to bypass.");
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if(player.isOnline()) {
        if (Profile.getProfiles().get(player.getUniqueId()).isVanished() && !player.isSneaking()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(player.isOnline()) {
            if (Profile.getProfiles().get(player.getUniqueId()).isVanished()) {
                event.setDeathMessage(null);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageEvent event) {
        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause == EntityDamageEvent.DamageCause.VOID || cause == EntityDamageEvent.DamageCause.SUICIDE) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player attacked = (Player) entity;
            if(attacked.isOnline()) {
            	Profile attackedProfile = Profile.getProfiles().get(attacked.getUniqueId());
                Player attacker = BukkitUtil.getFinalAttacker(event, true);
                if (attackedProfile.isVanished()) {
                    if (attacker != null && attacker.isSneaking()) {
                        attacker.sendMessage(ChatColor.RED + "That player is vanished.");
                    }
                    event.setCancelled(true);
                    return;
                }
                if (attacker != null && Profile.getProfiles().get(attacker.getUniqueId()).isVanished() && !attacker.isSneaking()) {
                    attacker.sendMessage(ChatColor.RED + "You cannot attack players whilst vanished, hold sneak to bypass.");
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile.isVanished() && !player.isSneaking()) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot build whilst vanished, hold sneak to bypass.");
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile.isVanished() && !player.isSneaking()) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot build whilst vanished, hold sneak to bypass.");
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile.isVanished() && !player.isSneaking()) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot build whilst vanished, hold sneak to bypass.");
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Profile profile = Profile.getProfiles().get(uuid);
        switch (event.getAction()) {
            case PHYSICAL: {
                if (profile.isVanished()) {
                    event.setCancelled(true);
                    break;
                }
                break;
            }
            case RIGHT_CLICK_BLOCK: {
                org.bukkit.block.Block block = event.getClickedBlock();
                BlockState state = block.getState();
                if (!(state instanceof Chest)) {
                    break;
                }
                if (!profile.isVanished()) {
                    break;
                }
                Chest chest = (Chest) state;
                Location chestLocation = chest.getLocation();
                InventoryType type = chest.getInventory().getType();
                if (type == InventoryType.CHEST && this.fakeChestLocationMap.putIfAbsent(uuid, chestLocation) == null) {
                    ItemStack[] contents = chest.getInventory().getContents();
                    Inventory fakeInventory = Bukkit.createInventory(null, contents.length, "[F] " + type.getDefaultTitle());
                    fakeInventory.setContents(contents);
                    event.setCancelled(true);
                    player.openInventory(fakeInventory);
                    handleFakeChest(player, chest, true);
                    this.fakeChestLocationMap.put(uuid, chestLocation);
                    break;
                }
                break;
            }
		default:
			break;
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Location chestLocation;
        if ((chestLocation = this.fakeChestLocationMap.remove(player.getUniqueId())) != null) {
            BlockState blockState = chestLocation.getBlock().getState();
            if (blockState instanceof Chest) {
                handleFakeChest(player, (Chest) blockState, false);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (humanEntity instanceof Player) {
            Player player = (Player) humanEntity;
            if (this.fakeChestLocationMap.containsKey(player.getUniqueId())) {
                ItemStack stack = event.getCurrentItem();
                if (stack != null && stack.getType() != Material.AIR && !player.hasPermission("bcore.vanish.chestinteract") && !player.isSneaking()) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You cannot interact with fake chest inventories.");
                }
            }
        }
    }
}
