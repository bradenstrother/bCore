package xyz.baqel.bcore.network.packet;

import com.google.gson.JsonObject;

import xyz.baqel.bcore.database.redis.gson.JsonChain;
import xyz.baqel.bcore.database.redis.packet.Packet;

public class PacketRequest implements Packet {

	private String senderName;
	private String message;
	
	public PacketRequest() {
		// TODO Auto-generated constructor stub
	}
	
	public PacketRequest(String senderName, String message) {
		this.senderName = senderName;
		this.message = message;
	}
	
	@Override
	public int id() {
		return 8;
	}

	@Override
	public JsonObject serialize() {
		return new JsonChain()
				.addProperty("senderName", this.senderName)
				.addProperty("message", this.message)
				.get();
	}

	@Override
	public void deserialize(JsonObject object) {
		this.senderName = object.get("senderName").getAsString();
		this.message = object.get("message").getAsString();
	}

	public String getMessage() {
		return message;
	}
	
	public String getSenderName() {
		return senderName;
	}
}
