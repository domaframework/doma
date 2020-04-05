package org.seasar.doma.jdbc.command;

import java.sql.CallableStatement;
import java.sql.SQLException;
import org.seasar.doma.jdbc.query.FunctionQuery;

public class FunctionCommand<RESULT> extends ModuleCommand<FunctionQuery<RESULT>, RESULT> {

  public FunctionCommand(FunctionQuery<RESULT> query) {
    super(query);
  }

  @Override
  protected RESULT executeInternal(CallableStatement callableStatement) throws SQLException {
    callableStatement.execute();
    fetchParameters(callableStatement);
    return query.getResult();
  }
}
