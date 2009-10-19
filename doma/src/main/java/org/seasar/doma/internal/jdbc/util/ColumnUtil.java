package org.seasar.doma.internal.jdbc.util;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.jdbc.entity.EntityPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.NamingConvention;

/**
 * 
 * @author taedium
 * 
 */
public final class ColumnUtil {

    public static String getColumnName(EntityType<?> entityType,
            EntityPropertyType<?> propertyType) {
        assertNotNull(entityType, propertyType);
        if (propertyType.getColumnName() != null) {
            return propertyType.getColumnName();
        }
        NamingConvention namingConvention = entityType.getNamingConvention();
        return namingConvention.fromPropertyToColumn(propertyType.getName());
    }
}
