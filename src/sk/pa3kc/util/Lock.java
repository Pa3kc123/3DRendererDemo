package sk.pa3kc.util;

public class Lock {
    private final String id;
    private final Object lock = new Object();

    public Lock(String id) {
        this.id = id;
    }

    public void lock() {
        synchronized(this.lock) {
            try {
                this.lock.wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void unlockOne() {
        synchronized(this.lock) {
            this.lock.notify();
        }
    }

    public void unlockAll() {
        synchronized(this.lock) {
            this.lock.notifyAll();
        }
    }

    @Override
    public String toString() {
        return this.id;
    }
}
