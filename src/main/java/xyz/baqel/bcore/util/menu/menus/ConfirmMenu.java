package xyz.baqel.bcore.util.menu.menus;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import xyz.baqel.bcore.util.menu.Button;
import xyz.baqel.bcore.util.menu.Menu;
import xyz.baqel.bcore.util.menu.button.ConfirmationButton;
import xyz.baqel.bcore.util.menu.callback.TypeCallback;

public class ConfirmMenu extends Menu {
	
    private String title;
    private TypeCallback<Boolean> response;
    private boolean closeAfterResponse;
    private Button[] centerButtons;
    
    public ConfirmMenu(String title, TypeCallback<Boolean> response, boolean closeAfter, Button... centerButtons) {
        this.title = title;
        this.response = response;
        this.closeAfterResponse = closeAfter;
        this.centerButtons = centerButtons;
    }
    
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                buttons.put(this.getSlot(x, y), new ConfirmationButton(true, this.response, this.closeAfterResponse));
                buttons.put(this.getSlot(8 - x, y), new ConfirmationButton(false, this.response, this.closeAfterResponse));
            }
        }
        if (this.centerButtons != null) {
            for (int i = 0; i < this.centerButtons.length; ++i) {
                if (this.centerButtons[i] != null) {
                    buttons.put(this.getSlot(4, i), this.centerButtons[i]);
                }
            }
        }
        return buttons;
    }
    
    @Override
    public String getTitle(Player player) {
        return this.title;
    }
}
