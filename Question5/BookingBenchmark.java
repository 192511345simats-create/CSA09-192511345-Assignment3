import java.util.concurrent.CountDownLatch;

public class BookingBenchmark {
    static class Booking {
        final boolean[] seats;
        int sold;
        Booking(int n) { seats = new boolean[n]; }
        boolean bookUnsafe(int seat, CountDownLatch ready, CountDownLatch go) {
            try { ready.countDown(); go.await(); } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); return false;
            }
            if (!seats[seat]) { seats[seat] = true; sold++; return true; }
            return false;
        }
        synchronized boolean bookMethod(int seat) {
            if (seats[seat]) return false;
            seats[seat] = true; sold++; return true;
        }
        boolean bookBlock(int seat) {
            synchronized (this) {
                if (seats[seat]) return false;
                seats[seat] = true; sold++; return true;
            }
        }
    }
    public static void main(String[] args) throws Exception {
        for (int mode = 0; mode < 3; mode++) {
            Booking b = new Booking(100);
            long start = System.nanoTime();
            for (int i = 0; i < 10000; i++) {
                int seat = i % 100;
                if (mode == 1) b.bookMethod(seat);
                if (mode == 2) b.bookBlock(seat);
            }
            System.out.println((mode == 1 ? "synchronized-method" : mode == 2 ? "synchronized-block" : "baseline")
                    + ", elapsed-ns=" + (System.nanoTime() - start) + ", sold=" + b.sold);
        }
    }
}