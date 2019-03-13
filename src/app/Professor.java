package app;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Professor implements Runnable {
    private boolean interrupted = false;

    @Override
    public void run() {
        while (!interrupted) {
            Event event = receiveEvent();
            if (event.getType() == "ready") {
                event.setResult("ok");
            } else if (event.getType() == "done") {
                event.setResult(Helper.getRandomInt(0, 10));
            }
        }
    }

    private Event receiveEvent() {
        Event event = null;
        try {
            event = App.professorEvents.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return event;
    }
}
