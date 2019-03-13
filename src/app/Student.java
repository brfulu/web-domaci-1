package app;

public class Student implements Runnable {
    private EventStream professorStream;
    private EventStream assistantStream;
    private long arrivalTime;
    private long duration;

    public Student(EventStream professorStream, EventStream assistantStream) {
        this.professorStream = professorStream;
        this.assistantStream = assistantStream;
    }

    @Override
    public void run() {
        arrivalTime = System.currentTimeMillis();
        int grade = -1;
        boolean askProfessor = true;
        while (grade == -1) {
            try {
                EventStream stream = askProfessor ? professorStream : assistantStream;
                grade = getHomeworkGrade(stream);
                if (grade != -1) {
                    System.out.println("Grade = " + grade + " " + askProfessor);
                    saveGrade(grade);
                    System.out.println("Snimljeno");
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
    }

    private int getHomeworkGrade(EventStream stream) throws InterruptedException {
        boolean success = stream.startConnection();
        if (!success) {
            return -1;
        }

        int grade = -1;
        Event startEvent = new Event(Thread.currentThread().getName(),"start", null);
        try {
            stream.putEvent(startEvent);

            defendHomework();

            Event doneEvent = new Event(Thread.currentThread().getName(), "done", null);
            stream.putEvent(doneEvent);
            grade = (Integer) stream.getResult(doneEvent);
        } catch (InterruptedException e) {
            grade = (Integer) stream.getResult(startEvent);
          //  saveGrade(grade);
        } finally {
            stream.endConnection();
        }
        return grade;
    }

    private void defendHomework() throws InterruptedException {
        duration = Helper.getRandomInt(2000, 2000);
        Thread.sleep(duration);
    }
}
