package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.jdbc.Entity;
import org.seasar.doma.internal.jdbc.Property;
import org.seasar.doma.internal.jdbc.query.Query;


/**
 * @author taedium
 * 
 */
public class EntityFetcher {

    protected final Query query;

    protected Map<String, String> nameMap;

    public EntityFetcher(Query query) throws SQLException {
        assertNotNull(query);
        this.query = query;
    }

    public void fetch(ResultSet resultSet, Entity<?> entity)
            throws SQLException {
        if (nameMap == null) {
            createNameMap(entity);
        }
        ResultSetMetaData resultSetMeta = resultSet.getMetaData();
        int count = resultSetMeta.getColumnCount();
        for (int i = 1; i < count + 1; i++) {
            String columnName = resultSetMeta.getColumnLabel(i);
            String propertyName = nameMap.get(columnName.toLowerCase());
            Property<?> property = entity.__getPropertyByName(propertyName);
            if (property != null) {
                Domain<?, ?> domain = property.getDomain();
                GetValueFunction function = new GetValueFunction(resultSet, i);
                domain.accept(query.getConfig().jdbcMappingVisitor(), function);
            }
        }
    }

    protected void createNameMap(Entity<?> entity) {
        List<Property<?>> properties = entity.__getProperties();
        nameMap = new HashMap<String, String>(properties.size());
        for (Property<?> property : properties) {
            String columnName = property.getColumnName(query.getConfig());
            nameMap.put(columnName.toLowerCase(), property.getName());
        }
    }
}
