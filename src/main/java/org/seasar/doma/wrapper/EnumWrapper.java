package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/**
 * A wrapper for the {@link Enum} class.
 * 
 * @param <E>
 *            The enum type subclass
 */
public class EnumWrapper<E extends Enum<E>> extends AbstractWrapper<E> {

    /**
     * Creates an instance.
     * 
     * @param enumClass
     *            the {@link Enum} class
     * @throws DomaNullPointerException
     *             if the {@code enumClass} is {@code null}
     */
    public EnumWrapper(Class<E> enumClass) {
        this(enumClass, null);
    }

    /**
     * Creates an instance with a value.
     * 
     * @param enumClass
     *            the {@link Enum} class
     * @param value
     *            the enum value
     * @throws DomaNullPointerException
     *             if the {@code enumClass} is {@code null}
     */
    public EnumWrapper(Class<E> enumClass, E value) {
        super(enumClass, value);
        if (enumClass == null) {
            throw new DomaNullPointerException("enumClass");
        }
    }

    @Override
    public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
            throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        return visitor.visitEnumWrapper(this, p, q);
    }
}
