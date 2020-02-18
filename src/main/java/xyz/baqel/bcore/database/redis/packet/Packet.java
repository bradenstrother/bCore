package xyz.baqel.bcore.database.redis.packet;

import com.google.gson.JsonObject;

public interface Packet {
	
    int id();  
    JsonObject serialize();
    void deserialize(JsonObject object);
}
