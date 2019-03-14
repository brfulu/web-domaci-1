package app;

public class Student implements Runnable {
    private EventStream professorStream;
    private EventStream assistantStream;
    private long arrivalTime;
    private long duration;
    private long startTime;
    private String lecturerThread;

    public Student(EventStream professorStream, EventStream assistantStream) {
        this.professorStream = professorStream;
        this.assistantStream = assistantStream;
    }

    @Override
    public void run() {
        arrivalTime = System.currentTimeMillis();
        int grade = -1;
        boolean askProfessor = Helper.getRandomBoolean();
        while (grade == -1) {
            try {
                EventStream stream = askProfessor ? professorStream : assistantStream;
                grade = getHomeworkGrade(stream);
                if (grade != -1) {
                    System.out.println("Thread: " + Thread.currentThread().getName()
                            + "  Arrival: " + (arrivalTime - App.startTime)
                            + "  Prof: " + lecturerThread
                            + "  TTC: " + duration + ":" + (startTime - App.startTime)
                            + "  Score: " + grade);
                    saveGrade(grade);
                    break;
                }
                askProfessor = !askProfessor;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void saveGrade(int grade) {
        App.gradesTotal.addAndGet(grade);
        App.studentsCompleted.incrementAndGet();
    }

    private int getHomeworkGrade(EventStream stream) throws InterruptedException {
        boolean success = stream.startConnection();
        if (!success) {
            return -1;
        }

        startTime = System.currentTimeMillis();
        int grade = -1;
        Event startEvent = new Event(Thread.currentThread().getName(), "start", null);
        if (!stream.putEvent(startEvent)) {
            throw new InterruptedException();
        }

        try {
            lecturerThread = (String) stream.getResult(startEvent);
            defendHomework();

            Event doneEvent = new Event(Thread.currentThread().getName(), "done", null);
            stream.putEvent(doneEvent);
            grade = (Integer) stream.getResult(doneEvent);
        } catch (InterruptedException e) {
            grade = (Integer) stream.getResult(startEvent);
            Thread.currentThread().interrupt();
        } finally {
            stream.endConnection();
        }
        return grade;
    }

    private void defendHomework() throws InterruptedException {
        duration = Helper.getRandomInt(500, 1000);
        Thread.sleep(duration);
    }
}
