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
    boolean lock() {
        try {
            boolean hasAcquired = semaphore.tryAcquire(1, TimeUnit.SECONDS);
            if (!hasAcquired) return false;
            barrier.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        } catch (BrokenBarrierException e) {
            System.out.println("Pukla barijera");
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
