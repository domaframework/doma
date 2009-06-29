package org.seasar.doma.internal.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.seasar.doma.internal.jdbc.query.BatchUpdateQuery;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;


/**
 * @author taedium
 * 
 */
public class BatchUpdateCommand extends BatchModifyCommand<BatchUpdateQuery> {

    public BatchUpdateCommand(BatchUpdateQuery query) {
        super(query);
    }

    @Override
    protected int[] executeInternal(PreparedStatement preparedStatement,
            List<PreparedSql> sqls) throws SQLException {
        int[] rows = executeBatch(preparedStatement, sqls);
        query.incrementVersions();
        return rows;
    }

}
