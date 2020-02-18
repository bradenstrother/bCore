package xyz.baqel.bcore.moderation;

import lombok.Getter;
import org.bukkit.Bukkit;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.moderation.modifier.ChatModifier;
import xyz.baqel.bcore.moderation.spam.ChatSpamFilter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Chat {

    private List<ChatModifier> modifiers;
    private ChatSpamFilter filter;

    public Chat(bCore plugin) {
        this.modifiers = new ArrayList<>();

        this.filter = new ChatSpamFilter();

        Bukkit.getPluginManager().registerEvents(new ChatListener(), plugin);
    }

    public ChatModifier getByClass(Class<? extends ChatModifier> chatmodifier) {
        for (ChatModifier modifier : this.modifiers) {
            if (modifier.getClass().equals(chatmodifier)) {
                return modifier;
            }
        }
        return null;
    }

    public List<ChatModifier> getModifiers() { return this.modifiers; }

    ChatSpamFilter getFilter() { return this.filter; }
}
