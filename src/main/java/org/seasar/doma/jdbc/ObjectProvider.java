package org.seasar.doma.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/** @author nakamura-to */
public interface ObjectProvider<OBJECT> {

  OBJECT get(ResultSet resultSet) throws SQLException;
}
