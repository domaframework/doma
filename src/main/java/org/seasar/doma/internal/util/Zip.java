package org.seasar.doma.internal.util;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Zip {

    public static <T, U> Stream<Pair<T, U>> stream(Stream<T> first, Stream<U> second) {
        assertNotNull(first, second);
        return streamInternal(first.iterator(), second.iterator());
    }

    public static <T, U> Stream<Pair<T, U>> streamInternal(Iterator<T> first, Iterator<U> second) {
        Iterator<Pair<T, U>> iterator = new Iterator<>() {
            @Override
            public boolean hasNext() {
                return first.hasNext() && second.hasNext();
            }

            @Override
            public Pair<T, U> next() {
                return new Pair<>(first.next(), second.next());
            }
        };
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
    }

}
