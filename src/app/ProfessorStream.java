package app;

import java.util.concurrent.*;

public class ProfessorStream extends EventStream {
    private Semaphore semaphore;
    private CyclicBarrier barrier;

    public ProfessorStream() {
        this.events = new LinkedBlockingQueue<>();
        this.semaphore = new Semaphore(2);
        this.barrier = new CyclicBarrier(2);
    }

    @Override
    boolean lock() throws InterruptedException {
        try {
            boolean hasAcquired = semaphore.tryAcquire(500, TimeUnit.MILLISECONDS);
            if (!hasAcquired) return false;
            barrier.await(500, TimeUnit.MILLISECONDS);
        } catch (BrokenBarrierException e) {
            return false;
        } catch (TimeoutException e) {
            semaphore.release();
            return false;
        }
        return true;
    }

    @Override
    void unlock() {
        semaphore.release();
    }
}
