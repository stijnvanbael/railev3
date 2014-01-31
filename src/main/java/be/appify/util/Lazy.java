package be.appify.util;

/**
 * A thread-safe object reference that will only initialize its value when get() is called.
 */
public abstract class Lazy<T> {
    private volatile T object;
    private volatile boolean initialized;

    public T get() {
        T result = object;

        if (!initialized) {
            synchronized (this) {
                result = object;
                if (!initialized) {
                    object = result = initialize();
                    initialized = true;
                }
            }
        }

        return result;
    }

    protected abstract T initialize();

    public synchronized boolean isInitialized() {
        return initialized;
    }

    public synchronized void reset() {
        initialized = false;
    }
}