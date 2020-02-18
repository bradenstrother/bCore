package xyz.baqel.bcore.util.board;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class BoardManager {
    private BoardAdapter adapter;
    private Map<UUID, Board> boardMap;

    public BoardManager(final BoardAdapter adapter) {
        this.adapter = adapter;
        this.boardMap = new HashMap<>();
    }

    public void sendScoreboard() {
        for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
            final Board board = this.boardMap.get(player.getUniqueId());
            if (board != null) {
                final Scoreboard scoreboard = board.getScoreboard();
                final Objective objective = board.getObjective();
                final String title = ColorText.translate(this.adapter.getTitle(player));
                if (!objective.getDisplayName().equals(title)) {
                    objective.setDisplayName(title);
                }
                final List<String> lines = this.adapter.getLines(player);
                if (lines == null || lines.isEmpty()) {
                    board.getEntries().forEach(BoardEntry::quit);
                    board.getEntries().clear();
                }
                else {
                    if (!this.adapter.getBoardStyle(player).isDescending()) {
                        Collections.reverse(lines);
                    }
                    if (board.getEntries().size() > lines.size()) {
                        for (int i = lines.size(); i < board.getEntries().size(); ++i) {
                            final BoardEntry entry = board.getEntryAtPosition(i);
                            if (entry != null) {
                                entry.quit();
                            }
                        }
                    }
                    int cache = this.adapter.getBoardStyle(player).getStart();
                    for (int j = 0; j < lines.size(); ++j) {
                        BoardEntry entry2 = board.getEntryAtPosition(j);
                        final String line = ColorText.translate(lines.get(j));
                        if (entry2 == null) {
                            entry2 = new BoardEntry(board, line);
                        }
                        entry2.setText(line);
                        entry2.setUp();
                        entry2.send(this.adapter.getBoardStyle(player).isDescending() ? cache-- : cache++);
                    }
                }
                if (player.getScoreboard() == scoreboard) {
                    continue;
                }
                player.setScoreboard(scoreboard);
            }
        }
    }

    public BoardAdapter getAdapter() {
        return this.adapter;
    }

    Map<UUID, Board> getBoardMap() {
        return this.boardMap;
    }
}
