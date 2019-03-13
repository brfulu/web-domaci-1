package app;

public class Assistant implements Runnable {
    @Override
    public void run() {
        System.out.println("Assistant thread");
    }
}
