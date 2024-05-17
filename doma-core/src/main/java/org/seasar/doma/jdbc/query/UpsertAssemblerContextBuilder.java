package org.seasar.doma.jdbc.query;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
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
      List<? extends Tuple2<? extends EntityPropertyType<?, ?>, ? extends InParameter<?>>>
          insertValues,
      List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues) {

    List<? extends EntityPropertyType<?, ?>> resolvedKeys = resolveKeys(entityType, keys);

    return buildInternal(
        buf,
        entityType,
        duplicateKeyType,
        naming,
        dialect,
        !keys.isEmpty(),
        resolvedKeys,
        insertValues,
        setValues);
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

    List<Tuple2<? extends EntityPropertyType<?, ?>, ? extends InParameter<?>>> insertValues =
        insertPropertyTypes.stream()
            .map(
                p -> {
                  Property<ENTITY, ?> property = p.createProperty();
                  property.load(entity);
                  return new Tuple2<>(p, property.asInParameter());
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
        insertValues,
        Collections.emptyList());
  }

  private static UpsertAssemblerContext buildInternal(
      PreparedSqlBuilder buf,
      EntityType<?> entityType,
      DuplicateKeyType duplicateKeyType,
      Naming naming,
      Dialect dialect,
      boolean isKeysSpecified,
      List<? extends EntityPropertyType<?, ?>> keys,
      List<? extends Tuple2<? extends EntityPropertyType<?, ?>, ? extends InParameter<?>>>
          insertValues,
      List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues) {

    List<? extends Tuple2<? extends EntityPropertyType<?, ?>, ? extends UpsertSetValue>>
        resolvedSetValues = resolveSetValues(keys, insertValues, setValues);

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

  private static List<
          ? extends Tuple2<? extends EntityPropertyType<?, ?>, ? extends UpsertSetValue>>
      resolveSetValues(
          List<? extends EntityPropertyType<?, ?>> keys,
          List<? extends Tuple2<? extends EntityPropertyType<?, ?>, ? extends InParameter<?>>>
              insertValues,
          List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues) {
    if (!setValues.isEmpty()) {
      return setValues;
    }
    return insertValues.stream()
        .map(Tuple2::component1)
        .filter(p -> !keys.contains(p))
        .filter(p -> p.isUpdatable() && !p.isId() && !p.isTenantId())
        .map(p -> new Tuple2<>(p, new UpsertSetValue.Prop(p)))
        .collect(Collectors.toList());
  }
}
