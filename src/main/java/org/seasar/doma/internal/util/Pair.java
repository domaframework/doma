package org.seasar.doma.internal.util;

public class Pair<T, U> {

    public final T fst;

    public final U snd;

    public Pair(T fst, U snd) {
        this.fst = fst;
        this.snd = snd;
    }
}
