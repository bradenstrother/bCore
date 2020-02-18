package xyz.baqel.bcore.manager.staffmode;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.PlayerUtil;
import xyz.baqel.bcore.util.item.ItemBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StaffModeListeners implements Listener {

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());
        StaffMode staffMode = profile.getStaffMode();
        ItemStack itemStack = event.getPlayer().getItemInHand();
        if (itemStack != null && staffMode != null && event.getRightClicked() instanceof Player && itemStack.getType() == Material.BOOK) {
            Bukkit.dispatchCommand(player, "inspect " + event.getRightClicked().getName());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());
        StaffMode staffMode = profile.getStaffMode();
        if (staffMode != null) {
            staffMode.setVanished(false);
            player.getInventory().setContents(staffMode.getContents());
            player.getInventory().setArmorContents(staffMode.getArmor());
            player.setGameMode(staffMode.getGamemode());
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());
        StaffMode staffMode = profile.getStaffMode();
        ItemStack itemStack = event.getItem();
        if (itemStack != null && staffMode != null && event.getAction().name().contains("RIGHT")) {
            event.setCancelled(true);
            if (itemStack.getType() == Material.RECORD_9) {
                Player toTeleport = null;
                List<Player> players = new ArrayList<>(PlayerUtil.getOnlinePlayers());
                Collections.shuffle(players);
                for (Player online : players) {
                    if (!online.hasPermission("bcore.staff.bypass")) {
                        toTeleport = online;
                        break;
                    }
                }
                if (toTeleport == null) {
                    player.sendMessage(ChatColor.RED + "No player found.");
                    return;
                }
                if (toTeleport == player) {
                    player.sendMessage(ChatColor.RED + "You found yourself!");
                    return;
                }
                player.teleport(toTeleport);
                player.sendMessage(ChatColor.GREEN + "You have teleported to " + ChatColor.LIGHT_PURPLE + toTeleport.getDisplayName() + ChatColor.GREEN + ".");
            } else {
                if (itemStack.getType() == Material.INK_SACK) {
//                    StaffMode profilew22 = Profile.getByUuid(player.getUniqueId()).getStaffMode();
//                    String vanished = profilew22.isVanished() ? (ChatColor.RED + "Visible") : (ChatColor.GREEN + "Vanished");
                    staffMode.setVanished(!staffMode.isVanished());
                    player.setItemInHand(new ItemBuilder(Material.INK_SACK).durability((itemStack.getDurability() == 8) ? 10 : 8).name((itemStack.getDurability() == 8) ? (ChatColor.GREEN + "Vanished") : (ChatColor.GRAY + "Visible")).build());
                    return;
                }
                if (itemStack.getType() == Material.SKULL_ITEM) {
                    List<Player> staff = new ArrayList<>();
                    for (Player online2 : PlayerUtil.getOnlinePlayers()) {
                        if (online2.hasPermission("bcore.staff.bypass")) {
                            staff.add(online2);
                        }
                    }
                    int rows = (int)Math.ceil(staff.size() / 9.0);
                    Inventory inventory = Bukkit.createInventory(null, (rows == 0) ? 9 : (9 * rows), "Online Staff");
                    for (Player member : staff) {
                        inventory.addItem(new ItemBuilder(Material.SKULL_ITEM).durability(3).name("&cStaff &7- &d" + member.getDisplayName()).lore(Arrays.asList("&7&m---------------------------------","&bVanished&7: &d" + (profile.getStaffMode().isVanished() ? "&aTrue" : "&cFalse"), "&eGameMode&7: &d" + (profile.getStaffMode().getGamemode() == GameMode.CREATIVE ? "&aCreative" : "&cSurvival"), "&7&m---------------------------------")).build());
                    }
                    player.openInventory(inventory);
                }
                if (itemStack.getType() == Material.NETHER_STAR) {
                    Bukkit.getServer().dispatchCommand(player,"staffmode");
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (profile.getStaffMode() != null) {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (profile.getStaffMode() != null) {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (profile.getStaffMode() != null) {
            event.setCancelled(true);
        }
    }

//    @EventHandler
//    public void onInventoryClickEvent(InventoryClickEvent event) {
//        Player player = (Player)event.getWhoClicked();
//        Profile profile = Profile.getByUuid(player.getUniqueId());
//        if (profile.getStaffMode() != null) {
//            event.setCancelled(false);
//        }
//    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player)event.getDamager();
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (profile.getStaffMode() != null && profile.getStaffMode().isVanished()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (profile.getStaffMode() != null) {
            event.getDrops().clear();
        }
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (profile.getStaffMode() != null) {
            event.getItemDrop().remove();
            if (player.getItemInHand().getAmount() == 0) {
                player.setItemInHand(event.getItemDrop().getItemStack());
            } else {
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() + 1);
            }
            player.updateInventory();
        }
    }

    @EventHandler
    public void onInventoryInvSeeClickEvent(InventoryClickEvent event) {
        if (event.getInventory().getTitle().contains("Inspection Inventory")) {
            event.setCancelled(true);
        }
    }
}
