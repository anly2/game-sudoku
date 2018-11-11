package me.aanchev.sudoku.utils;

import java.util.Optional;
import java.util.stream.Stream;


public class StreamExtensions {
    private StreamExtensions() {
    }


    public static <E> Iterable<E> iterate(Stream<E> stream) {
        return stream::iterator;
    }

    @SuppressWarnings("unchecked")
    public static <E> Optional<E> findExactlyOne(Stream<E> stream) {
        Object[] r = {null};
        return (stream.limit(2).peek(e -> r[0] = e).count() == 1) ?
                Optional.ofNullable((E) r[0]) : Optional.empty();
    }

}
