package emse;

class TimeTracker {

    private long lastStart;

    public void reset() {
        lastStart = System.currentTimeMillis();
    }

    public long getTimePassed() {
        return System.currentTimeMillis() - lastStart;
    }
}
