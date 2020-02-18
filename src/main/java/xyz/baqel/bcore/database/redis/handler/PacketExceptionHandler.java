package xyz.baqel.bcore.database.redis.handler;

import xyz.baqel.bcore.util.chat.Chat;
import xyz.baqel.bcore.util.chat.Chat.Level;

public class PacketExceptionHandler {
	
    public void onException(Exception e) {
        Chat.log(Level.EXCEPTION, "&cFailed to send packet.");
        e.printStackTrace();
    }
}
