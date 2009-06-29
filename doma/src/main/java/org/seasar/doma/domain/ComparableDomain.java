package org.seasar.doma.domain;

/**
 * @author taedium
 * 
 */
public interface ComparableDomain<V extends Comparable<? super V>, D extends Domain<V, D> & Comparable<? super D>>
        extends Domain<V, D>, Comparable<D> {

    int compareTo(D other);

    boolean eq(V other);

    boolean eq(D other);

    boolean ge(V other);

    boolean ge(D other);

    boolean gt(V other);

    boolean gt(D other);

    boolean le(V other);

    boolean le(D other);

    boolean lt(V other);

    boolean lt(D other);

}
