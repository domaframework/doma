package org.seasar.doma.internal.jdbc.util;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.NamingConvention;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityProperty;

/**
 * 
 * @author taedium
 * 
 */
public final class ColumnUtil {

    public static String getColumnName(Config config, EntityProperty<?> property) {
        assertNotNull(config, property);
        if (property.getColumnName() != null) {
            return property.getColumnName();
        }
        Dialect dialect = config.dialect();
        NamingConvention namingConvention = config.namingConvention();
        return namingConvention.fromPropertyToColumn(property.getName(), dialect);
    }
}
