package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.jdbc.query.Query;


/**
 * @author taedium
 * 
 */
public class DomainFetcher {

    protected final Query query;

    public DomainFetcher(Query query) throws SQLException {
        assertNotNull(query);
        this.query = query;
    }

    public void fetch(ResultSet resultSet, Domain<?, ?> domain)
            throws SQLException {
        ResultSetMetaData resultSetMeta = resultSet.getMetaData();
        if (resultSetMeta.getColumnCount() > 0) {
            GetValueFunction function = new GetValueFunction(resultSet, 1);
            domain.accept(query.getConfig().jdbcMappingVisitor(), function);
        }
    }
}
