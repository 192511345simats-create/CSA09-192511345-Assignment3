import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ResultProcessor {
    static final Path DATA = Path.of("scores");
    static class Outcome {
        final boolean success; final String detail;
        Outcome(boolean success, String detail) { this.success = success; this.detail = detail; }
    }
    static Callable<Outcome> task(Path file) {
        return () -> {
            try {
                String line = Files.readString(file).trim();
                String[] values = line.split(",");
                if (values.length != 3) throw new IOException("Expected id,mark1,mark2");
                int m1 = Integer.parseInt(values[1]), m2 = Integer.parseInt(values[2]);
                if (m1 < 0 || m1 > 100 || m2 < 0 || m2 > 100)
                    throw new IllegalArgumentException("Marks outside 0..100");
                return new Outcome(true, values[0] + "," + ((m1 + m2) / 2));
            } catch (Exception e) {
                return new Outcome(false, file.getFileName() + ": " + e.getMessage());
            }
        };
    }
    static void prepare(int count) throws IOException {
        Files.createDirectories(DATA);
        for (int i = 0; i < count; i++) {
            Path f = DATA.resolve(String.format("score-%05d.csv", i));
            if (i % 100 == 0) Files.deleteIfExists(f); // intentional missing-file case
            else if (i % 100 == 1) Files.writeString(f, "corrupt-record"); // intentional corrupt case
            else Files.writeString(f, "STU" + i + "," + (i % 101) + "," + ((i * 3) % 101));
        }
    }
    static void run(int poolSize, int count) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        ConcurrentMap<String, Outcome> results = new ConcurrentHashMap<>();
        List<Future<Outcome>> futures = new ArrayList<>();
        long start = System.nanoTime();
        try {
            for (int i = 0; i < count; i++) {
                Path file = DATA.resolve(String.format("score-%05d.csv", i));
                futures.add(pool.submit(task(file)));
            }
            for (int i = 0; i < futures.size(); i++) {
                Outcome result = futures.get(i).get();
                results.put("file-" + i, result);
            }
        } finally {
            pool.shutdown();
            if (!pool.awaitTermination(1, TimeUnit.MINUTES)) pool.shutdownNow();
        }
        long elapsed = System.nanoTime() - start;
        long ok = results.values().stream().filter(x -> x.success).count();
        long failed = results.size() - ok;
        System.out.println("pool=" + poolSize + ", success=" + ok + ", failed=" + failed
                + ", total=" + results.size() + ", elapsed-ms=" + (elapsed / 1_000_000.0));
        if (results.size() != count) throw new IllegalStateException("Incomplete result summary");
    }
    public static void main(String[] args) throws Exception {
        int count = args.length == 0 ? 10000 : Integer.parseInt(args[0]);
        prepare(count);
        for (int p : new int[]{1, 2, 4, 8}) run(p, count);
    }
}