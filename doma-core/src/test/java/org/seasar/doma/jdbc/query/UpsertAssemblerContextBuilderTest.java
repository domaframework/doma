package org.seasar.doma.jdbc.query;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlParameterVisitor;
import org.seasar.doma.jdbc.criteria.entity.Dept;
import org.seasar.doma.jdbc.criteria.entity.Dept_;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.wrapper.Wrapper;

class UpsertAssemblerContextBuilderTest {
  private final MockConfig config = new MockConfig();
  private final PreparedSqlBuilder buf =
      new PreparedSqlBuilder(config, SqlKind.BATCH_INSERT, SqlLogType.RAW);
  private final Dept_ metaDept = new Dept_();
  private final List<EntityPropertyType<Dept, ?>> propertyTypes =
      metaDept.asType().getEntityPropertyTypes();

  @Test
  void build_onDuplicateKeyException() {
    List<EntityPropertyType<?, ?>> keys =
        propertyTypes.stream().filter(EntityPropertyType::isId).collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues =
        propertyTypes.stream()
            .filter(c -> !c.isId())
            .map(c -> new Tuple2<EntityPropertyType<?, ?>, InParameter<?>>(c, mockInParameter()))
            .collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues =
        propertyTypes.stream()
            .filter(c -> !c.isId())
            .map(
                c ->
                    new Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>(
                        c, new UpsertSetValue.Prop(c)))
            .collect(toList());
    DomaIllegalArgumentException ex =
        assertThrows(
            DomaIllegalArgumentException.class,
            () -> {
              UpsertAssemblerContextBuilder.build(
                  buf,
                  metaDept.asType(),
                  DuplicateKeyType.EXCEPTION,
                  Naming.NONE,
                  config.getDialect(),
                  keys,
                  insertValues,
                  setValues);
            });
    System.out.println(ex.getMessage());
  }

  @Test
  void build_onDuplicateKeyUpdate() {
    List<EntityPropertyType<?, ?>> keys =
        propertyTypes.stream().filter(EntityPropertyType::isId).collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues =
        propertyTypes.stream()
            .filter(c -> !c.isId())
            .map(c -> new Tuple2<EntityPropertyType<?, ?>, InParameter<?>>(c, mockInParameter()))
            .collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues =
        propertyTypes.stream()
            .filter(c -> !c.isId())
            .map(
                c ->
                    new Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>(
                        c, new UpsertSetValue.Prop(c)))
            .collect(toList());
    UpsertAssemblerContext context =
        UpsertAssemblerContextBuilder.build(
            buf,
            metaDept.asType(),
            DuplicateKeyType.UPDATE,
            Naming.NONE,
            config.getDialect(),
            keys,
            insertValues,
            setValues);
    assertNotNull(context);
    assertTrue(context.isKeysSpecified);
  }

  @Test
  void build_onDuplicateKeyUpdate_emptyKey() {
    List<EntityPropertyType<?, ?>> keys = Collections.emptyList();
    List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues =
        propertyTypes.stream()
            .filter(c -> !c.isId())
            .map(c -> new Tuple2<EntityPropertyType<?, ?>, InParameter<?>>(c, mockInParameter()))
            .collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues =
        propertyTypes.stream()
            .filter(c -> !c.isId())
            .map(
                c ->
                    new Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>(
                        c, new UpsertSetValue.Prop(c)))
            .collect(toList());
    UpsertAssemblerContext context =
        UpsertAssemblerContextBuilder.build(
            buf,
            metaDept.asType(),
            DuplicateKeyType.UPDATE,
            Naming.NONE,
            config.getDialect(),
            keys,
            insertValues,
            setValues);
    assertFalse(context.isKeysSpecified);
    assertFalse(context.keys.isEmpty());
  }

  @Test
  void build_onDuplicateKeyUpdate_emptyInsertValues() {
    List<EntityPropertyType<?, ?>> keys =
        propertyTypes.stream().filter(EntityPropertyType::isId).collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues = Collections.emptyList();
    List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues =
        propertyTypes.stream()
            .filter(c -> !c.isId())
            .map(
                c ->
                    new Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>(
                        c, new UpsertSetValue.Prop(c)))
            .collect(toList());
    DomaIllegalArgumentException ex =
        assertThrows(
            DomaIllegalArgumentException.class,
            () -> {
              UpsertAssemblerContextBuilder.build(
                  buf,
                  metaDept.asType(),
                  DuplicateKeyType.UPDATE,
                  Naming.NONE,
                  config.getDialect(),
                  keys,
                  insertValues,
                  setValues);
            });
    System.out.println(ex.getMessage());
  }

  @Test
  void build_onDuplicateKeyUpdate_emptySetValues() {
    List<EntityPropertyType<?, ?>> keys =
        propertyTypes.stream().filter(EntityPropertyType::isId).collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues =
        propertyTypes.stream()
            .filter(c -> !c.isId())
            .map(c -> new Tuple2<EntityPropertyType<?, ?>, InParameter<?>>(c, mockInParameter()))
            .collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues = Collections.emptyList();
    UpsertAssemblerContext context =
        UpsertAssemblerContextBuilder.build(
            buf,
            metaDept.asType(),
            DuplicateKeyType.UPDATE,
            Naming.NONE,
            config.getDialect(),
            keys,
            insertValues,
            setValues);
    assertFalse(context.setValues.isEmpty());
  }

