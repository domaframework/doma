package org.seasar.doma.jdbc.entity;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.util.DatabaseObjectUtil;

public abstract class AbstractEntityDesc<ENTITY> implements EntityDesc<ENTITY> {

  protected AbstractEntityDesc() {}

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
