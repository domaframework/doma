package org.seasar.doma.jdbc;

import org.seasar.doma.domain.AbstractBigDecimalDomain;
import org.seasar.doma.domain.AbstractDateDomain;
import org.seasar.doma.domain.AbstractIntegerDomain;
import org.seasar.doma.domain.AbstractStringDomain;
import org.seasar.doma.domain.AbstractTimeDomain;
import org.seasar.doma.domain.AbstractTimestampDomain;
import org.seasar.doma.domain.BuiltInDomainVisitor;
import org.seasar.doma.domain.Domain;

/**
 * @author taedium
 * 
 */
public class SqlLogFormattingVisitor implements
        BuiltInDomainVisitor<String, Void, RuntimeException> {

    protected static final String NULL = "null";

    @Override
    public String visitAbstractBigDecimalDomain(
            AbstractBigDecimalDomain<?> domain, Void p) {
        if (domain.isNull()) {
            return NULL;
        }
        return domain.get().toPlainString();
    }

    @Override
    public String visitAbstractDateDomain(AbstractDateDomain<?> domain, Void p) {
        if (domain.isNull()) {
            return NULL;
        }
        return "'" + domain.get() + "'";
    }

    @Override
    public String visitAbstractIntegerDomain(AbstractIntegerDomain<?> domain,
            Void p) {
        if (domain.isNull()) {
            return NULL;
        }
        return domain.get().toString();
    }

    @Override
    public String visitAbstractStringDomain(AbstractStringDomain<?> domain,
            Void p) {
        if (domain.isNull()) {
            return NULL;
        }
        return "'" + domain.get() + "'";
    }

    @Override
    public String visitAbstractTimeDomain(AbstractTimeDomain<?> domain, Void p) {
        if (domain.isNull()) {
            return NULL;
        }
        return "'" + domain.get() + "'";
    }

    @Override
    public String visitAbstractTimestampDomain(
            AbstractTimestampDomain<?> domain, Void p) {
        if (domain.isNull()) {
            return NULL;
        }
        return "'" + domain.get() + "'";
    }

    @Override
    public String visitUnknownDomain(Domain<?, ?> domain, Void p) {
        if (domain.isNull()) {
            return NULL;
        }
        return domain.toString();
    }

}
