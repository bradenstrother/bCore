package xyz.baqel.bcore.util;

import org.bukkit.scheduler.BukkitRunnable;
import xyz.baqel.bcore.bCore;

public class TaskUtil {
    public static void runTaskAsync(final Runnable runnable) {
        bCore.getPlugin().getServer().getScheduler().runTaskAsynchronously(bCore.getPlugin(), runnable);
    }

    public static void runTaskLater(final Runnable runnable, final long delay) {
        bCore.getPlugin().getServer().getScheduler().runTaskLater(bCore.getPlugin(), runnable, delay);
    }

    public static void runTaskTimer(final BukkitRunnable runnable, final long delay, final long timer) {
        runnable.runTaskTimer(bCore.getPlugin(), delay, timer);
    }

    public static void runTaskTimer(final Runnable runnable, final long delay, final long timer) {
        bCore.getPlugin().getServer().getScheduler().runTaskTimer(bCore.getPlugin(), runnable, delay, timer);
    }

    public static void runTask(final Runnable runnable) {
        bCore.getPlugin().getServer().getScheduler().runTask(bCore.getPlugin(), runnable);
    }
}
