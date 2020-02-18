package xyz.baqel.bcore.profile.staff.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.baqel.bcore.profile.Profile;
import xyz.baqel.bcore.util.menu.Button;
import xyz.baqel.bcore.util.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;

public class StaffMenu extends PaginatedMenu {

    private Profile profile;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&aOptions for " + this.profile.getColoredUsername();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(9, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return null;
            }
        });
        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        return null;
    }
}
