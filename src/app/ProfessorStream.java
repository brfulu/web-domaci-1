package app;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class ProfessorStream extends EventStream {
    private Semaphore semaphore;
    private CyclicBarrier barrier;

    public ProfessorStream() {
        this.events = new LinkedBlockingQueue<>();
        this.semaphore = new Semaphore(2);
        this.barrier = new CyclicBarrier(2);
    }

    @Override
    void lock() {
        try {
            semaphore.acquire();
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    @Override
    void unlock() {
        semaphore.release();
    }
}
