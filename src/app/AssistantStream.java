package app;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.locks.ReentrantLock;

public class AssistantStream extends EventStream {
    private ReentrantLock lock;

    public AssistantStream() {
        this.events = new SynchronousQueue<>();
        this.lock = new ReentrantLock();
    }

    @Override
    void lock() {
        lock.lock();
    }

    @Override
    void unlock() {
        lock.unlock();
    }
}
