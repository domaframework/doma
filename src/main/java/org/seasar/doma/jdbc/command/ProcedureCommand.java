package org.seasar.doma.jdbc.command;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.seasar.doma.Procedure;
import org.seasar.doma.jdbc.query.ProcedureQuery;

/**
 * A command for a stored PROCEDURE.
 * 
 * @see Procedure
 */
public class ProcedureCommand extends ModuleCommand<ProcedureQuery, Void> {

    public ProcedureCommand(ProcedureQuery query) {
        super(query);
    }

    @Override
    protected Void executeInternal(CallableStatement callableStatement) throws SQLException {
        callableStatement.execute();
        fetchParameters(callableStatement);
        return null;
    }

}
