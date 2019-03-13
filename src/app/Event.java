package app;

import java.util.concurrent.CountDownLatch;

public class Event {
    private final String sender;
    private final String type;
    private final Object data;
    private final CountDownLatch resultReady;
    private Object result;

    public Event(String sender, String type, Object data) {
        this.sender = sender;
        this.type = type;
        this.data = data;
        this.resultReady = new CountDownLatch(1);
    }

    public void setResult(Object result) {
        this.result = result;
        resultReady.countDown();
    }

    public Object getResult() throws InterruptedException {
        resultReady.await();
        return result;
    }

    public String getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }
}
