package org.seasar.doma.internal.jdbc.command;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.query.Query;


/**
 * @author taedium
 * 
 */
public interface ResultSetHandler<R> {

    R handle(ResultSet resultSet, Query query) throws SQLException;
}
