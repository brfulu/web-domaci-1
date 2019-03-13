package app;

public abstract class Lecturer implements Runnable {
    private boolean interrupted = false;
    protected EventStream stream;

    @Override
    public void run() {
        while (!interrupted) {
            Event event = stream.takeEvent();
            if (event.getType() == "ready") {
                event.setResult("ok");
            } else if (event.getType() == "done") {
                event.setResult(Helper.getRandomInt(0, 10));
            }
        }
    }
}
