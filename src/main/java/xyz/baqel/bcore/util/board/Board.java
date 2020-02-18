package xyz.baqel.bcore.util.board;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Board {
    private Scoreboard scoreboard;
    private Objective objective;
    private BoardManager manager;

    Board(final Player player) {
        List<BoardEntry> entries = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        UUID id = player.getUniqueId();
        this.setUp(player);
    }

    public void setUp(final Player player) {
        if (player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
            this.scoreboard = player.getScoreboard();
        } else {
            this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }
        (this.objective = this.scoreboard.registerNewObjective("bCore", "dummy")).setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(this.board.getAdapter().getTitle(player));
        player.setScoreboard(this.scoreboard);
    }


}
