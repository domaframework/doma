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
import java.util.Objects;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.query.InsertAssembler;
import org.seasar.doma.jdbc.query.InsertAssemblerContext;

public class DefaultInsertAssembler<ENTITY> implements InsertAssembler {

  private final PreparedSqlBuilder buf;
  private final EntityType<ENTITY> entityType;
  private final Naming naming;
  private final Dialect dialect;
  private final List<EntityPropertyType<ENTITY, ?>> insertPropertyTypes;
  private final ENTITY entity;

  public DefaultInsertAssembler(InsertAssemblerContext<ENTITY> context) {
    Objects.requireNonNull(context);
    this.buf = context.buf;
    this.entityType = context.entityType;
    this.naming = context.naming;
    this.dialect = context.dialect;
    this.insertPropertyTypes = context.insertPropertyTypes;
    this.entity = context.entity;
  }

  @Override
  public void assemble() {
    assembleInsertInto();
    buf.appendSql(" ");
    assembleValues();
  }

  void assembleInsertInto() {
    buf.appendSql("insert into ");
    buf.appendSql(entityType.getQualifiedTableName(naming::apply, dialect::applyQuote));
    buf.appendSql(" (");
    if (!insertPropertyTypes.isEmpty()) {
      for (EntityPropertyType<ENTITY, ?> propertyType : insertPropertyTypes) {
        buf.appendSql(propertyType.getColumnName(naming::apply, dialect::applyQuote));
        buf.appendSql(", ");
      }
      buf.cutBackSql(2);
    }
    buf.appendSql(")");
  }

  void assembleValues() {
    buf.appendSql("values (");
    if (!insertPropertyTypes.isEmpty()) {
      for (EntityPropertyType<ENTITY, ?> propertyType : insertPropertyTypes) {
        Property<ENTITY, ?> property = propertyType.createProperty();
        property.load(entity);
        buf.appendParameter(property.asInParameter());
        buf.appendSql(", ");
      }
      buf.cutBackSql(2);
    }
    buf.appendSql(")");
  }
}
