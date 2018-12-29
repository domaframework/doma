package org.seasar.doma.jdbc.entity;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.util.DatabaseObjectUtil;

public abstract class AbstractEntityType<ENTITY> implements EntityType<ENTITY> {

  protected AbstractEntityType() {}

  @Override
  public String getQualifiedTableName() {
    return getQualifiedTableName(Function.<String>identity());
  }

  @Override
  public String getQualifiedTableName(Function<String, String> quoteFunction) {
    return getQualifiedTableName((namingType, text) -> namingType.apply(text), quoteFunction);
  }

  @Override
  public String getQualifiedTableName(
      BiFunction<NamingType, String, String> namingFunction,
      Function<String, String> quoteFunction) {
    String catalogName = getCatalogName();
    String schemaName = getSchemaName();
    String tableName = getTableName(namingFunction);
    Function<String, String> mapper = isQuoteRequired() ? quoteFunction : Function.identity();
    return DatabaseObjectUtil.getQualifiedName(mapper, catalogName, schemaName, tableName);
  }
}
