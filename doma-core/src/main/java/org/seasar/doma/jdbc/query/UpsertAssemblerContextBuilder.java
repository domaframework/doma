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

    List<? extends EntityPropertyType<?, ?>> resolvedKeys = resolveKeys(entityType, keys);

    return buildInternal(
        buf,
        entityType,
        duplicateKeyType,
        naming,
        dialect,
        !keys.isEmpty(),
        resolvedKeys,
        new QueryRows(Collections.singletonList(new QueryOperandPairList(insertValues))),
        new QueryOperandPairList(setValues));
  }

  public static <ENTITY> UpsertAssemblerContext buildFromEntity(
      PreparedSqlBuilder buf,
      EntityType<ENTITY> entityType,
      DuplicateKeyType duplicateKeyType,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<ENTITY, ?>> idPropertyTypes,
      List<EntityPropertyType<ENTITY, ?>> insertPropertyTypes,
      ENTITY entity) {

    List<QueryOperandPair> insertValues =
        insertPropertyTypes.stream()
            .map(
                p -> {
                  Property<ENTITY, ?> property = p.createProperty();
                  property.load(entity);
                  QueryOperand left = new QueryOperand.Prop(p);
                  QueryOperand right = new QueryOperand.Param(p, property.asInParameter());
                  return new QueryOperandPair(left, right);
                })
            .collect(Collectors.toList());

    return buildInternal(
        buf,
        entityType,
        duplicateKeyType,
        naming,
        dialect,
        false,
        idPropertyTypes,
        new QueryRows(Collections.singletonList(new QueryOperandPairList(insertValues))),
        new QueryOperandPairList(Collections.emptyList()));
  }

  public static <ENTITY> UpsertAssemblerContext buildFromEntityList(
      PreparedSqlBuilder buf,
      EntityType<ENTITY> entityType,
      DuplicateKeyType duplicateKeyType,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<ENTITY, ?>> idPropertyTypes,
      List<EntityPropertyType<ENTITY, ?>> insertPropertyTypes,
      List<ENTITY> entities) {

    List<QueryOperandPairList> rows =
        entities.stream()
            .map(
                entity ->
                    insertPropertyTypes.stream()
                        .map(
                            p -> {
                              Property<ENTITY, ?> property = p.createProperty();
                              property.load(entity);
                              QueryOperand left = new QueryOperand.Prop(p);
                              QueryOperand right =
                                  new QueryOperand.Param(p, property.asInParameter());
                              return new QueryOperandPair(left, right);
                            })
                        .collect(Collectors.toList()))
            .map(QueryOperandPairList::new)
            .collect(Collectors.toList());
    return buildInternal(
        buf,
        entityType,
        duplicateKeyType,
        naming,
        dialect,
        false,
        idPropertyTypes,
        new QueryRows(rows),
        new QueryOperandPairList(Collections.emptyList()));
  }

  private static UpsertAssemblerContext buildInternal(
      PreparedSqlBuilder buf,
      EntityType<?> entityType,
      DuplicateKeyType duplicateKeyType,
      Naming naming,
      Dialect dialect,
      boolean isKeysSpecified,
      List<? extends EntityPropertyType<?, ?>> keys,
      QueryRows insertValues,
      QueryOperandPairList setValues) {

    QueryOperandPairList resolvedSetValues = resolveSetValues(keys, insertValues, setValues);

    return new UpsertAssemblerContext(
        buf,
        entityType,
        duplicateKeyType,
        naming,
        dialect,
        isKeysSpecified,
        keys,
        insertValues,
        resolvedSetValues);
  }

  private static List<? extends EntityPropertyType<?, ?>> resolveKeys(
      EntityType<?> entityType, List<? extends EntityPropertyType<?, ?>> keys) {
    if (!keys.isEmpty()) {
      return keys;
    }
    return entityType.getIdPropertyTypes();
  }

  private static QueryOperandPairList resolveSetValues(
      List<? extends EntityPropertyType<?, ?>> keys,
      QueryRows insertValues,
      QueryOperandPairList setValues) {
    if (!setValues.isEmpty()) {
      return setValues;
    }
    QueryOperandPairList firstInsertValues = insertValues.getRows().get(0);
    List<QueryOperandPair> result =
        firstInsertValues.getPairs().stream()
            .map(pair -> pair.getLeft().getEntityPropertyType())
            .filter(p -> !keys.contains(p))
            .filter(p -> p.isUpdatable() && !p.isId() && !p.isTenantId())
            .map(
                p -> {
                  QueryOperand operand = new QueryOperand.Prop(p);
                  return new QueryOperandPair(operand, operand);
                })
            .collect(Collectors.toList());
    return new QueryOperandPairList(result);
  }
}
