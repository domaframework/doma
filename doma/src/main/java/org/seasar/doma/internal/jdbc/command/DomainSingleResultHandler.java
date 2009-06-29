package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.util.Classes;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.NonUniqueResultException;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class DomainSingleResultHandler<D extends Domain<?, ?>> implements
        ResultSetHandler<D> {

    protected final Class<D> domainClass;

    public DomainSingleResultHandler(Class<D> domainClass) {
        assertNotNull(domainClass);
        this.domainClass = domainClass;
    }

    @Override
    public D handle(ResultSet resultSet, Query query) throws SQLException {
        DomainFetcher fetcher = new DomainFetcher(query);
        D domain = null;
        if (resultSet.next()) {
            try {
                domain = Classes.newInstance(domainClass);
            } catch (WrapException e) {
                Throwable cause = e.getCause();
                throw new JdbcException(MessageCode.DOMA2006, cause,
                        domainClass.getName(), cause);
            }
            fetcher.fetch(resultSet, domain);
            if (resultSet.next()) {
                throw new NonUniqueResultException(query.getSql());
            }
        }
        return domain;
    }

}
