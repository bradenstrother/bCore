package xyz.baqel.bcore.network.packet;

import com.google.gson.JsonObject;
import xyz.baqel.bcore.database.redis.gson.JsonChain;
import xyz.baqel.bcore.database.redis.packet.Packet;

public class PacketReport implements Packet {

	private String senderName;
	private String targetName;
	private String message;
	
	public PacketReport() {
		// TODO Auto-generated constructor stub
	}
	
	public PacketReport(String senderName, String targetName, String message) {
		this.senderName = senderName;
		this.targetName = targetName;
		this.message = message;
	}
	
	@Override
	public int id() {
		return 7;
	}

	@Override
	public JsonObject serialize() {
		return new JsonChain()
				.addProperty("senderName", this.senderName)
				.addProperty("targetName", this.targetName)
				.addProperty("message", this.message)
				.get();
	}

	@Override
	public void deserialize(JsonObject object) {
		this.senderName = object.get("senderName").getAsString();
		this.targetName = object.get("targetName").getAsString();
		this.message = object.get("message").getAsString();
	}

	public String getMessage() {
		return message;
	}
	
	public String getSenderName() {
		return senderName;
	}
	
	public String getTargetName() {
		return targetName;
	}
}
