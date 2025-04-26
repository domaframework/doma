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

import java.util.List;
import java.util.Objects;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * The UpsertAssemblerContext class represents the context for executing an upsert operation in a
 * database. It holds information about the entity to be upserted, the prepared SQL statement, the
 * entity type, the duplicate key type, naming and dialect information, and the values to be set for
 * the entity.
 */
public class UpsertAssemblerContext {
  public final PreparedSqlBuilder buf;
  public final EntityType<?> entityType;
  public final DuplicateKeyType duplicateKeyType;
  public final Naming naming;
  public final Dialect dialect;

  public final boolean isKeysSpecified;

  /**
   * conflicting keys
   *
   * @see EntityPropertyType
   */
  public final List<? extends EntityPropertyType<?, ?>> keys;

  public final List<? extends EntityPropertyType<?, ?>> insertPropertyTypes;

  /** values clause property-parameter pair list */
  public final List<InsertRow> insertRows;

  /** set clause property-value pair list */
  public final List<QueryOperandPair> setValues;

  public final ReturningProperties returning;

  /**
   * Constructs an instance of UpsertAssemblerContext with the specified prepared SQL builder,
   * entity
   *
   * @param buf the prepared SQL builder
   * @param entityType the entity type
   * @param duplicateKeyType the duplicate key type
   * @param naming the naming
   * @param dialect the dialect
   * @param isKeysSpecified whether the keys are specified
   * @param keys the conflicting keys
   * @param insertPropertyTypes the insert property types
   * @param insertRows the insert rows
   * @param setValues the set clause property-value pair list(optional).Required in case of
   *     duplicateKeyType.UPDATE
   * @param returning whether returning values is required
   */
  UpsertAssemblerContext(
      PreparedSqlBuilder buf,
      EntityType<?> entityType,
      DuplicateKeyType duplicateKeyType,
      Naming naming,
      Dialect dialect,
      boolean isKeysSpecified,
      List<? extends EntityPropertyType<?, ?>> keys,
      List<? extends EntityPropertyType<?, ?>> insertPropertyTypes,
      List<InsertRow> insertRows,
      List<QueryOperandPair> setValues,
      ReturningProperties returning) {
    Objects.requireNonNull(buf);
    Objects.requireNonNull(entityType);
    Objects.requireNonNull(duplicateKeyType);
    Objects.requireNonNull(naming);
    Objects.requireNonNull(dialect);
    Objects.requireNonNull(keys);
    Objects.requireNonNull(insertPropertyTypes);
    Objects.requireNonNull(insertRows);
    Objects.requireNonNull(setValues);
    Objects.requireNonNull(returning);
    if (duplicateKeyType == DuplicateKeyType.EXCEPTION) {
      throw new DomaIllegalArgumentException(
          "duplicateKeyType",
          "The duplicateKeyType must not be set to EXCEPTION when performing an upsert.");
    }
    if (keys.isEmpty()) {
      throw new DomaIllegalArgumentException(
          "keys",
          "The keys must not be empty when performing an upsert. At least one key must be specified.");
    }
    if (insertPropertyTypes.isEmpty()) {
      throw new DomaIllegalArgumentException(
          "insertPropertyTypes",
          "The insertPropertyTypes must not be empty when performing an upsert. At least one property must be specified.");
    }
    if (insertRows.isEmpty() || insertRows.stream().anyMatch(row -> !row.iterator().hasNext())) {
      throw new DomaIllegalArgumentException(
          "insertRows",
          "The insertRows must not be empty when performing an upsert. At least one insert value must be specified.");
    }
    if (duplicateKeyType == DuplicateKeyType.UPDATE && setValues.isEmpty()) {
      throw new DomaIllegalArgumentException(
          "setValues",
          "The setValues must not be empty when performing an upsert with the UPDATE duplicateKeyType. At least one set value must be specified.");
    }
    this.buf = buf;
    this.entityType = entityType;
    this.duplicateKeyType = duplicateKeyType;
    this.naming = naming;
    this.dialect = dialect;
    this.isKeysSpecified = isKeysSpecified;
    this.keys = keys;
    this.insertPropertyTypes = insertPropertyTypes;
    this.insertRows = insertRows;
    this.setValues = setValues;
    this.returning = returning;
  }
}
