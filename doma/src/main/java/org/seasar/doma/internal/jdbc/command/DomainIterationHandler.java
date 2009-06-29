package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.util.Classes;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class DomainIterationHandler<R, D extends Domain<?, ?>> implements
        ResultSetHandler<R> {

    protected final Class<D> domainClass;

    protected final IterationCallback<R, D> iterationCallback;

    public DomainIterationHandler(Class<D> domainClass,
            IterationCallback<R, D> iterationCallback) {
        assertNotNull(domainClass);
        this.domainClass = domainClass;
        this.iterationCallback = iterationCallback;
    }

    @Override
    public R handle(ResultSet resultSet, Query query) throws SQLException {
        DomainFetcher fetcher = new DomainFetcher(query);
        IterationContext iterationContext = new IterationContext();
        R result = null;
        while (resultSet.next()) {
            D domain = null;
            try {
                domain = Classes.newInstance(domainClass);
            } catch (WrapException e) {
                Throwable cause = e.getCause();
                throw new JdbcException(MessageCode.DOMA2006, cause,
                        domainClass.getName(), cause);
            }
            fetcher.fetch(resultSet, domain);
            result = iterationCallback.iterate(domain, iterationContext);
            if (iterationContext.isExited()) {
                return result;
            }
        }
        return result;
    }

}
