package org.seasar.doma.internal.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.query.DeleteQuery;


/**
 * @author taedium
 * 
 */
public class DeleteCommand extends ModifyCommand<DeleteQuery> {

    public DeleteCommand(DeleteQuery query) {
        super(query);
    }

    @Override
    protected int executeInternal(PreparedStatement preparedStatement)
            throws SQLException {
        return executeUpdate(preparedStatement);
    }

}
