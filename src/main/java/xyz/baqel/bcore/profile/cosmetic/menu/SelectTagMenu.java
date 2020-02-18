package xyz.baqel.bcore.profile.cosmetic.menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.cosmetic.CosmeticTag;
import xyz.baqel.bcore.util.Chat;
import xyz.baqel.bcore.util.item.ItemMaker;
import xyz.baqel.bcore.util.menu.Button;
import xyz.baqel.bcore.util.menu.Menu;

public class SelectTagMenu extends Menu {
	
	@Override
	public int getSize() { return 9 * 6; }
	
    @Override
    public String getTitle(final Player player) { return Chat.AQUA + "Select a Tag"; }

    CosmeticTag tag;

    @Override
    public Map<Integer, Button> getButtons(final Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();
        for (final CosmeticTag tag : CosmeticTag.values()) {
            if (player.hasPermission(this.tag.tagPermission())) {
                buttons.put(buttons.size(), new SelectTagButton(tag));
            }
        }
        buttons.put(41 - 1, new ResetTagButton());
        buttons.put(46 - 1, new BackButton());
        return buttons;
    }
    
    private class SelectTagButton extends Button {
    	
        private CosmeticTag tag;

        @Override
        public ItemStack getButtonItem(final Player player) {
            return new ItemMaker(Material.NAME_TAG).setTitle(this.tag.getSelectionDisplay()).setLore("", "&bClick here to select &d" + this.tag.getName() + "&b.").build();
        }
        
        @Override
        public void clicked(final Player player, final int slot, final ClickType clickType, final int hotbarButton) {
            final Profile profile = Profile.getProfiles().get(player.getUniqueId());
            profile.setTag(this.tag);
            player.closeInventory();
            Chat.sendMessage(player, "&aYou set your tag to: &r" + this.tag.getSelectionDisplay());
        }
        
        SelectTagButton(final CosmeticTag tag) { this.tag = tag; }
    }
    
    private class ResetTagButton extends Button {
        @Override
        public ItemStack getButtonItem(final Player player) {
            return new ItemMaker(Material.NETHER_STAR).setTitle(Chat.RED + Chat.BOLD + "Reset Tag").setLore("", Chat.DARK_PURPLE + "Click here to reset your tag.").build();
        }
        
        @Override
        public void clicked(final Player player, final int slot, final ClickType clickType, final int hotbarButton) {
            final Profile nucleusPlayer = Profile.getProfiles().get(player.getUniqueId());
            nucleusPlayer.setTag(null);
            player.closeInventory();
            player.sendMessage(Chat.RED + "You reset your tag.");
        }
    }
    
    private class BackButton extends Button {
    	
    	@Override
    	public ItemStack getButtonItem(Player player) {
    		return new ItemMaker(Material.CARPET).setTitle("&a&lGo Back").setLore(" ", "&bClick here to go back main page.").build();
    	}
    	
    	@Override
    	public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
    		new CosmeticMenu().openMenu(player);
    	}
    }
}
