/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.jdbc.util.ColumnUtil;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.entity.EntityMeta;
import org.seasar.doma.jdbc.entity.EntityPropertyMeta;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class EntityFetcher<E> {

    protected final Query query;

    protected Map<String, String> nameMap;

    public EntityFetcher(Query query) throws SQLException {
        assertNotNull(query);
        this.query = query;
    }

    public void fetch(ResultSet resultSet, EntityMeta<E> entityMeta)
            throws SQLException {
        assertNotNull(resultSet, entityMeta);
        if (nameMap == null) {
            createNameMap(entityMeta);
        }
        ResultSetMetaData resultSetMeta = resultSet.getMetaData();
        int count = resultSetMeta.getColumnCount();
        JdbcMappingVisitor jdbcMappingVisitor = query.getConfig().dialect()
                .getJdbcMappingVisitor();
        for (int i = 1; i < count + 1; i++) {
            String columnName = resultSetMeta.getColumnLabel(i);
            String propertyName = nameMap.get(columnName.toLowerCase());
            EntityPropertyMeta<?> property = entityMeta
                    .getPropertyMeta(propertyName);
            if (property != null) {
                Wrapper<?> wrapper = property.getWrapper();
                GetValueFunction function = new GetValueFunction(resultSet, i);
                wrapper.accept(jdbcMappingVisitor, function);
            }
        }
    }

    protected void createNameMap(EntityMeta<?> entity) {
        List<EntityPropertyMeta<?>> properties = entity.getPropertyMetas();
        nameMap = new HashMap<String, String>(properties.size());
        for (EntityPropertyMeta<?> property : properties) {
            String columnName = ColumnUtil.getColumnName(query.getConfig(),
                    property);
            nameMap.put(columnName.toLowerCase(), property.getName());
        }
    }
}
