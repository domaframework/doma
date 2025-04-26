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
import org.seasar.doma.jdbc.entity.VersionPropertyType;
import org.seasar.doma.jdbc.query.UpdateAssembler;
import org.seasar.doma.jdbc.query.UpdateAssemblerContext;
import org.seasar.doma.jdbc.query.UpdateQueryHelper;

public class DefaultUpdateAssembler<ENTITY> implements UpdateAssembler {

  private final PreparedSqlBuilder buf;
  private final EntityType<ENTITY> entityType;
  private final Naming naming;
  private final Dialect dialect;
  private final UpdateQueryHelper<ENTITY> updateQueryHelper;
  private final List<EntityPropertyType<ENTITY, ?>> idPropertyTypes;
  private final List<EntityPropertyType<ENTITY, ?>> updatePropertyTypes;
  private final VersionPropertyType<ENTITY, ?, ?> versionPropertyType;
  private final EntityPropertyType<ENTITY, ?> tenantIdPropertyType;
  private final boolean versionIgnored;
  private final ENTITY entity;
  private final boolean returning;

  public DefaultUpdateAssembler(UpdateAssemblerContext<ENTITY> context) {
    Objects.requireNonNull(context);
    this.buf = context.buf;
    this.entityType = context.entityType;
    this.naming = context.naming;
    this.dialect = context.dialect;
    this.updateQueryHelper = context.updateQueryHelper;
    this.idPropertyTypes = context.idPropertyTypes;
    this.updatePropertyTypes = context.updatePropertyTypes;
    this.versionPropertyType = context.versionPropertyType;
    this.tenantIdPropertyType = context.tenantIdPropertyType;
    this.versionIgnored = context.versionIgnored;
    this.entity = context.entity;
    this.returning = context.returning;
  }

  @Override
  public void assemble() {
    assembleUpdateSet();
    buf.appendSql(" ");
    assembleWhere();
  }

  void assembleUpdateSet() {
    buf.appendSql("update ");
    buf.appendSql(entityType.getQualifiedTableName(naming::apply, dialect::applyQuote));
    buf.appendSql(" set ");
    updateQueryHelper.populateValues(entity, updatePropertyTypes, versionPropertyType, buf);
  }

  void assembleWhere() {
    boolean whereClauseAppended = false;
    if (idPropertyTypes.size() > 0) {
      buf.appendSql("where ");
      whereClauseAppended = true;
      for (EntityPropertyType<ENTITY, ?> propertyType : idPropertyTypes) {
        Property<ENTITY, ?> property = propertyType.createProperty();
        property.load(entity);
        buf.appendSql(propertyType.getColumnName(naming::apply, dialect::applyQuote));
        buf.appendSql(" = ");
        buf.appendParameter(property.asInParameter());
        buf.appendSql(" and ");
      }
      buf.cutBackSql(5);
    }
    if (!versionIgnored && versionPropertyType != null) {
      if (whereClauseAppended) {
        buf.appendSql(" and ");
      } else {
        buf.appendSql("where ");
        whereClauseAppended = true;
      }
      Property<ENTITY, ?> property = versionPropertyType.createProperty();
      property.load(entity);
      buf.appendSql(versionPropertyType.getColumnName(naming::apply, dialect::applyQuote));
      buf.appendSql(" = ");
      buf.appendParameter(property.asInParameter());
    }
    if (tenantIdPropertyType != null) {
      if (whereClauseAppended) {
        buf.appendSql(" and ");
      } else {
        buf.appendSql("where ");
        //noinspection UnusedAssignment
        whereClauseAppended = true;
      }
      Property<ENTITY, ?> property = tenantIdPropertyType.createProperty();
      property.load(entity);
      buf.appendSql(tenantIdPropertyType.getColumnName(naming::apply, dialect::applyQuote));
      buf.appendSql(" = ");
      buf.appendParameter(property.asInParameter());
    }
  }
}
