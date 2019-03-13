package app;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;

public class Student implements Runnable {
    private Map<String, BlockingQueue<Event>> events = new HashMap<>();

    public Student() {
        events.put("professor", App.professorEvents);
        events.put("assistant", App.assistantEvents);
    }

    @Override
    public void run() {
        waitTurn();
        int grade = getHomeworkGrade();
        saveGrade(grade);
    }

    private void saveGrade(int grade) {
        App.gradesTotal.addAndGet(grade);
    }

    private int getHomeworkGrade() {
        Event ready = new Event("ready", null);
        sendEvent("professor", ready);
        getEventResult(ready);

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Event done = new Event("done", null);
        sendEvent("professor", done);

        App.semaphore.release();

        int grade = -1;
        try {
            grade = (Integer) getEventResult(done);
            System.out.println("Grade = " + grade);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grade;
    }

    private void waitTurn() {
        try {
            App.semaphore.acquire();
            App.barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private Object getEventResult(Event event) {
        Object result = new Object();
        try {
            result = event.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void sendEvent(String receiver, Event event) {
        try {
            events.get(receiver).put(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
