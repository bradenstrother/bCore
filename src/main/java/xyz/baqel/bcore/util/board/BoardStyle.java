package xyz.baqel.bcore.util.board;

public enum BoardStyle {
    MODERN(false, 1),
    KOHI(true, 15),
    VIPER(true, -1),
    TEAMSHQ(true, 0);

    private boolean descending;
    private int start;

    public boolean isDescending() {
        return this.descending;
    }

    public int getStart() {
        return this.start;
    }

    private BoardStyle(final boolean descending, final int start) {
        this.descending = descending;
        this.start = start;
    }
}
