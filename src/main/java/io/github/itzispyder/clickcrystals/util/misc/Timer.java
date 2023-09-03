package io.github.itzispyder.clickcrystals.util.misc;

public class Timer {

    public static final long MILLIS_IN_SECOND = 1000L;
    public static final long MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60L;
    public static final long MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60L;
    public static final long MILLIS_IN_DAY = MILLIS_IN_HOUR * 24L;
    private long start;

    private Timer() {
        this.start = System.currentTimeMillis();
    }

    public static Timer start() {
        return new Timer();
    }

    public static End zero() {
        return new End(0);
    }

    public End end() {
        return new End(start);
    }


    public static class End {

        private final long start;
        private final long end;

        private End(long start) {
            this.end = System.currentTimeMillis();
            this.start = start;
        }

        public long timePassed() {
            return end - start;
        }

        public String getStamp(boolean day, boolean hr, boolean min, boolean sec, boolean ms) {
            long time = timePassed();
            String stamp = "";

            if (day) {
                long l = (long)Math.floor((double)time / (double)MILLIS_IN_DAY);
                time -= l * MILLIS_IN_DAY;
                if (l > 0L) stamp += l + "d";
            }
            if (hr) {
                long l = (long)Math.floor((double)time / (double)MILLIS_IN_HOUR);
                time -= l * MILLIS_IN_HOUR;
                if (l > 0L) stamp += " " + l + "hr";
            }
            if (min) {
                long l = (long)Math.floor((double)time / (double)MILLIS_IN_MINUTE);
                time -= l * MILLIS_IN_MINUTE;
                if (l > 0L) stamp += " " + l + "min";
            }
            if (sec) {
                long l = (long)Math.floor((double)time / (double)MILLIS_IN_SECOND);
                time -= l * MILLIS_IN_SECOND;
                if (l > 0L) stamp += " " + l + "sec";
            }
            if (ms) {
                if (time > 0L) stamp += " " + time + "ms";
            }

            return stamp.trim();
        }

        public String getStampStandard() {
            return getStamp(false, true, true, false, false);
        }

        public String getStampLogger() {
            return getStamp(false, true, true, true, false);
        }

        public String getStampPrecise() {
            return getStamp(false, false, true, true, true);
        }

        public String getStampFull() {
            return getStamp(true, true, true, true, true);
        }
    }
}
