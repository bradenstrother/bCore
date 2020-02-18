package xyz.baqel.bcore.util.chat;

import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatUtil {
    private List<TextComponent> components;

    public ChatUtil(final String message) {
        this.components = new ArrayList<>();
        final TextComponent component = new TextComponent(ColorText.translate(message));
        this.components.add(component);
    }

    public ChatUtil(final String message, final String hover, final String click) {
        this.components = new ArrayList<>();
        this.add(message, hover, click);
    }

    private void add(final String message, final String hover, final String click) {
        final TextComponent component = new TextComponent(message);
        if (hover != null) {
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorText.translate(hover)).create()));
        }
        if (click != null) {
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ColorText.translate(click)));
        }
        this.components.add(component);
    }

    public void add(final String message) {
        this.components.add(new TextComponent(message));
    }

    public void send(final Player player) {
        player.sendMessage(Arrays.toString(this.components.toArray((BaseComponent[]) new TextComponent[0])));
    }
}
