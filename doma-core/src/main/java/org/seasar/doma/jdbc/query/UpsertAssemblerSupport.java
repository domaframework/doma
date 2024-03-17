package org.seasar.doma.jdbc.query;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;

public class UpsertAssemblerSupport {
  private final Naming naming;
  private final Dialect dialect;
  private final UpsertAliasConstants aliasConstants;

  public UpsertAssemblerSupport(Naming naming, Dialect dialect) {
    this(naming, dialect, new DefaultUpsertAliasConstants());
  }

  public UpsertAssemblerSupport(
      Naming naming, Dialect dialect, UpsertAliasConstants aliasConstants) {
    this.naming = Objects.requireNonNull(naming);
    this.dialect = Objects.requireNonNull(dialect);
    this.aliasConstants = Objects.requireNonNull(aliasConstants);
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

  public static class DefaultUpsertAliasConstants implements UpsertAliasConstants {
    @Override
    public String getTargetAlias() {
      return "target";
    }

    @Override
    public String getExcludedAlias() {
      return "excluded";
    }
  }

  public interface UpsertAliasConstants {
    String getTargetAlias();

    String getExcludedAlias();
  }

  public String targetTable(EntityType<?> entityType, TableNameType tableNameType) {
    switch (tableNameType) {
      case NAME:
        return entityType.getQualifiedTableName(naming::apply, dialect::applyQuote);
      case ALIAS:
        return aliasConstants.getTargetAlias();
      case AS_ALIAS:
        return " as " + aliasConstants.getTargetAlias();
      case NAME_ALIAS:
        return entityType.getQualifiedTableName(naming::apply, dialect::applyQuote)
            + " "
            + aliasConstants.getTargetAlias();
      case NAME_AS_ALIAS:
        return entityType.getQualifiedTableName(naming::apply, dialect::applyQuote)
            + " as "
            + aliasConstants.getTargetAlias();
      default:
        throw new IllegalArgumentException("Unknown table name type: " + tableNameType);
    }
  }

  public String targetTable(
      List<EntityPropertyType<?, ?>> entityPropertyTypes, TableNameType tableNameType) {
    switch (tableNameType) {
      case NAME:
      case NAME_ALIAS:
        throw new UnsupportedOperationException();
      case ALIAS:
        return aliasConstants.getTargetAlias();
      case AS_ALIAS:
        return " as " + aliasConstants.getTargetAlias();
      default:
        throw new IllegalArgumentException("Unknown table name type: " + tableNameType);
    }
  }

  public String targetProp(EntityPropertyType<?, ?> propertyType, ColumnNameType columnNameType) {
    switch (columnNameType) {
      case NAME:
        return propertyType.getColumnName(naming::apply, dialect::applyQuote);
      case NAME_ALIAS:
        return aliasConstants.getTargetAlias()
            + "."
            + propertyType.getColumnName(naming::apply, dialect::applyQuote);
      default:
        throw new IllegalArgumentException("Unknown table name type: " + columnNameType);
    }
  }

  public String excludeAlias() {
    return aliasConstants.getExcludedAlias();
  }

  public String excludeProp(EntityPropertyType<?, ?> propertyType, ColumnNameType columnNameType) {
    switch (columnNameType) {
      case NAME:
        return propertyType.getColumnName(naming::apply, dialect::applyQuote);
      case NAME_ALIAS:
        return aliasConstants.getExcludedAlias()
            + "."
            + propertyType.getColumnName(naming::apply, dialect::applyQuote);
      default:
        throw new IllegalArgumentException("Unknown table name type: " + columnNameType);
    }
  }

  public String prop(EntityPropertyType<?, ?> propertyType) {
    return propertyType.getColumnName(naming::apply, dialect::applyQuote);
  }

  public InParameter<?> param(
      EntityPropertyType<?, ?> propertyType, Map<EntityPropertyType<?, ?>, InParameter<?>> values) {
    return values.get(propertyType);
  }
}
