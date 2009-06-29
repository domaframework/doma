package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.util.Classes;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class DomainResultListHandler<D extends Domain<?, ?>> implements
        ResultSetHandler<List<D>> {

    protected final Class<D> domainClass;

    public DomainResultListHandler(Class<D> domainClass) {
        assertNotNull(domainClass);
        this.domainClass = domainClass;
    }

    @Override
    public List<D> handle(ResultSet resultSet, Query query) throws SQLException {
        DomainFetcher fetcher = new DomainFetcher(query);
        List<D> domains = new ArrayList<D>();
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
            domains.add(domain);
        }
        return domains;
    }

}