  @Test
  void build_onDuplicateKeyIgnore() {
    List<EntityPropertyType<?, ?>> keys =
        propertyTypes.stream().filter(EntityPropertyType::isId).collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues =
        propertyTypes.stream()
            .filter(c -> !c.isId())
            .map(c -> new Tuple2<EntityPropertyType<?, ?>, InParameter<?>>(c, mockInParameter()))
            .collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues =
        propertyTypes.stream()
            .filter(c -> !c.isId())
            .map(
                c ->
                    new Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>(
                        c, new UpsertSetValue.Prop(c)))
            .collect(toList());
    UpsertAssemblerContext context =
        UpsertAssemblerContextBuilder.build(
            buf,
            metaDept.asType(),
            DuplicateKeyType.IGNORE,
            Naming.NONE,
            config.getDialect(),
            keys,
            insertValues,
            setValues);
    assertNotNull(context);
    assertTrue(context.isKeysSpecified);
  }

  @Test
  void build_onDuplicateKeyIgnore_emptyKey() {
    List<EntityPropertyType<?, ?>> keys = Collections.emptyList();
    List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues =
        propertyTypes.stream()
            .filter(c -> !c.isId())
            .map(c -> new Tuple2<EntityPropertyType<?, ?>, InParameter<?>>(c, mockInParameter()))
            .collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues =
        propertyTypes.stream()
            .filter(c -> !c.isId())
            .map(
                c ->
                    new Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>(
                        c, new UpsertSetValue.Prop(c)))
            .collect(toList());
    UpsertAssemblerContext context =
        UpsertAssemblerContextBuilder.build(
            buf,
            metaDept.asType(),
            DuplicateKeyType.IGNORE,
            Naming.NONE,
            config.getDialect(),
            keys,
            insertValues,
            setValues);
    assertFalse(context.isKeysSpecified);
    assertFalse(context.keys.isEmpty());
  }

  @Test
  void build_onDuplicateKeyIgnore_emptyInsertValues() {
    List<EntityPropertyType<?, ?>> keys =
        propertyTypes.stream().filter(EntityPropertyType::isId).collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues = Collections.emptyList();
    List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues =
        propertyTypes.stream()
            .filter(c -> !c.isId())
            .map(
                c ->
                    new Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>(
                        c, new UpsertSetValue.Prop(c)))
            .collect(toList());
    DomaIllegalArgumentException ex =
        assertThrows(
            DomaIllegalArgumentException.class,
            () -> {
              UpsertAssemblerContextBuilder.build(
                  buf,
                  metaDept.asType(),
                  DuplicateKeyType.IGNORE,
                  Naming.NONE,
                  config.getDialect(),
                  keys,
                  insertValues,
                  setValues);
            });
    System.out.println(ex.getMessage());
  }

  @Test
  void build_onDuplicateKeyIgnore_emptySetValues() {
    List<EntityPropertyType<?, ?>> keys =
        propertyTypes.stream().filter(EntityPropertyType::isId).collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues =
        propertyTypes.stream()
            .filter(c -> !c.isId())
            .map(c -> new Tuple2<EntityPropertyType<?, ?>, InParameter<?>>(c, mockInParameter()))
            .collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues = Collections.emptyList();
    UpsertAssemblerContext context =
        UpsertAssemblerContextBuilder.build(
            buf,
            metaDept.asType(),
            DuplicateKeyType.IGNORE,
            Naming.NONE,
            config.getDialect(),
            keys,
            insertValues,
            setValues);
    assertNotNull(context);
    assertFalse(context.setValues.isEmpty());
  }

  @Test
  void buildFromEntity() {
    List<EntityPropertyType<Dept, ?>> idPropertyTypes =
        propertyTypes.stream().filter(EntityPropertyType::isId).collect(toList());
    List<EntityPropertyType<Dept, ?>> insertPropertyTypes =
        metaDept.asType().getEntityPropertyTypes().stream()
            .filter(c -> !c.isId())
            .collect(toList());
    Dept dept = new Dept();
    dept.setId(1);
    dept.setName("a");
    UpsertAssemblerContext context =
        UpsertAssemblerContextBuilder.buildFromEntity(
            buf,
            metaDept.asType(),
            DuplicateKeyType.IGNORE,
            Naming.NONE,
            config.getDialect(),
            idPropertyTypes,
            insertPropertyTypes,
            dept);
    assertFalse(context.isKeysSpecified);
    assertFalse(context.keys.isEmpty());
    assertFalse(context.insertValues.isEmpty());
    assertFalse(context.setValues.isEmpty());
  }

  private InParameter<Object> mockInParameter() {
    return new InParameter<Object>() {
      @Override
      public Object getValue() {
        return null;
      }

      @Override
      public <R, P, TH extends Throwable> R accept(SqlParameterVisitor<R, P, TH> visitor, P p)
          throws TH {
        return null;
      }

      @Override
      public Optional<Class<?>> getDomainClass() {
        return Optional.empty();
      }

      @Override
      public Wrapper<Object> getWrapper() {
        return null;
      }
    };
  }
}
