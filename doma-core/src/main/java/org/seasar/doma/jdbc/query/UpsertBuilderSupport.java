package org.seasar.doma.jdbc.query;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;

public class UpsertBuilderSupport {
  private final Naming naming;
  private final Dialect dialect;
  private final UpsertAliasManager aliasManager;

  public UpsertBuilderSupport(Naming naming, Dialect dialect, UpsertAliasManager aliasManager) {
    this.naming = Objects.requireNonNull(naming);
    this.dialect = Objects.requireNonNull(dialect);
    this.aliasManager = aliasManager;
  }

  public enum TableNameType {
    NAME,
    ALIAS,
    AS_ALIAS,
    NAME_ALIAS,
    NAME_AS_ALIAS,
  }

  public enum ColumnNameType {
    NAME,
    NAME_ALIAS,
  }

  public static class DefaultUpsertAliasManager implements UpsertAliasManager {
    @Override
    public String getTargetAlias() {
      return "target";
    }

    @Override
    public String getExcludedAlias() {
      return "excluded";
    }
  }

  public interface UpsertAliasManager {
    String getTargetAlias();

    String getExcludedAlias();
  }

  public String table(EntityType<?> entityType, TableNameType tableNameType) {
    switch (tableNameType) {
      case NAME:
        return entityType.getQualifiedTableName(naming::apply, dialect::applyQuote);
      case ALIAS:
        return aliasManager.getTargetAlias();
      case AS_ALIAS:
        return " as " + aliasManager.getTargetAlias();
      case NAME_ALIAS:
        return entityType.getQualifiedTableName(naming::apply, dialect::applyQuote)
            + " "
            + aliasManager.getTargetAlias();
      case NAME_AS_ALIAS:
        return entityType.getQualifiedTableName(naming::apply, dialect::applyQuote)
            + " as "
            + aliasManager.getTargetAlias();
      default:
        throw new IllegalArgumentException("Unknown table name type: " + tableNameType);
    }
  }

  public String table(
      List<EntityPropertyType<?, ?>> entityPropertyTypes, TableNameType tableNameType) {
    switch (tableNameType) {
      case NAME:
        throw new UnsupportedOperationException();
      case ALIAS:
        return aliasManager.getTargetAlias();
      case AS_ALIAS:
        return " as " + aliasManager.getTargetAlias();
      case NAME_ALIAS:
        throw new UnsupportedOperationException();
      default:
        throw new IllegalArgumentException("Unknown table name type: " + tableNameType);
    }
  }

  public String column(EntityPropertyType<?, ?> propertyType) {
    return propertyType.getColumnName(naming::apply, dialect::applyQuote);
  }

  public String updateParam(EntityPropertyType<?, ?> propertyType, ColumnNameType columnNameType) {
    switch (columnNameType) {
      case NAME:
        return propertyType.getColumnName(naming::apply, dialect::applyQuote);
      case NAME_ALIAS:
        return aliasManager.getExcludedAlias()
            + "."
            + propertyType.getColumnName(naming::apply, dialect::applyQuote);
      default:
        throw new IllegalArgumentException("Unknown table name type: " + columnNameType);
    }
  }

  public String updateParam(EntityPropertyType<?, ?> propertyType) {
    return aliasManager.getExcludedAlias()
        + "."
        + propertyType.getColumnName(naming::apply, dialect::applyQuote);
  }

  public InParameter<?> param(
      EntityPropertyType<?, ?> propertyType, Map<EntityPropertyType<?, ?>, InParameter<?>> values) {
    return values.get(propertyType);
  }
}
