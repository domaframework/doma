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

import static org.seasar.doma.internal.Constants.*;
import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.MappedPropertyNotFoundException;
import org.seasar.doma.jdbc.ResultMappingException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.wrapper.Wrapper;

/**
 * エンティティのビルダーです。
 * 
 * @author taedium
 * @since 1.34.0
 * @param <E>
 *            エンティティの型
 */
public class EntityBuilder<E> {

    protected final Query query;

    protected final EntityType<E> entityType;

    protected final boolean resultMappingEnsured;

    protected Map<Integer, EntityPropertyType<E, ?>> indexMap;

    public EntityBuilder(Query query, EntityType<E> entityType,
            boolean resultMappingEnsured) {
        assertNotNull(query, entityType);
        this.query = query;
        this.entityType = entityType;
        this.resultMappingEnsured = resultMappingEnsured;
    }

    public E build(ResultSet resultSet) throws SQLException {
        assertNotNull(resultSet);
        if (indexMap == null) {
            indexMap = createIndexMap(resultSet.getMetaData(), entityType);
        }
        JdbcMappingVisitor jdbcMappingVisitor = query.getConfig().getDialect()
                .getJdbcMappingVisitor();
        if (entityType.isImmutable()) {
            Map<String, Object> properties = new HashMap<String, Object>(
                    indexMap.size());
            for (Map.Entry<Integer, EntityPropertyType<E, ?>> entry : indexMap
                    .entrySet()) {
                Integer index = entry.getKey();
                EntityPropertyType<E, ?> propertyType = entry.getValue();
                GetValueFunction function = new GetValueFunction(resultSet,
                        index);
                Wrapper<?> wrapper = propertyType.getWrapper(properties);
                wrapper.accept(jdbcMappingVisitor, function);
            }
            return entityType.newEntity(properties);
        }
        E entity = entityType
                .newEntity(Collections.<String, Object> emptyMap());
        for (Map.Entry<Integer, EntityPropertyType<E, ?>> entry : indexMap
                .entrySet()) {
            Integer index = entry.getKey();
            EntityPropertyType<E, ?> propertyType = entry.getValue();
            GetValueFunction function = new GetValueFunction(resultSet, index);
            Wrapper<?> wrapper = propertyType.getWrapper(entity);
            wrapper.accept(jdbcMappingVisitor, function);
        }
        entityType.saveCurrentStates(entity);
        return entity;
    }

    protected HashMap<Integer, EntityPropertyType<E, ?>> createIndexMap(
            ResultSetMetaData resultSetMeta, EntityType<E> entityType)
            throws SQLException {
        HashMap<Integer, EntityPropertyType<E, ?>> indexMap = new HashMap<Integer, EntityPropertyType<E, ?>>();
        HashMap<String, EntityPropertyType<E, ?>> columnNameMap = createColumnNameMap(entityType);
        Set<EntityPropertyType<E, ?>> unmappedPropertySet = resultMappingEnsured ? new HashSet<EntityPropertyType<E, ?>>(
                columnNameMap.values()) : Collections
                .<EntityPropertyType<E, ?>> emptySet();
        int count = resultSetMeta.getColumnCount();
        for (int i = 1; i < count + 1; i++) {
            String columnName = resultSetMeta.getColumnLabel(i);
            String lowerCaseColumnName = columnName.toLowerCase();
            EntityPropertyType<E, ?> propertyType = columnNameMap
                    .get(lowerCaseColumnName);
            if (propertyType == null) {
                if (ROWNUMBER_COLUMN_NAME.equals(lowerCaseColumnName)) {
                    continue;
                }
                if (!query.getConfig().ignoreUnknownColumn()) {
                    throwMappedPropertyNotFoundException(columnName);
                }
            } else {
                unmappedPropertySet.remove(propertyType);
                indexMap.put(i, propertyType);
            }
        }
        if (resultMappingEnsured && !unmappedPropertySet.isEmpty()) {
            throwResultMappingException(unmappedPropertySet);
        }
        return indexMap;
    }

    protected HashMap<String, EntityPropertyType<E, ?>> createColumnNameMap(
            EntityType<E> entityType) {
        List<EntityPropertyType<E, ?>> propertyTypes = entityType
                .getEntityPropertyTypes();
        HashMap<String, EntityPropertyType<E, ?>> result = new HashMap<String, EntityPropertyType<E, ?>>(
                propertyTypes.size());
        for (EntityPropertyType<E, ?> propertyType : propertyTypes) {
            String columnName = propertyType.getColumnName();
            result.put(columnName.toLowerCase(), propertyType);
        }
        return result;
    }

    protected void throwMappedPropertyNotFoundException(String columnName) {
        Sql<?> sql = query.getSql();
        NamingType namingType = entityType.getNamingType();
        throw new MappedPropertyNotFoundException(query.getConfig()
                .getExceptionSqlLogType(), columnName,
                namingType.revert(columnName), entityType.getEntityClass()
                        .getName(), sql.getKind(), sql.getRawSql(),
                sql.getFormattedSql(), sql.getSqlFilePath());
    }

    protected void throwResultMappingException(
            Set<EntityPropertyType<E, ?>> unmappedPropertySet) {
        int size = unmappedPropertySet.size();
        List<String> unmappedPropertyNames = new ArrayList<String>(size);
        List<String> expectedColumnNames = new ArrayList<String>(size);
        for (EntityPropertyType<E, ?> propertyType : unmappedPropertySet) {
            unmappedPropertyNames.add(propertyType.getName());
            expectedColumnNames.add(propertyType.getColumnName());
        }
        Sql<?> sql = query.getSql();
        throw new ResultMappingException(query.getConfig()
                .getExceptionSqlLogType(), entityType.getEntityClass()
                .getName(), unmappedPropertyNames, expectedColumnNames,
                sql.getKind(), sql.getRawSql(), sql.getFormattedSql(),
                sql.getSqlFilePath());
    }

}
