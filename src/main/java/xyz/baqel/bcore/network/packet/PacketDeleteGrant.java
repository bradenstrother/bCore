package xyz.baqel.bcore.network.packet;

import java.util.UUID;

import com.google.gson.JsonObject;

import xyz.baqel.bcore.database.redis.gson.JsonChain;
import xyz.baqel.bcore.database.redis.packet.Packet;
import xyz.baqel.bcore.profile.grant.Grant;

public class PacketDeleteGrant implements Packet {
	
    private UUID playerUuid;
    
    private Grant grant;
    
    public PacketDeleteGrant() {
		// TODO Auto-generated constructor stub
	}
    
    public PacketDeleteGrant(UUID playerUuid, Grant grant) {
        this.playerUuid = playerUuid;
        this.grant = grant;
    }
    
    @Override
    public int id() {
        return 4;
    }
    
    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("playerUuid", this.playerUuid.toString()).add("grant", Grant.SERIALIZER.serialize(this.grant)).get();
    }
    
    @Override
    public void deserialize(JsonObject jsonObject) {
        this.playerUuid = UUID.fromString(jsonObject.get("playerUuid").getAsString());
        this.grant = Grant.DESERIALIZER.deserialize(jsonObject.get("grant").getAsJsonObject());
    }
    
    public UUID getPlayerUuid() {
        return this.playerUuid;
    }
    
    public Grant getGrant() {
        return this.grant;
    }
}
