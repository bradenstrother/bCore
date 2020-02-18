package xyz.baqel.bcore.profile.cosmetic.menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.cosmetic.CosmeticColor;
import xyz.baqel.bcore.profile.cosmetic.type.CosmeticEditType;
import xyz.baqel.bcore.util.Chat;
import xyz.baqel.bcore.util.item.ItemMaker;
import xyz.baqel.bcore.util.menu.Button;
import xyz.baqel.bcore.util.menu.Menu;

public class SelectColorMenu extends Menu {
	
    private CosmeticEditType editType;
    
    @Override
    public String getTitle(Player player) {
        if (this.editType == CosmeticEditType.TAG) {
            return Chat.AQUA + Chat.BOLD + "Select a Tag Color";
        }
        return Chat.AQUA + Chat.BOLD + "Select a Color";
    }
    
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (CosmeticColor color : CosmeticColor.values()) {
            buttons.put(buttons.size(), new SelectColorButton(color, this.editType));
        }
        if(this.editType == CosmeticEditType.COLOR) {
        	buttons.put(32 -1, new ResetColorButton());
        }
        return buttons;
    }
    
    public SelectColorMenu(CosmeticEditType editType) { this.editType = editType; }
    
    private class SelectColorButton extends Button {
    	
        private CosmeticColor color;
        
        private CosmeticEditType editType;
        
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemMaker(Material.WOOL).setData(this.color.getVariant()).setTitle(this.color.getDisplay() + this.color.getName()).setLore("", "&bClick here to select &r" + this.color.getDisplay() + this.color.getName() + "&b.").build();
        }
        
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            Profile profile = Profile.getProfiles().get(player.getUniqueId());
            if (this.editType == CosmeticEditType.TAG) {
                profile.setTagColor(this.color);
            } else {
                profile.setColor(this.color);
            }
            player.closeInventory();
            Chat.sendMessage(player, "&bYou set your color to: &r" + this.color.getDisplay() + this.color.getName());
        }
        
        SelectColorButton(CosmeticColor color, CosmeticEditType editType) {
            this.color = color;
            this.editType = editType;
        }
    }
    
    private class ResetColorButton extends Button {
    	
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemMaker(Material.WOOL).setTitle(Chat.AQUA + Chat.BOLD + "Reset Color").setLore("", Chat.DARK_PURPLE + "Click here to reset your color.").build();
        }
        
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        	Profile profile = Profile.getProfiles().get(player.getUniqueId());
            profile.setColor(null);
            player.closeInventory();
            player.sendMessage(Chat.RED + "You reset your color.");
        }
    }
}
