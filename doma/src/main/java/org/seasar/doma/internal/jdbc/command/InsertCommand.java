package org.seasar.doma.internal.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.query.InsertQuery;


/**
 * @author taedium
 * 
 */
public class InsertCommand extends ModifyCommand<InsertQuery> {

    public InsertCommand(InsertQuery query) {
        super(query);
    }

    @Override
    protected int executeInternal(PreparedStatement preparedStatement)
            throws SQLException {
        int rows = executeUpdate(preparedStatement);
        query.generateId(preparedStatement);
        return rows;
    }

}
