package org.seasar.doma.internal.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.query.UpdateQuery;


/**
 * @author taedium
 * 
 */
public class UpdateCommand extends ModifyCommand<UpdateQuery> {

    public UpdateCommand(UpdateQuery query) {
        super(query);
    }

    @Override
    protected int executeInternal(PreparedStatement preparedStatement)
            throws SQLException {
        int rows = executeUpdate(preparedStatement);
        query.incrementVersion();
        return rows;
    }

}
