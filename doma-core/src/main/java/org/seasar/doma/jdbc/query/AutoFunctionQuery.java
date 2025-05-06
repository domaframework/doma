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
import org.seasar.doma.jdbc.ResultParameter;
import org.seasar.doma.jdbc.SqlKind;

/**
 * An auto function query that calls a database function.
 *
 * <p>This class implements {@link FunctionQuery} to provide functionality for calling database
 * functions. It handles parameter binding and result retrieval.
 *
 * @param <RESULT> the type of the function result
 */
public class AutoFunctionQuery<RESULT> extends AutoModuleQuery implements FunctionQuery<RESULT> {

  /** The parameter that will hold the function result. */
  protected ResultParameter<RESULT> resultParameter;

  /** {@inheritDoc} */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(moduleName, resultParameter);
    prepareQualifiedName();
    prepareOptions();
    prepareSql();
    assertNotNull(sql);
  }

  /**
   * Prepares the SQL for this function query.
   *
   * <p>This method builds the callable SQL statement for the function call.
   */
  protected void prepareSql() {
    CallableSqlBuilder builder =
        new CallableSqlBuilder(
            config, SqlKind.FUNCTION, qualifiedName, parameters, sqlLogType, resultParameter);
    sql = builder.build(this::comment);
  }

  /**
   * Sets the function name.
   *
   * @param functionName the function name
   */
  public void setFunctionName(String functionName) {
    setModuleName(functionName);
  }

  /**
   * Sets the result parameter.
   *
   * @param parameter the result parameter
   */
  public void setResultParameter(ResultParameter<RESULT> parameter) {
    this.resultParameter = parameter;
  }

  /** {@inheritDoc} */
  @Override
  public RESULT getResult() {
    return resultParameter.getResult();
  }
}
