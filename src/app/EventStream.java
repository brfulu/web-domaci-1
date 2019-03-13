package app;

import java.util.concurrent.BlockingQueue;

public abstract class EventStream {
    protected BlockingQueue<Event> events;

    abstract void lock();
    abstract void unlock();

    public void startConnection() {
        lock();
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

    public Event takeEvent() {
        Event event = null;
        try {
            event = events.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return event;
    }

    public Object getResult(Event event) {
        Object result = new Object();
        try {
            result = event.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
