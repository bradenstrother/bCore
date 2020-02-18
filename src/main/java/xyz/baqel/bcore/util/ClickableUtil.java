package xyz.baqel.bcore.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.NoArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.baqel.bcore.util.chat.Chat;

@NoArgsConstructor
public class ClickableUtil {

	private List<TextComponent> components = new ArrayList<>();

	public ClickableUtil(String msg) {
		TextComponent message = new TextComponent(Chat.formatMessages(msg));

		this.components.add(message);
	}

	public ClickableUtil(String msg, String hoverMsg, String clickString) {
		this.add(Chat.formatMessages(msg), hoverMsg, clickString);
	}

	public TextComponent add(String msg, String hoverMsg, String clickString) {
		TextComponent message = new TextComponent(Chat.formatMessages(msg));

		if (hoverMsg != null) {
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Chat.formatMessages(hoverMsg)).create()));
		}

		if (clickString != null) {
			message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, clickString));
		}

		this.components.add(message);

		return message;
	}

	public void add(String message) {
		this.components.add(new TextComponent(Chat.formatMessages(message)));
	}

	public void sendToPlayer(CommandSender sender) {
		if(sender instanceof Player) {
			((Player) sender).spigot().sendMessage(this.asComponents());
		} else {
			sender.sendMessage(TextComponent.toPlainText(this.asComponents()));
		}
	}

	public TextComponent[] asComponents() {
		return this.components.toArray(new TextComponent[0]);
	}
}
