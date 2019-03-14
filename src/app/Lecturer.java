package app;

import java.util.ArrayList;
import java.util.List;

public abstract class Lecturer implements Runnable {
    private boolean interrupted = false;
    protected EventStream stream;
    private List<Event> startEvents = new ArrayList<>();

    @Override
    public void run() {
        while (!interrupted) {
            Event event = null;
            try {
                event = stream.takeEvent();
                if (event.getType() == "start") {
                    startEvents.add(event);
                    event.setResult(Thread.currentThread().getName());
                } else if (event.getType() == "done") {
                    event.setResult(Helper.getRandomInt(0, 10));
                    removeOldEvents(event);
                }
            } catch (InterruptedException ex) {
                for (Event startEvent : startEvents) {
                    startEvent.setResult(Helper.getRandomInt(0, 10));
                }
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void removeOldEvents(Event event) {
        List<Event> found = new ArrayList<>();
        for (Event e : startEvents) {
            if (e.getSender().equals(event.getSender())) {
                found.add(e);
            }
        }
        startEvents.removeAll(found);
    }
}
