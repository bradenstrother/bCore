package xyz.baqel.bcore.manager.staffmode;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.item.ItemBuilder;

public class StaffMode {
    private Player player;

    private boolean vanished;

    private ItemStack[] contents;

    private ItemStack[] armor;

    private GameMode gamemode;

    public StaffMode(Player player) {
        this.player = player;
        this.contents = player.getInventory().getContents();
        this.armor = player.getInventory().getArmorContents();
        this.gamemode = player.getGameMode();
        this.setVanished(true);
        this.setup();
    }

    private void setup() {
        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);
        this.player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS).name(Chat.RED + Chat.BOLD + "Teleport Tool").lore("Click to teleport through blocks").build());
        this.player.getInventory().setItem(1, new ItemBuilder(Material.BOOK).name(Chat.DARK_PURPLE + Chat.BOLD + "Inspect").lore("Click a player to inspect them").build());
        this.player.getInventory().setItem(4, new ItemBuilder(Material.SKULL).name(Chat.RED + Chat.BOLD + "Online Staff").lore("Click to view online staff members").build());
        this.player.getInventory().setItem(7, new ItemBuilder(Material.INK_SACK).durability(8).name(Chat.GREEN + Chat.BOLD + "Become Visible").lore("Click To Toggle Vanish").build());
        this.player.getInventory().setItem(8, new ItemBuilder(Material.NETHER_STAR).name(Chat.RED + Chat.BOLD + "Exit").lore("Click to exit staff mode").build());
        this.player.setGameMode(GameMode.CREATIVE);
        this.player.updateInventory();
    }

    public void setVanished(boolean vanished) {
        this.vanished = vanished;
        if (vanished) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("bcore.staff.bypass") || p.hasPermission("*") || p.hasPermission("bcore.*")) {
                    p.showPlayer(this.player);
                    Chat.broadcastMessage("&8&oShowing " + this.player.getPlayer().getName() + "&8&o to " + p.getPlayerListName());
                    continue;
                }
                p.hidePlayer(this.player);
                Chat.broadcastMessage("&8&oHiding " + this.player.getPlayer().getName() + "&8&o to " + p.getPlayerListName());
            }
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(this.player);
            }
        }
    }

    public Player getPlayer() { return this.player; }

    boolean isVanished() { return this.vanished; }

    public ItemStack[] getContents() { return this.contents; }

    public ItemStack[] getArmor() { return this.armor; }

    public void setContents(ItemStack[] contents) { this.contents = contents; }

    public void setArmor(ItemStack[] armor) { this.armor = armor; }

    public GameMode getGamemode() { return this.gamemode; }

    public void setGamemode(GameMode gamemode) { this.gamemode = gamemode; }
}
