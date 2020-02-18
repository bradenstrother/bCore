package xyz.baqel.bcore.util.board;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BoardListener implements Listener {
    private BoardManager board;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.board.getBoardMap().put(event.getPlayer().getUniqueId(), new Board(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.board.getBoardMap().remove(event.getPlayer().getUniqueId());
    }

    public BoardListener(BoardManager board) {
        this.board = board;
    }
}
