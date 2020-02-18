package xyz.baqel.bcore.util.board;

import org.bukkit.entity.Player;

import java.util.List;

public interface BoardAdapter {
    String getTitle(final Player p0);

    List<String> getLines(final Player p0);

    BoardStyle getBoardStyle(final Player p0);
}
