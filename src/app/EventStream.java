package app;

import java.util.concurrent.BlockingQueue;

public abstract class EventStream {
    protected BlockingQueue<Event> events;

    abstract boolean lock();

    abstract void unlock();

    public boolean startConnection() {
        return lock();
    }

    public void endConnection() {
        unlock();
    }

    public void putEvent(Event event) {
        try {
            events.put(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Event takeEvent() throws InterruptedException {
        return events.take();
    }

    public Object getResult(Event event) throws InterruptedException {
        return event.getResult();
    }
}
