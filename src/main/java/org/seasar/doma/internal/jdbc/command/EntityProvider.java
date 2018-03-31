package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.Constants.ROWNUMBER_COLUMN_NAME;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import org.seasar.doma.jdbc.*;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.query.Query;

public class EntityProvider<ENTITY> extends AbstractObjectProvider<ENTITY> {

  protected final EntityDesc<ENTITY> entityDesc;

  protected final Query query;

  protected final boolean resultMappingEnsured;

  protected final JdbcMappingVisitor jdbcMappingVisitor;

  protected final UnknownColumnHandler unknownColumnHandler;

  protected Map<Integer, EntityPropertyDesc<ENTITY, ?>> indexMap;

  public EntityProvider(EntityDesc<ENTITY> entityDesc, Query query, boolean resultMappingEnsured) {
    assertNotNull(entityDesc, query);
    this.entityDesc = entityDesc;
    this.query = query;
    this.resultMappingEnsured = resultMappingEnsured;
    this.jdbcMappingVisitor = query.getConfig().getDialect().getJdbcMappingVisitor();
    this.unknownColumnHandler = query.getConfig().getUnknownColumnHandler();
  }

  @Override
  public ENTITY get(ResultSet resultSet) throws SQLException {
    return build(resultSet);
  }

  protected ENTITY build(ResultSet resultSet) throws SQLException {
    assertNotNull(resultSet);
    if (indexMap == null) {
      indexMap = createIndexMap(resultSet.getMetaData(), entityDesc);
    }
    Map<String, Property<ENTITY, ?>> states = new HashMap<>(indexMap.size());
    for (Map.Entry<Integer, EntityPropertyDesc<ENTITY, ?>> entry : indexMap.entrySet()) {
      Integer index = entry.getKey();
      EntityPropertyDesc<ENTITY, ?> propertyDesc = entry.getValue();
      Property<ENTITY, ?> property = propertyDesc.createProperty();
      fetch(resultSet, property, index, jdbcMappingVisitor);
      states.put(propertyDesc.getName(), property);
    }
    ENTITY entity = entityDesc.newEntity(states);
    if (!entityDesc.isImmutable()) {
      entityDesc.saveCurrentStates(entity);
    }
    return entity;
  }

  protected HashMap<Integer, EntityPropertyDesc<ENTITY, ?>> createIndexMap(
      ResultSetMetaData resultSetMeta, EntityDesc<ENTITY> entityDesc) throws SQLException {
    HashMap<Integer, EntityPropertyDesc<ENTITY, ?>> indexMap = new HashMap<>();
    HashMap<String, EntityPropertyDesc<ENTITY, ?>> columnNameMap = createColumnNameMap(entityDesc);
    Set<EntityPropertyDesc<ENTITY, ?>> unmappedPropertySet =
        resultMappingEnsured ? new HashSet<>(columnNameMap.values()) : Collections.emptySet();
    int count = resultSetMeta.getColumnCount();
    for (int i = 1; i < count + 1; i++) {
      String columnName = resultSetMeta.getColumnLabel(i);
      String lowerCaseColumnName = columnName.toLowerCase();
      EntityPropertyDesc<ENTITY, ?> propertyDesc = columnNameMap.get(lowerCaseColumnName);
      if (propertyDesc == null) {
        if (ROWNUMBER_COLUMN_NAME.equals(lowerCaseColumnName)) {
          continue;
        }
        unknownColumnHandler.handle(query, entityDesc, lowerCaseColumnName);
      } else {
        unmappedPropertySet.remove(propertyDesc);
        indexMap.put(i, propertyDesc);
      }
    }
    if (resultMappingEnsured && !unmappedPropertySet.isEmpty()) {
      throwResultMappingException(unmappedPropertySet);
    }
    return indexMap;
  }

  protected HashMap<String, EntityPropertyDesc<ENTITY, ?>> createColumnNameMap(
      EntityDesc<ENTITY> entityDesc) {
    Naming naming = query.getConfig().getNaming();
    List<EntityPropertyDesc<ENTITY, ?>> propertyDescs = entityDesc.getEntityPropertyDescs();
    HashMap<String, EntityPropertyDesc<ENTITY, ?>> result = new HashMap<>(propertyDescs.size());
    for (EntityPropertyDesc<ENTITY, ?> propertyDesc : propertyDescs) {
      String columnName = propertyDesc.getColumnName(naming::apply);
      result.put(columnName.toLowerCase(), propertyDesc);
    }
    return result;
  }

  protected void throwResultMappingException(
      Set<EntityPropertyDesc<ENTITY, ?>> unmappedPropertySet) {
    Naming naming = query.getConfig().getNaming();
    int size = unmappedPropertySet.size();
    List<String> unmappedPropertyNames = new ArrayList<>(size);
    List<String> expectedColumnNames = new ArrayList<>(size);
    for (EntityPropertyDesc<ENTITY, ?> propertyDesc : unmappedPropertySet) {
      unmappedPropertyNames.add(propertyDesc.getName());
      expectedColumnNames.add(propertyDesc.getColumnName(naming::apply));
    }
    Sql<?> sql = query.getSql();
    throw new ResultMappingException(
        query.getConfig().getExceptionSqlLogType(),
        entityDesc.getEntityClass().getName(),
        unmappedPropertyNames,
        expectedColumnNames,
        sql.getKind(),
        sql.getRawSql(),
        sql.getFormattedSql(),
        sql.getSqlFilePath());
  }
}
