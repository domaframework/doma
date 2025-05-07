/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.query;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.util.DatabaseObjectUtil;
import org.seasar.doma.jdbc.CallableSql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlParameter;

/**
 * An abstract base class for queries that call database modules.
 *
 * <p>This class provides common functionality for queries that call database stored procedures and
 * functions. It handles parameter binding, module name qualification, and SQL generation.
 */
public abstract class AutoModuleQuery extends AbstractQuery implements ModuleQuery {

  /** The callable SQL for this module query. */
  protected CallableSql sql;

  /** The catalog name of the module. */
  protected String catalogName;

  /** The schema name of the module. */
  protected String schemaName;

  /** The name of the module. */
  protected String moduleName;

  /** The qualified name of the module. */
  protected String qualifiedName;

  /** Whether quotes are required for identifiers. */
  protected boolean isQuoteRequired;

  /** The parameters for the module call. */
  protected final List<SqlParameter> parameters = new ArrayList<>();

  /** The SQL log type for this module query. */
  protected SqlLogType sqlLogType;

  /**
   * Prepares the qualified name of the module.
   *
   * <p>This method builds the fully qualified name of the module using catalog, schema, and module
   * names, applying quotes if required.
   */
  protected void prepareQualifiedName() {
    Function<String, String> mapper =
        isQuoteRequired ? config.getDialect()::applyQuote : Function.identity();
    qualifiedName =
        DatabaseObjectUtil.getQualifiedName(mapper, catalogName, schemaName, moduleName);
  }

  /**
   * Prepares the options for this module query.
   *
   * <p>This method sets default values for query timeout if not already set.
   */
  protected void prepareOptions() {
    if (queryTimeout <= 0) {
      queryTimeout = config.getQueryTimeout();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void complete() {}

  /**
   * Sets the catalog name of the module.
   *
   * @param catalogName the catalog name
   */
  public void setCatalogName(String catalogName) {
    this.catalogName = catalogName;
  }

  /**
   * Sets the schema name of the module.
   *
   * @param schemaName the schema name
   */
  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }

  /**
   * Sets the name of the module.
   *
   * @param moduleName the module name
   */
  protected void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  /**
   * Sets whether quotes are required for identifiers.
   *
   * @param isQuoteRequired {@code true} if quotes are required
   */
  public void setQuoteRequired(boolean isQuoteRequired) {
    this.isQuoteRequired = isQuoteRequired;
  }

  /**
   * Sets the SQL log type for this module query.
   *
   * @param sqlLogType the SQL log type
   */
  public void setSqlLogType(SqlLogType sqlLogType) {
    this.sqlLogType = sqlLogType;
  }

  /**
   * Adds a parameter to this module query.
   *
   * @param parameter the parameter to add
   */
  public void addParameter(SqlParameter parameter) {
    parameters.add(parameter);
  }

  /** {@inheritDoc} */
  @Override
  public String getQualifiedName() {
    return qualifiedName;
  }

  /** {@inheritDoc} */
  @Override
  public CallableSql getSql() {
    return sql;
  }

  /** {@inheritDoc} */
  @Override
  public SqlLogType getSqlLogType() {
    return sqlLogType;
  }
}
