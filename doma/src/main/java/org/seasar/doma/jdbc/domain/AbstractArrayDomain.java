package org.seasar.doma.jdbc.domain;

import java.sql.Array;
import java.sql.SQLException;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.domain.AbstractDomain;
import org.seasar.doma.domain.DomainVisitor;

/**
 * @author taedium
 * 
 */
public class AbstractArrayDomain<E> extends
        AbstractDomain<Array, AbstractArrayDomain<E>> {

    public AbstractArrayDomain() {
        super(null);
    }

    public AbstractArrayDomain(Array v) {
        super(v);
    }

    @SuppressWarnings("unchecked")
    public E[] getArray() {
        try {
            return (E[]) value.getArray();
        } catch (SQLException e) {
            throw new JdbcArrayException(e);
        }
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (AbstractArrayDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractArrayDomainVisitor<R, P, TH> v = AbstractArrayDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractArrayDomain(this, p);
        }
        return visitor.visitUnknownDomain(this, p);
    }

}
