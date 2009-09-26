package org.seasar.doma.internal.jdbc.command;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Fetcher<T> {

    void fetch(ResultSet resultSet, T target) throws SQLException;
}
