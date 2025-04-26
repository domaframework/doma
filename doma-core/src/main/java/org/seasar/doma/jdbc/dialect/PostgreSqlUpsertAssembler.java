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

import java.util.List;
import java.util.function.Consumer;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.DuplicateKeyType;
import org.seasar.doma.jdbc.query.InsertRow;
import org.seasar.doma.jdbc.query.QueryOperand;
import org.seasar.doma.jdbc.query.QueryOperandPair;
import org.seasar.doma.jdbc.query.ReturningProperties;
import org.seasar.doma.jdbc.query.UpsertAssembler;
import org.seasar.doma.jdbc.query.UpsertAssemblerContext;
import org.seasar.doma.jdbc.query.UpsertAssemblerSupport;

// TODO: Rename to PostgresUpsertAssembler
public class PostgreSqlUpsertAssembler implements UpsertAssembler {
  private final PreparedSqlBuilder buf;
  private final EntityType<?> entityType;
  private final DuplicateKeyType duplicateKeyType;
  private final UpsertAssemblerSupport upsertAssemblerSupport;

  private final boolean isKeysSpecified;
  private final List<? extends EntityPropertyType<?, ?>> keys;

  private final List<? extends EntityPropertyType<?, ?>> insertPropertyTypes;

  private final List<InsertRow> insertRows;
  private final List<QueryOperandPair> setValues;
  private final Naming naming;
  private final Dialect dialect;
  private final ReturningProperties returning;
  private final QueryOperand.Visitor queryOperandVisitor = new QueryOperandVisitor();

  public PostgreSqlUpsertAssembler(UpsertAssemblerContext context) {
    this.buf = context.buf;
    this.entityType = context.entityType;
    this.duplicateKeyType = context.duplicateKeyType;
    this.keys = context.keys;
    this.isKeysSpecified = context.isKeysSpecified;
    this.insertPropertyTypes = context.insertPropertyTypes;
    this.insertRows = context.insertRows;
    this.setValues = context.setValues;
    this.naming = context.naming;
    this.dialect = context.dialect;
    this.returning = context.returning;
    this.upsertAssemblerSupport = new UpsertAssemblerSupport(naming, dialect);
  }

  @Override
  public void assemble() {
    buf.appendSql("insert into ");
    tableNameAndAlias(entityType);
    join(insertPropertyTypes, ", ", " (", ")", buf, this::column);
    buf.appendSql(" values ");
    join(
        insertRows,
        ", ",
        "",
        "",
        buf,
        row -> join(row, ", ", "(", ")", buf, p -> p.accept(queryOperandVisitor)));
    if (duplicateKeyType == DuplicateKeyType.IGNORE) {
      buf.appendSql(" on conflict");
      if (isKeysSpecified) {
        join(keys, ", ", " (", ")", buf, this::column);
      }
      buf.appendSql(" do nothing");
    } else if (duplicateKeyType == DuplicateKeyType.UPDATE) {
      buf.appendSql(" on conflict");
      join(keys, ", ", " (", ")", buf, this::column);
      buf.appendSql(" do update set ");
      join(
          setValues,
          ", ",
          "",
          "",
          buf,
          p -> {
            column(p.getLeft().getEntityPropertyType());
            buf.appendSql(" = ");
            p.getRight().accept(queryOperandVisitor);
          });
    }
    if (!returning.isNone()) {
      StandardAssemblerUtil.assembleReturning(buf, entityType, naming, dialect, returning);
    }
  }

  private void tableNameAndAlias(EntityType<?> entityType) {
    String sql =
        this.upsertAssemblerSupport.targetTable(
            entityType, UpsertAssemblerSupport.TableNameType.NAME_AS_ALIAS);
    buf.appendSql(sql);
  }

  private void column(EntityPropertyType<?, ?> propertyType) {
    String sql = this.upsertAssemblerSupport.prop(propertyType);
    buf.appendSql(sql);
  }

  private <T> void join(
      Iterable<T> iterable,
      String delimiter,
      String start,
      String end,
      PreparedSqlBuilder buf,
      Consumer<T> consumer) {
    buf.appendSql(start);
    for (T val : iterable) {
      consumer.accept(val);
      buf.appendSql(delimiter);
    }
    buf.cutBackSql(delimiter.length());
    buf.appendSql(end);
  }

  class QueryOperandVisitor implements QueryOperand.Visitor {
    @Override
    public void visit(QueryOperand.Param param) {
      buf.appendParameter(param.inParameter);
    }

    @Override
    public void visit(QueryOperand.Prop prop) {
      String sql =
          upsertAssemblerSupport.excludeProp(
              prop.propertyType, UpsertAssemblerSupport.ColumnNameType.NAME_ALIAS);
      buf.appendSql(sql);
    }
  }
}
