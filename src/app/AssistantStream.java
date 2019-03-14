package app;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AssistantStream extends EventStream {
    private ReentrantLock lock;

    public AssistantStream() {
        this.events = new SynchronousQueue<>();
        this.lock = new ReentrantLock();
    }

    @Override
    boolean lock() throws InterruptedException {
        return lock.tryLock(500, TimeUnit.MILLISECONDS);
    }

    @Override
    void unlock() {
        lock.unlock();
    }
}
