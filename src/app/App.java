package app;

import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    // student se javi profesoru da je spreman da pocne
    // student kaze zavrsio sam izlaganje
    // profesor mu u resultu posalje ocjenu
    // student zapise ocjenu u deljenu memoriju

    public static BlockingQueue<Event> professorEvents = new LinkedBlockingQueue<>();
    public static BlockingQueue<Event> assistantEvents = new SynchronousQueue<>();

    public static Semaphore semaphore = new Semaphore(2);
    public static CyclicBarrier barrier = new CyclicBarrier(2);

    public static AtomicInteger gradesTotal = new AtomicInteger();

    public static void main(String[] args) {
        Professor professor = new Professor();
        Assistant assistant = new Assistant();

        ExecutorService lecturerExecutor = Executors.newFixedThreadPool(2);
        lecturerExecutor.execute(professor);
        lecturerExecutor.execute(assistant);

        ScheduledExecutorService studentExecutor = Executors.newScheduledThreadPool(10);

        Scanner scanner = new Scanner(System.in);
        int studentCount = scanner.nextInt();
        scanner.close();

        for (int i = 0; i < studentCount; i++) {
            Student s = new Student();
            studentExecutor.schedule(s, 0, TimeUnit.SECONDS);
        }

        lecturerExecutor.shutdown();
        studentExecutor.shutdown();

        try {
            lecturerExecutor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("AS = " + (gradesTotal.doubleValue() / studentCount));
    }
}
