package org.seasar.doma.internal.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.seasar.doma.internal.jdbc.query.BatchInsertQuery;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.jdbc.Dialect;
import org.seasar.doma.jdbc.UniqueConstraintException;


/**
 * @author taedium
 * 
 */
public class BatchInsertCommand extends BatchModifyCommand<BatchInsertQuery> {

    public BatchInsertCommand(BatchInsertQuery query) {
        super(query);
    }

    @Override
    protected int[] executeInternal(PreparedStatement preparedStatement,
            List<PreparedSql> sqls) throws SQLException {
        if (query.isBatchSupported()) {
            return executeBatch(preparedStatement, sqls);
        }
        int[] updatedRows = new int[sqls.size()];
        int sqlSize = sqls.size();
        for (int i = 0; i < sqlSize; i++) {
            PreparedSql sql = sqls.get(i);
            log(sql);
            bindParameters(preparedStatement, sql);
            executeUpdate(preparedStatement, sql);
            query.generateId(preparedStatement, i);
        }
        return updatedRows;
    }

    protected int executeUpdate(PreparedStatement preparedStatement,
            PreparedSql sql) throws SQLException {
        try {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Dialect dialect = query.getConfig().dialect();
            if (dialect.isUniqueConstraintViolated(e)) {
                throw new UniqueConstraintException(sql.getRawSql(), "", e);
            }
            throw e;
        }
    }

}
