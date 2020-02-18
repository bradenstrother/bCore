package xyz.baqel.bcore.network.packet;

import java.util.UUID;

import com.google.gson.JsonObject;

import xyz.baqel.bcore.database.redis.gson.JsonChain;
import xyz.baqel.bcore.database.redis.packet.Packet;

public class PacketDeleteRank implements Packet {
	
    private UUID uuid;
    
    public PacketDeleteRank() {
		// TODO Auto-generated constructor stub
	}
    
    @Override
    public int id() {
        return 5;
    }
    
    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("uuid", this.uuid.toString()).get();
    }
    
    @Override
    public void deserialize(JsonObject jsonObject) {
        this.uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
    }
    
    public PacketDeleteRank(UUID uuid) {
        this.uuid = uuid;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
}
