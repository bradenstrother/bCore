package xyz.baqel.bcore.database.redis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.baqel.bcore.bCore;
import xyz.baqel.bcore.util.config.ConfigCursor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
@NoArgsConstructor
public class RedisImpl {

	@Getter private bCore plugin;
	@Getter private JedisPool jedisPool;
	
	@Getter private Redis redis;
	
	public RedisImpl(bCore plugin) {
		this.plugin = plugin;
	}
	
	public void init() {
		ConfigCursor cursorRedis = new ConfigCursor(this.plugin.getRootConfig(), "database.redis");
    	this.jedisPool = new JedisPool(cursorRedis.getString("host"), cursorRedis.getInt("port"));
    	
    	if(cursorRedis.getBoolean("authentication.enabled")) {
    		try (Jedis jedis = this.jedisPool.getResource()) {
    			jedis.auth(cursorRedis.getString("authentication.password"));
    		}
    	}
    	
    	this.redis = new Redis(
    			//database
    			"bcore",
    			//host
    			cursorRedis.getString("host"),
    			//port
    			cursorRedis.getInt("port"),
    			//authentication
    			cursorRedis.getBoolean("authentication.enabled") ? cursorRedis.getString("authentication.password") : null);
	}
	
	public void disable() {
		try {
			this.jedisPool.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JedisPool getJedisPool() {
		return jedisPool;
	}
	
	public Redis getRedis() {
		return redis;
	}
}
