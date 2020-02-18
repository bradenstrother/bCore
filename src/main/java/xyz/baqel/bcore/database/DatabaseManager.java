package xyz.baqel.bcore.database;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.database.mongo.MongodImpl;
import xyz.baqel.bcore.database.redis.RedisImpl;

@Getter
@NoArgsConstructor
public class DatabaseManager {

	@Getter private bCore plugin;
	@Getter private MongodImpl mongodImpl;
	@Getter private RedisImpl redisImpl;
	
	public DatabaseManager(bCore plugin) { this.plugin = plugin; }
	
	public void init() {
		this.mongodImpl = new MongodImpl(this.plugin);
		this.mongodImpl.init();
		
		this.redisImpl = new RedisImpl(this.plugin);
		this.redisImpl.init();
	}
	
	public void disable() {
		this.mongodImpl.disable();
		this.redisImpl.disable();
	}
	
	public MongodImpl getMongodImpl() { return mongodImpl; }
	
	public RedisImpl getRedisImpl() { return redisImpl; }
}
