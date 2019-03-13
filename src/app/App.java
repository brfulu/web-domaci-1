package app;

import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    // student se javi profesoru da je spreman da pocne
    // student kaze zavrsio sam izlaganje
    // profesor mu u resultu posalje ocjenu
    // student zapise ocjenu u deljenu memoriju
    public static AtomicInteger gradesTotal = new AtomicInteger();

    public static void main(String[] args) {
        EventStream professorStream = new ProfessorStream();
        EventStream assistantStream = new AssistantStream();

        Lecturer professor = new Professor(professorStream);
        Lecturer assistant = new Assistant(assistantStream);

        ExecutorService lecturerExecutor = Executors.newFixedThreadPool(2);
        lecturerExecutor.execute(professor);
        lecturerExecutor.execute(assistant);

        Scanner scanner = new Scanner(System.in);
        int studentCount = scanner.nextInt();
        scanner.close();

        ScheduledExecutorService studentExecutor = Executors.newScheduledThreadPool(10);
        for (int i = 0; i < studentCount; i++) {
            Student s = new Student(professorStream, assistantStream);
            studentExecutor.schedule(s, Helper.getRandomInt(0, 4), TimeUnit.SECONDS);
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
