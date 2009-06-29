package org.seasar.doma.internal.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.seasar.doma.internal.jdbc.query.BatchDeleteQuery;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;


/**
 * @author taedium
 * 
 */
public class BatchDeleteCommand extends BatchModifyCommand<BatchDeleteQuery> {

    public BatchDeleteCommand(BatchDeleteQuery query) {
        super(query);
    }

    @Override
    protected int[] executeInternal(PreparedStatement preparedStatement,
            List<PreparedSql> sqls) throws SQLException {
        return executeBatch(preparedStatement, sqls);
    }

}
