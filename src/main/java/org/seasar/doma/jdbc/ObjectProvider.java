package org.seasar.doma.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An object provider.
 * <p>
 * The object is mapped to a row of the result set.
 */
public interface ObjectProvider<OBJECT> {

    /**
     * Retrieve the object.
     * 
     * @param resultSet
     *            the result set
     * @return the object
     * @throws SQLException
     *             if the result set throws {@code SQLException}.
     */
    OBJECT get(ResultSet resultSet) throws SQLException;

}
