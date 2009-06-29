package org.seasar.doma.jdbc;

import java.sql.SQLException;

import org.seasar.doma.domain.AbstractBigDecimalDomain;
import org.seasar.doma.domain.AbstractDateDomain;
import org.seasar.doma.domain.AbstractIntegerDomain;
import org.seasar.doma.domain.AbstractStringDomain;
import org.seasar.doma.domain.AbstractTimeDomain;
import org.seasar.doma.domain.AbstractTimestampDomain;
import org.seasar.doma.domain.BuiltInDomainVisitor;
import org.seasar.doma.domain.Domain;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class JdbcMappingVisitor implements
        BuiltInDomainVisitor<Void, JdbcMappingFunction, SQLException> {

    @Override
    public Void visitAbstractStringDomain(AbstractStringDomain<?> domain,
            JdbcMappingFunction p) throws SQLException {
        p.apply(domain, JdbcTypes.STRING);
        return null;
    }

    @Override
    public Void visitAbstractBigDecimalDomain(
            AbstractBigDecimalDomain<?> domain, JdbcMappingFunction p)
            throws SQLException {
        p.apply(domain, JdbcTypes.BIGDECIMAL);
        return null;
    }

    @Override
    public Void visitAbstractDateDomain(AbstractDateDomain<?> domain,
            JdbcMappingFunction p) throws SQLException {
        p.apply(domain, JdbcTypes.DATE);
        return null;
    }

    @Override
    public Void visitAbstractIntegerDomain(AbstractIntegerDomain<?> domain,
            JdbcMappingFunction p) throws SQLException {
        p.apply(domain, JdbcTypes.INTEGER);
        return null;
    }

    @Override
    public Void visitAbstractTimestampDomain(AbstractTimestampDomain<?> domain,
            JdbcMappingFunction p) throws SQLException {
        p.apply(domain, JdbcTypes.TIMESTAMP);
        return null;
    }

    @Override
    public Void visitAbstractTimeDomain(AbstractTimeDomain<?> domain,
            JdbcMappingFunction p) throws SQLException {
        p.apply(domain, JdbcTypes.TIME);
        return null;
    }

    @Override
    public Void visitUnknownDomain(Domain<?, ?> domain, JdbcMappingFunction p)
            throws SQLException {
        throw new JdbcException(MessageCode.DOMA2019, domain.getClass()
                .getName());
    }
}
