package be.appify.util;

import java.util.List;

public class ListStream<T> implements Stream<T> {
    private List<T> list;

    public ListStream(List<T> list) {
        this.list = list;
    }

    @Override
    public void apply(Procedure<T> procedure) {
        for(T item : list) {
            procedure.apply(item);
        }
    }
}
