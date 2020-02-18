package xyz.baqel.bcore.profile.grant.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.profile.grant.Grant;
import xyz.baqel.bcore.profile.grant.procedure.GrantProcedure;
import xyz.baqel.bcore.profile.grant.procedure.GrantProcedureStage;
import xyz.baqel.bcore.profile.grant.procedure.GrantProcedureType;
import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.item.ItemMaker;
import xyz.baqel.bcore.util.menu.Button;
import xyz.baqel.bcore.util.menu.pagination.PaginatedMenu;

public class GrantsMenu extends PaginatedMenu {
	
    private Profile profile;
    
    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&aGrants of " + this.profile.getColoredUsername();
    }
    
    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (Grant grant : this.profile.getGrants()) {
            buttons.put(buttons.size(), new GrantInfoButton(this.profile, grant));
        }
        return buttons;
    }
    
    public GrantsMenu(Profile profile) {
        this.profile = profile;
    }
    
    private class GrantInfoButton extends Button {
    	
        private Profile profile;
        private Grant grant;
        
        @Override
        public ItemStack getButtonItem(Player player) {
            String addedBy = "Console";
            
            if (this.grant.getAddedBy() != null) {
                addedBy = "Could not fetch...";
                
                Profile addedByProfile = Profile.getByUuid(this.grant.getAddedBy());
                
                if (addedByProfile != null && addedByProfile.isLoaded()) {
                    addedBy = addedByProfile.getUsername();
                }
            }
            List<String> lore = new ArrayList<>();
            lore.add(Chat.MENU_BAR);
            
            lore.add("&bIssuer: &f" + addedBy);
            lore.add("&bReason: &f" + this.grant.getAddedReason());
            lore.add("&bDate added: &f" + this.grant.getAddedAtDate());
            
            if (!this.grant.isRemoved()) {
                if (!this.grant.hasExpired()) {
                    lore.add("&bExpires at: &f" + this.grant.getExpiresAtDate());
                }
            } else {
                String removedBy = "Console";
                
                if (this.grant.getRemovedBy() != null) {
                    removedBy = "Could not fetch...";
                    
                    Profile removedByProfile = Profile.getByUuid(this.grant.getRemovedBy());
                    
                    if (removedByProfile != null && removedByProfile.isLoaded()) {
                        removedBy = removedByProfile.getUsername();
                    }
                }
                lore.add(Chat.MENU_BAR);
                
                lore.add("&7[Removed] &bIssuer: &f" + removedBy);
                lore.add("&7[Removed] &bReason: &f" + this.grant.getRemovedReason());
                lore.add("&7[Removed] &bDate removed: &f" + this.grant.getRemovedAtDate());
            }
            
            lore.add(Chat.MENU_BAR);
            
            if (!this.grant.isRemoved()) {
                if(!this.grant.getRank().isDefaultRank()) {
                	lore.add("&cRight click to remove this grant");
                	lore.add(Chat.MENU_BAR);
                }
            }
            return new ItemMaker(Material.PAPER).setTitle(this.grant.getRank().getColor() + this.grant.getRank().getDisplayName()).setLore(lore).build();
        }
        
        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType == ClickType.RIGHT && !this.grant.isRemoved() && !this.grant.getRank().isDefaultRank()) {
                GrantProcedure procedure = new GrantProcedure(player, this.profile, GrantProcedureType.REMOVE, GrantProcedureStage.REQUIRE_TEXT);
                procedure.setGrant(this.grant);
                Chat.sendMessage(player, "&c&oType a reason for removing this grant in chat...");
                player.closeInventory();
            }
        }
        
        GrantInfoButton(Profile profile, Grant grant) {
            this.profile = profile;
            this.grant = grant;
        }
    }
}
