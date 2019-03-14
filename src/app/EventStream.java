package app;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class EventStream {
    protected BlockingQueue<Event> events;

    abstract boolean lock() throws InterruptedException;

    abstract void unlock();

    public boolean startConnection() throws InterruptedException {
        return lock();
    }

    public void endConnection() {
        unlock();
    }

    public boolean putEvent(Event event) throws InterruptedException {
        return events.offer(event, 2, java.util.concurrent.TimeUnit.SECONDS);
    }

    public Event takeEvent() throws InterruptedException {
        return events.take();
    }

    public Object getResult(Event event) throws InterruptedException {
        return event.getResult();
    }
}
