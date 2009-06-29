package org.seasar.doma.domain;

/**
 * @author taedium
 * 
 */
public interface NumberDomain<V extends Number, D extends NumberDomain<V, D>>
        extends Domain<V, D> {

    void set(Number value);

    void set(NumberDomain<V, D> value);

}
