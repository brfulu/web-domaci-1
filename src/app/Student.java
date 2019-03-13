package app;

public class Student implements Runnable {
    private EventStream professorStream;
    private EventStream assistantStream;

    public Student(EventStream professorStream, EventStream assistantStream) {
        this.professorStream = professorStream;
        this.assistantStream = assistantStream;
    }

    @Override
    public void run() {
        int grade = -1;
        boolean askProfessor = false;
        while (grade == -1) {
            EventStream stream = askProfessor ? professorStream : assistantStream;
            grade = getHomeworkGrade(stream);
            if (grade != -1) {
                System.out.println("Grade = " + grade);
                saveGrade(grade);
                break;
            }
            askProfessor = !askProfessor;
        }
    }

    private void saveGrade(int grade) {
        App.gradesTotal.addAndGet(grade);
    }

    private int getHomeworkGrade(EventStream stream) {
        stream.startConnection();

        Event ready = new Event("ready", null);
        stream.putEvent(ready);
        stream.getResult(ready);

        defendHomework();

        Event done = new Event("done", null);
        stream.putEvent(done);
        int grade = (Integer) stream.getResult(done);

        stream.endConnection();
        return grade;
    }

    private void defendHomework() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
