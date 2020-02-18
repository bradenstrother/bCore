package xyz.baqel.bcore.util.event;

import org.bukkit.event.Cancellable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancellableEvent extends BaseEvent implements Cancellable {

	private boolean cancelled;

	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
