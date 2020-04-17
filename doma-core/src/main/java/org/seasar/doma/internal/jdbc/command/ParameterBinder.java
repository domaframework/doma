package org.seasar.doma.internal.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.seasar.doma.jdbc.SqlParameter;

public interface ParameterBinder<S extends PreparedStatement, P extends SqlParameter> {

  void bind(S statement, List<? extends P> parameters) throws SQLException;
}
