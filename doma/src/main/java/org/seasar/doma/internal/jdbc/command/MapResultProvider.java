/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.query.Query;

/***
 * 
 * @author nakamura-to
 * 
 * @param <MAP>
 * @param <CONTAINER>
 */
public class MapResultProvider<CONTAINER> extends
        AbstractResultProvider<CONTAINER> {

    protected final Query query;

    protected final MapKeyNamingType keyNamingType;

    protected final Function<Map<String, Object>, CONTAINER> mapper;

    protected final JdbcMappingVisitor jdbcMappingVisitor;

    protected Map<Integer, String> indexMap;

    /**
     * 
     * @param query
     * @param keyNamingType
     * @param mapper
     */
    public MapResultProvider(Query query, MapKeyNamingType keyNamingType,
            Function<Map<String, Object>, CONTAINER> mapper) {
        assertNotNull(query, keyNamingType);
        this.query = query;
        this.keyNamingType = keyNamingType;
        this.mapper = mapper;
        this.jdbcMappingVisitor = query.getConfig().getDialect()
                .getJdbcMappingVisitor();
    }

    @Override
    public CONTAINER get(ResultSet resultSet) throws SQLException {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        if (indexMap == null) {
            indexMap = createIndexMap(resultSet.getMetaData());
        }
        for (Map.Entry<Integer, String> entry : indexMap.entrySet()) {
            Integer index = entry.getKey();
            String key = entry.getValue();
            BasicScalar<Object> scalar = new BasicScalar<>(
                    () -> new org.seasar.doma.wrapper.ObjectWrapper(), false);
            fetch(resultSet, scalar, index, jdbcMappingVisitor);
            map.put(key, scalar.get());
        }
        return mapper.apply(map);
    }

    @Override
    public CONTAINER getDefault() {
        return mapper.apply(null);
    }

    protected HashMap<Integer, String> createIndexMap(
            ResultSetMetaData resultSetMeta) throws SQLException {
        HashMap<Integer, String> indexMap = new HashMap<Integer, String>();
        int count = resultSetMeta.getColumnCount();
        for (int i = 1; i < count + 1; i++) {
            String columnName = resultSetMeta.getColumnLabel(i);
            indexMap.put(i, keyNamingType.apply(columnName));
        }
        return indexMap;
    }

}
