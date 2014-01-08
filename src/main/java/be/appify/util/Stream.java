package be.appify.util;

public interface Stream<T> {
    void apply(Procedure<T> procedure);
}
