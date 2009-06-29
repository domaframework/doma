package org.seasar.doma.internal.jdbc.mock;

import java.sql.SQLException;
import java.sql.Wrapper;

import org.seasar.doma.internal.util.Assertions;


/**
 * 
 * @author taedium
 * 
 */
public class MockWrapper implements Wrapper {

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        Assertions.notYetImplemented();
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

}
