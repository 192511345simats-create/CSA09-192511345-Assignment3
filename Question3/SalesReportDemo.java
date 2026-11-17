import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SalesReportDemo {
    private static final Path INPUT = Path.of("input");
    private static final Path OUTPUT = Path.of("sales-summary.txt");

    static void prepareFiles() throws IOException {
        Files.createDirectories(INPUT);
        Files.writeString(INPUT.resolve("store1.txt"), "Chennai,1200\n");
        Files.writeString(INPUT.resolve("store2.txt"), "Vellore,900\n");
        Files.writeString(INPUT.resolve("store3.txt"), "InvalidRecord\n");
    }

    // Demonstrates a fragile manual-close pattern: an exception before close can leak.
    static void manualReport() {
        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(OUTPUT);
            for (String name : List.of("store1.txt", "store2.txt", "store3.txt")) {
                BufferedReader reader = Files.newBufferedReader(INPUT.resolve(name));
                String line = reader.readLine();
                String[] parts = line.split(",");
                if (parts.length != 2) throw new IOException("Malformed record in " + name);
                writer.write(parts[0] + ": " + parts[1]);
                writer.newLine();
                reader.close();
            }
        } catch (IOException e) {
            System.out.println("Manual version failed: " + e.getMessage());
        } finally {
            if (writer != null) try { writer.close(); } catch (IOException e) {
                System.out.println("Writer close failed: " + e.getMessage());
            }
        }
    }

    static void safeReport() {
        try (BufferedWriter writer = Files.newBufferedWriter(OUTPUT)) {
            for (String name : List.of("store1.txt", "store2.txt", "store3.txt")) {
                try (BufferedReader reader = Files.newBufferedReader(INPUT.resolve(name))) {
                    String line = reader.readLine();
                    String[] parts = line.split(",");
                    if (parts.length != 2) throw new IOException("Malformed record in " + name);
                    writer.write(parts[0] + ": " + parts[1]);
                    writer.newLine();
                } catch (IOException e) {
                    System.out.println("Skipped " + name + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not create report: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        prepareFiles();
        manualReport();
        safeReport();
        System.out.println("Try-with-resources report:");
        Files.readAllLines(OUTPUT).forEach(System.out::println);
    }
}