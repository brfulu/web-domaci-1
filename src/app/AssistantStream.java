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
    boolean lock() {
        try {
            return lock.tryLock(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Nemaaaaaa");
            return false;
        }
    }

    @Override
    void unlock() {
        lock.unlock();
    }
}
