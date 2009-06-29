package org.seasar.doma.internal.jdbc.command;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.query.ProcedureQuery;


/**
 * @author taedium
 * 
 */
public class ProcedureCommand extends ModuleCommand<Void, ProcedureQuery> {

    public ProcedureCommand(ProcedureQuery query) {
        super(query);
    }

    @Override
    protected Void executeInternal(CallableStatement callableStatement)
            throws SQLException {
        callableStatement.execute();
        fetchParameters(callableStatement);
        return null;
    }

}
