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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.jdbc.sql.CallableSqlBuilder;
import org.seasar.doma.jdbc.SqlKind;

/**
 * An auto procedure query that calls a database stored procedure.
 *
 * <p>This class implements {@link ProcedureQuery} to provide functionality for calling database
 * stored procedures. It handles parameter binding and SQL generation.
 */
public class AutoProcedureQuery extends AutoModuleQuery implements ProcedureQuery {

  /** {@inheritDoc} */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(moduleName);
    prepareQualifiedName();
    prepareOptions();
    prepareSql();
    assertNotNull(sql);
  }

  /**
   * Prepares the SQL for this procedure query.
   *
   * <p>This method builds the callable SQL statement for the procedure call.
   */
  protected void prepareSql() {
    CallableSqlBuilder builder =
        new CallableSqlBuilder(config, SqlKind.PROCEDURE, qualifiedName, parameters, sqlLogType);
    sql = builder.build(this::comment);
  }

  /**
   * Sets the procedure name.
   *
   * @param procedureName the procedure name
   */
  public void setProcedureName(String procedureName) {
    setModuleName(procedureName);
  }
}
