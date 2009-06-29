package org.seasar.doma.internal.jdbc.command;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.query.FunctionQuery;


/**
 * @author taedium
 * 
 */
public class FunctionCommand<R> extends ModuleCommand<R, FunctionQuery<R>> {

    public FunctionCommand(FunctionQuery<R> query) {
        super(query);
    }

    @Override
    protected R executeInternal(CallableStatement callableStatement)
            throws SQLException {
        callableStatement.execute();
        fetchParameters(callableStatement);
        return query.getResult();
    }

}
