package be.appify.util;

public interface Stream<T> {
    void apply(Function<T, Void> function);
}
