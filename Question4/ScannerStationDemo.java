import java.util.concurrent.atomic.AtomicInteger;

class ThreadStation extends Thread {
    private final int scripts;
    ThreadStation(String name, int scripts) { super(name); this.scripts = scripts; }
    public void run() {
        try { Thread.sleep(100 + (int)(Math.random() * 300)); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
        System.out.println(getName() + " scanned " + scripts + " scripts.");
    }
}
class RunnableStation implements Runnable {
    private final String name;
    private final int scripts;
    RunnableStation(String name, int scripts) { this.name = name; this.scripts = scripts; }
    public void run() {
        try { Thread.sleep(100 + (int)(Math.random() * 300)); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
        System.out.println(name + " scanned " + scripts + " scripts.");
    }
}
public class ScannerStationDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread[] stations = {
            new ThreadStation("Station-1", 25),
            new Thread(new RunnableStation("Station-2", 30), "Station-2"),
            new ThreadStation("Station-3", 20),
            new Thread(new RunnableStation("Station-4", 25), "Station-4")
        };
        for (Thread t : stations) t.start();
        for (Thread t : stations) System.out.println(t.getName() + " alive=" + t.isAlive());
        for (Thread t : stations) t.join();
        System.out.println("All stations completed. Total scripts: 100");
    }
}