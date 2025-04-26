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
package org.seasar.doma.jdbc.dialect;

import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.query.InsertAssembler;
import org.seasar.doma.jdbc.query.InsertAssemblerContext;
import org.seasar.doma.jdbc.query.MultiInsertAssembler;
import org.seasar.doma.jdbc.query.MultiInsertAssemblerContext;
import org.seasar.doma.jdbc.query.UpdateAssembler;
import org.seasar.doma.jdbc.query.UpdateAssemblerContext;
import org.seasar.doma.jdbc.query.UpsertAssembler;
import org.seasar.doma.jdbc.query.UpsertAssemblerContext;

/** A dialect for H2 version 1.4.200 and above. */
public class H2Dialect extends H214199Dialect {

  public H2Dialect() {
    this(new H2JdbcMappingVisitor(), new H2SqlLogFormattingVisitor(), new H2ExpressionFunctions());
  }

  public H2Dialect(JdbcMappingVisitor jdbcMappingVisitor) {
    this(jdbcMappingVisitor, new H2SqlLogFormattingVisitor(), new H2ExpressionFunctions());
  }

  public H2Dialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(new H2JdbcMappingVisitor(), sqlLogFormattingVisitor, new H2ExpressionFunctions());
  }

  public H2Dialect(ExpressionFunctions expressionFunctions) {
    this(new H2JdbcMappingVisitor(), new H2SqlLogFormattingVisitor(), expressionFunctions);
  }

  public H2Dialect(
      JdbcMappingVisitor jdbcMappingVisitor, SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(jdbcMappingVisitor, sqlLogFormattingVisitor, new H2ExpressionFunctions());
  }

  public H2Dialect(
      JdbcMappingVisitor jdbcMappingVisitor,
      SqlLogFormattingVisitor sqlLogFormattingVisitor,
      ExpressionFunctions expressionFunctions) {
    super(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions);
  }

  @Override
  public boolean includesIdentityColumn() {
    return false;
  }

  @Override
  public boolean supportsUpsertEmulationWithMergeStatement() {
    return true;
  }

  @Override
  public boolean supportsBatchExecutionReturningGeneratedValues() {
    return true;
  }

  public static class H2JdbcMappingVisitor extends H214199JdbcMappingVisitor {}

  public static class H2SqlLogFormattingVisitor extends H214199SqlLogFormattingVisitor {}

  public static class H2ExpressionFunctions extends H214199ExpressionFunctions {

    public H2ExpressionFunctions() {
      super();
    }

    public H2ExpressionFunctions(char[] wildcards) {
      super(wildcards);
    }

    protected H2ExpressionFunctions(char escapeChar, char[] wildcards) {
      super(escapeChar, wildcards);
    }
  }

  @Override
  public UpsertAssembler getUpsertAssembler(UpsertAssemblerContext context) {
    return new H2UpsertAssembler(context);
  }

  @Override
  public <ENTITY> InsertAssembler getInsertAssembler(InsertAssemblerContext<ENTITY> context) {
    return new H2InsertAssembler<>(context);
  }

  @Override
  public <ENTITY> MultiInsertAssembler getMultiInsertAssembler(
      MultiInsertAssemblerContext<ENTITY> context) {
    return new H2MultiInsertAssembler<>(context);
  }

  @Override
  public <ENTITY> UpdateAssembler getUpdateAssembler(UpdateAssemblerContext<ENTITY> context) {
    return new H2UpdateAssembler<>(context);
  }
}
