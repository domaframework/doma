package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;


/**
 * 
 * @author taedium
 * 
 */
public class PreparedSqlParameterBinder {

    protected final Query query;

    public PreparedSqlParameterBinder(Query query) {
        assertNotNull(query);
        this.query = query;
    }

    public void bind(PreparedStatement preparedStatement,
            List<? extends PreparedSqlParameter> paramters) throws SQLException {
        assertNotNull(preparedStatement, paramters);
        int index = 1;
        for (PreparedSqlParameter p : paramters) {
            SetValueFunction function = new SetValueFunction(preparedStatement,
                    index);
            Domain<?, ?> domain = p.getDomain();
            domain.accept(query.getConfig().jdbcMappingVisitor(), function);
            index++;
        }
    }

}
