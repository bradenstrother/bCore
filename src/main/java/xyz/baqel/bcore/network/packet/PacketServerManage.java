package xyz.baqel.bcore.network.packet;

import com.google.gson.JsonObject;

import xyz.baqel.bcore.database.redis.gson.JsonChain;
import xyz.baqel.bcore.database.redis.packet.Packet;
import xyz.baqel.bcore.server.state.ServerState;
import xyz.baqel.bcore.server.type.ServerType;

public class PacketServerManage implements Packet {

	private String name;
	private ServerType type;
	private ServerState state;
	private int count;
	private int maxCount;
	private long lastUpdate;
	private boolean whitelisted;
	
	public PacketServerManage() {
		// TODO Auto-generated constructor stub
	}
	
	public PacketServerManage(String name, ServerType type, ServerState state, int count, int maxCount, long lastUpdate, boolean whitelisted) {
		this.name = name;
		this.type = type;
		this.state = state;
		this.count = count;
		this.maxCount = maxCount;
		this.lastUpdate = lastUpdate;
		this.whitelisted = whitelisted;
	}
	
	@Override
	public int id() {
		return 9;
	}

	@Override
	public JsonObject serialize() {
		return new JsonChain()
				.addProperty("name", this.name)
				.addProperty("type", this.type.getName())
				.addProperty("state", this.state.getName())
				.addProperty("count", this.count)
				.addProperty("maxCount", this.maxCount)
				.addProperty("lastUpdate", this.lastUpdate)
				.addProperty("whitelisted", this.whitelisted)
				.get();
	}

	@Override
	public void deserialize(JsonObject object) {
		this.name = object.get("name").getAsString();
		this.type = ServerType.getServerTypeOfDefault(object.get("type").getAsString());
		this.state = ServerState.getServerStateOrDefault(object.get("state").getAsString());
		this.count = object.get("count").getAsInt();
		this.maxCount = object.get("maxCount").getAsInt();
		this.lastUpdate = object.get("lastUpdate").getAsLong();
		this.whitelisted = object.get("whitelisted").getAsBoolean();
	}

	public String getName() {
		return name;
	}
	
	public ServerType getType() {
		return type;
	}
	
	public ServerState getState() {
		return state;
	}
	
	public int getCount() {
		return count;
	}
	
	public int getMaxCount() {
		return maxCount;
	}
	
	public long getLastUpdate() {
		return lastUpdate;
	}
	
	public boolean isWhitelisted() {
		return whitelisted;
	}
}
