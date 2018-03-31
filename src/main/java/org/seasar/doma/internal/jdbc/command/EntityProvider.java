package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.Constants.ROWNUMBER_COLUMN_NAME;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

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
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.ResultMappingException;
import org.seasar.doma.jdbc.UnknownColumnHandler;
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
    for (var entry : indexMap.entrySet()) {
      var index = entry.getKey();
      var propertyDesc = entry.getValue();
      var property = propertyDesc.createProperty();
      fetch(resultSet, property, index, jdbcMappingVisitor);
      states.put(propertyDesc.getName(), property);
    }
    var entity = entityDesc.newEntity(states);
    if (!entityDesc.isImmutable()) {
      entityDesc.saveCurrentStates(entity);
    }
    return entity;
  }

  protected HashMap<Integer, EntityPropertyDesc<ENTITY, ?>> createIndexMap(
      ResultSetMetaData resultSetMeta, EntityDesc<ENTITY> entityDesc) throws SQLException {
    HashMap<Integer, EntityPropertyDesc<ENTITY, ?>> indexMap = new HashMap<>();
    var columnNameMap = createColumnNameMap(entityDesc);
    Set<EntityPropertyDesc<ENTITY, ?>> unmappedPropertySet =
        resultMappingEnsured ? new HashSet<>(columnNameMap.values()) : Collections.emptySet();
    var count = resultSetMeta.getColumnCount();
    for (var i = 1; i < count + 1; i++) {
      var columnName = resultSetMeta.getColumnLabel(i);
      var lowerCaseColumnName = columnName.toLowerCase();
      var propertyDesc = columnNameMap.get(lowerCaseColumnName);
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
    var naming = query.getConfig().getNaming();
    var propertyDescs = entityDesc.getEntityPropertyDescs();
    HashMap<String, EntityPropertyDesc<ENTITY, ?>> result = new HashMap<>(propertyDescs.size());
    for (var propertyDesc : propertyDescs) {
      var columnName = propertyDesc.getColumnName(naming::apply);
      result.put(columnName.toLowerCase(), propertyDesc);
    }
    return result;
  }

  protected void throwResultMappingException(
      Set<EntityPropertyDesc<ENTITY, ?>> unmappedPropertySet) {
    var naming = query.getConfig().getNaming();
    var size = unmappedPropertySet.size();
    List<String> unmappedPropertyNames = new ArrayList<>(size);
    List<String> expectedColumnNames = new ArrayList<>(size);
    for (var propertyDesc : unmappedPropertySet) {
      unmappedPropertyNames.add(propertyDesc.getName());
      expectedColumnNames.add(propertyDesc.getColumnName(naming::apply));
    }
    var sql = query.getSql();
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
