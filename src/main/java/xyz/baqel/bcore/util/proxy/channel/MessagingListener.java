package xyz.baqel.bcore.util.proxy.channel;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessagingListener implements PluginMessageListener {

	@Getter private Plugin plugin;
	public Map<String, Integer> playerCount;
	
	public MessagingListener(Plugin plugin) {
		this.plugin = plugin;
		this.playerCount = new LinkedHashMap<String, Integer>();
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player target, byte[] message) {
		if(!channel.equals("BungeeCord")) return;
		
		 try {
			if(message.length == 0) return;
			
			ByteArrayDataInput inPut = ByteStreams.newDataInput(message);
			String subChannel = inPut.readUTF();
			if(subChannel.equals("PlayerCount")) {
				String server = inPut.readUTF();
				Integer playerCount = inPut.readInt();
				
				this.playerCount.put(server, playerCount);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
