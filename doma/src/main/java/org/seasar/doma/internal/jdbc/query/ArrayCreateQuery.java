package org.seasar.doma.internal.jdbc.query;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;

import org.seasar.doma.domain.Domain;

/**
 * @author taedium
 * 
 */
public class ArrayCreateQuery<R extends Domain<Array, ?>> extends
        AbstractCreateQuery<Array, R> {

    protected String typeName;

    protected Object[] elements;

    public void compile() {
        assertNotNull(config, callerClassName, callerMethodName, result, typeName, elements);
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Object[] getElements() {
        return elements;
    }

    public void setElements(Object[] elements) {
        this.elements = elements;
    }

    @Override
    public R create(Connection connection) throws SQLException {
        Array array = connection.createArrayOf(typeName, elements);
        result.set(array);
        return result;
    }
}
