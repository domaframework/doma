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

import org.seasar.doma.internal.jdbc.entity.EntityPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.jdbc.util.ColumnUtil;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class EntityFetcher implements ResultFetcher<ResultSet, EntityType<?>> {

    protected final Query query;

    protected Map<String, String> nameMap;

    public EntityFetcher(Query query) throws SQLException {
        assertNotNull(query);
        this.query = query;
    }

    @Override
    public void fetch(ResultSet resultSet, EntityType<?> entityType)
            throws SQLException {
        assertNotNull(resultSet, entityType);
        if (nameMap == null) {
            createNameMap(entityType);
        }
        ResultSetMetaData resultSetMeta = resultSet.getMetaData();
        int count = resultSetMeta.getColumnCount();
        JdbcMappingVisitor jdbcMappingVisitor = query.getConfig().dialect()
                .getJdbcMappingVisitor();
        for (int i = 1; i < count + 1; i++) {
            String columnName = resultSetMeta.getColumnLabel(i);
            String propertyName = nameMap.get(columnName.toLowerCase());
            EntityPropertyType<?> propertyType = entityType
                    .getEntityPropertyType(propertyName);
            if (propertyType != null) {
                Wrapper<?> wrapper = propertyType.getWrapper();
                GetValueFunction function = new GetValueFunction(resultSet, i);
                wrapper.accept(jdbcMappingVisitor, function);
            }
        }
    }

    protected void createNameMap(EntityType<?> entityType) {
        List<EntityPropertyType<?>> propertyTypes = entityType
                .getEntityPropertyTypes();
        nameMap = new HashMap<String, String>(propertyTypes.size());
        for (EntityPropertyType<?> p : propertyTypes) {
            String columnName = ColumnUtil.getColumnName(entityType, p);
            nameMap.put(columnName.toLowerCase(), p.getName());
        }
    }
}
