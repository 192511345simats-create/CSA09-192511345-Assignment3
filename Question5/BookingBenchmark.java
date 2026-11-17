import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BookingBenchmark {
    static class Booking {
        final boolean[] seats = new boolean[100];
        int sold;

        boolean unsafe(int seat) {
            if (seats[seat]) return false;
            seats[seat] = true;
            sold++;
            return true;
        }
        synchronized boolean synchronizedMethod(int seat) {
            if (seats[seat]) return false;
            seats[seat] = true;
            sold++;
            return true;
        }
        boolean synchronizedBlock(int seat) {
            synchronized (this) {
                if (seats[seat]) return false;
                seats[seat] = true;
                sold++;
                return true;
            }
        }
    }

    static void measure(String label, int mode) throws Exception {
        Booking booking = new Booking();
        ExecutorService pool = Executors.newFixedThreadPool(16);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<?>> jobs = new ArrayList<>();
        long begin = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            final int seat = i % booking.seats.length;
            jobs.add(pool.submit(() -> {
                try { start.await(); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                if (mode == 0) booking.unsafe(seat);
                else if (mode == 1) booking.synchronizedMethod(seat);
                else booking.synchronizedBlock(seat);
            }));
        }
        start.countDown();
        for (Future<?> job : jobs) job.get();
        long elapsed = System.nanoTime() - begin;
        pool.shutdown();
        System.out.println(label + ", elapsed-ms=" + (elapsed / 1_000_000.0)
                + ", reported-sold=" + booking.sold);
    }

    public static void main(String[] args) throws Exception {
        measure("unsynchronized", 0);
        measure("synchronized-method", 1);
        measure("synchronized-block", 2);
    }
}