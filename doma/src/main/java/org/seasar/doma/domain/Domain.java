package org.seasar.doma.domain;

/**
 * @author taedium
 * 
 */
public interface Domain<V, D extends Domain<V, D>> {

    V get();

    void set(V value);

    void set(D other);

    boolean isNull();

    boolean isNotNull();

    boolean isChanged();

    void setChanged(boolean changed);

    <R, P, TH extends Throwable> R accept(DomainVisitor<R, P, TH> visitor, P p)
            throws TH;
}
