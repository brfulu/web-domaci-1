package app;

import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    /*
        1. Student se bori i ceka svoj red
        2. Student posalje startEvent predavacu (putem streama)
        3. Predavac obradi startEvent i vrati ime svog threada
        4. Student brani domaci x sekundi (sleep)
        5. Student salje doneEvent predavacu
        6. Predavac prima event i vraca ocjenu
        7. Student zapisuje ocjenu i oslobadja lockove
     */
    public static AtomicInteger gradesTotal = new AtomicInteger();
    public static AtomicInteger studentsCompleted = new AtomicInteger();
    public static long startTime;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int studentCount = scanner.nextInt();
        scanner.close();

        EventStream professorStream = new ProfessorStream();
        EventStream assistantStream = new AssistantStream();

        Lecturer professor = new Professor(professorStream);
        Lecturer assistant = new Assistant(assistantStream);

        ExecutorService lecturerExecutor = Executors.newFixedThreadPool(2);
        lecturerExecutor.execute(professor);
        lecturerExecutor.execute(assistant);

        startTime = System.currentTimeMillis();

        ScheduledExecutorService studentExecutor = Executors.newScheduledThreadPool(20);
        for (int i = 0; i < studentCount; i++) {
            Student s = new Student(professorStream, assistantStream);
            studentExecutor.schedule(s, Helper.getRandomInt(1, 1000), TimeUnit.MILLISECONDS);
        }

        try {
            Thread.sleep(5000);
            lecturerExecutor.shutdownNow();
            studentExecutor.shutdownNow();
            lecturerExecutor.awaitTermination(2, TimeUnit.SECONDS);
            studentExecutor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("GradesTotal=" + gradesTotal.intValue() + " StudentsCompleted=" + studentsCompleted.intValue());
        System.out.println("AS = " + (gradesTotal.doubleValue() / studentsCompleted.doubleValue()));
    }
}
