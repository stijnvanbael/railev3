package be.appify.util;

import java.util.ArrayList;
import java.util.List;

public final class Lists {
    private Lists() {
    }

    public static <T> Stream<T> forEach(List<T> list) {
        return new ListStream<>(list);
    }

    public static <T, U> List<U> transform(List<T> list, Function<T, U> transformation) {
        List<U> results = new ArrayList<>();
        for(T item : list) {
            results.add(transformation.apply(item));
        }
        return results;
    }
}
