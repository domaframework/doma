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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;

public class UpsertAssemblerContextBuilder {

  public static UpsertAssemblerContext build(
      PreparedSqlBuilder buf,
      EntityType<?> entityType,
      DuplicateKeyType duplicateKeyType,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<?, ?>> keys,
      List<QueryOperandPair> insertValues,
      List<QueryOperandPair> setValues) {
    return build(
        buf, entityType, duplicateKeyType, naming, dialect, keys, insertValues, setValues, false);
  }

  public static UpsertAssemblerContext build(
      PreparedSqlBuilder buf,
      EntityType<?> entityType,
      DuplicateKeyType duplicateKeyType,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<?, ?>> keys,
      List<QueryOperandPair> insertValues,
      List<QueryOperandPair> setValues,
      boolean returning) {

    List<? extends EntityPropertyType<?, ?>> resolvedKeys = resolveKeys(entityType, keys);
    List<? extends EntityPropertyType<?, ?>> insertPropertyTypes =
        insertValues.stream()
            .map(pair -> pair.getLeft().getEntityPropertyType())
            .collect(Collectors.toList());
    List<QueryOperand> values =
        insertValues.stream().map(QueryOperandPair::getRight).collect(Collectors.toList());
    List<InsertRow> rows = Collections.singletonList(new InsertRow(values));

    return buildInternal(
        buf,
        entityType,
        duplicateKeyType,
        naming,
        dialect,
        !keys.isEmpty(),
        resolvedKeys,
        insertPropertyTypes,
        rows,
        setValues,
        returning);
  }

  public static <ENTITY> UpsertAssemblerContext buildFromEntity(
      PreparedSqlBuilder buf,
      EntityType<ENTITY> entityType,
      DuplicateKeyType duplicateKeyType,
      List<EntityPropertyType<ENTITY, ?>> duplicateKeys,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<ENTITY, ?>> idPropertyTypes,
      List<EntityPropertyType<ENTITY, ?>> insertPropertyTypes,
      ENTITY entity) {

    return buildFromEntity(
        buf,
        entityType,
        duplicateKeyType,
        duplicateKeys,
        naming,
        dialect,
        idPropertyTypes,
        insertPropertyTypes,
        entity,
        false);
  }

  public static <ENTITY> UpsertAssemblerContext buildFromEntity(
      PreparedSqlBuilder buf,
      EntityType<ENTITY> entityType,
      DuplicateKeyType duplicateKeyType,
      List<EntityPropertyType<ENTITY, ?>> duplicateKeys,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<ENTITY, ?>> idPropertyTypes,
      List<EntityPropertyType<ENTITY, ?>> insertPropertyTypes,
      ENTITY entity,
      boolean returning) {

    return buildFromEntityList(
        buf,
        entityType,
        duplicateKeyType,
        duplicateKeys,
        naming,
        dialect,
        idPropertyTypes,
        insertPropertyTypes,
        Collections.singletonList(entity),
        returning);
  }

  public static <ENTITY> UpsertAssemblerContext buildFromEntityList(
      PreparedSqlBuilder buf,
      EntityType<ENTITY> entityType,
      DuplicateKeyType duplicateKeyType,
      List<EntityPropertyType<ENTITY, ?>> duplicateKeys,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<ENTITY, ?>> idPropertyTypes,
      List<EntityPropertyType<ENTITY, ?>> insertPropertyTypes,
      List<ENTITY> entities) {
    return buildFromEntityList(
        buf,
        entityType,
        duplicateKeyType,
        duplicateKeys,
        naming,
        dialect,
        idPropertyTypes,
        insertPropertyTypes,
        entities,
        false);
  }

  public static <ENTITY> UpsertAssemblerContext buildFromEntityList(
      PreparedSqlBuilder buf,
      EntityType<ENTITY> entityType,
      DuplicateKeyType duplicateKeyType,
      List<EntityPropertyType<ENTITY, ?>> duplicateKeys,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<ENTITY, ?>> idPropertyTypes,
      List<EntityPropertyType<ENTITY, ?>> insertPropertyTypes,
      List<ENTITY> entities,
      boolean returning) {

    List<InsertRow> rows =
        entities.stream()
            .map(
                entity ->
                    insertPropertyTypes.stream()
                        .map(
                            p -> {
                              Property<ENTITY, ?> property = p.createProperty();
                              property.load(entity);
                              return (QueryOperand)
                                  new QueryOperand.Param(p, property.asInParameter());
                            })
                        .collect(Collectors.toList()))
            .map(InsertRow::new)
            .collect(Collectors.toList());

    return buildInternal(
        buf,
        entityType,
        duplicateKeyType,
        naming,
        dialect,
        !duplicateKeys.isEmpty(),
        !duplicateKeys.isEmpty() ? duplicateKeys : idPropertyTypes,
        insertPropertyTypes,
        rows,
        Collections.emptyList(),
        returning);
  }

  private static UpsertAssemblerContext buildInternal(
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
      boolean returning) {

    List<QueryOperandPair> resolvedSetValues =
        resolveSetValues(keys, insertPropertyTypes, setValues);

    return new UpsertAssemblerContext(
        buf,
        entityType,
        duplicateKeyType,
        naming,
        dialect,
        isKeysSpecified,
        keys,
        insertPropertyTypes,
        insertRows,
        resolvedSetValues,
        returning);
  }

  private static List<? extends EntityPropertyType<?, ?>> resolveKeys(
      EntityType<?> entityType, List<? extends EntityPropertyType<?, ?>> keys) {
    if (!keys.isEmpty()) {
      return keys;
    }
    return entityType.getIdPropertyTypes();
  }

  private static List<QueryOperandPair> resolveSetValues(
      List<? extends EntityPropertyType<?, ?>> keys,
      List<? extends EntityPropertyType<?, ?>> insertPropertyTypes,
      List<QueryOperandPair> setValues) {
    if (!setValues.isEmpty()) {
      return setValues;
    }
    return insertPropertyTypes.stream()
        .filter(p -> !keys.contains(p))
        .filter(p -> p.isUpdatable() && !p.isId() && !p.isTenantId())
        .map(
            p -> {
              QueryOperand operand = new QueryOperand.Prop(p);
              return new QueryOperandPair(operand, operand);
            })
        .collect(Collectors.toList());
  }
}
