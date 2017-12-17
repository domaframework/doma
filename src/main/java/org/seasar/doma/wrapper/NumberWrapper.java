package org.seasar.doma.wrapper;

/**
 * A wrapper for the {@link Number} class.
 * 
 * @param <BASIC>
 *            The number type subclass
 */
public interface NumberWrapper<BASIC extends Number> extends Wrapper<BASIC> {

    @Override
    void set(Number value);

    /**
     * Increments this object.
     */
    void increment();

    /**
     * Decrements this object.
     */
    void decrement();

}
