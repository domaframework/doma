package org.seasar.doma.jdbc.query;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import org.seasar.doma.jdbc.criteria.entity.Dept_;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.wrapper.Wrapper;

class UpsertAssemblerContextTest {
  private final MockConfig config = new MockConfig();
  private final PreparedSqlBuilder buf =
      new PreparedSqlBuilder(config, SqlKind.BATCH_INSERT, SqlLogType.RAW);
  private final Dept_ metaDept = new Dept_();
  private final List<EntityPropertyType<?, ?>> propertyTypes =
      metaDept.allPropertyMetamodels().stream().map(PropertyMetamodel::asType).collect(toList());

  @Test
  void onDuplicateKeyException() {
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
              new UpsertAssemblerContext(
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
  void onDuplicateKeyUpdate() {
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
        new UpsertAssemblerContext(
            buf,
            metaDept.asType(),
            DuplicateKeyType.UPDATE,
            Naming.NONE,
            config.getDialect(),
            keys,
            insertValues,
            setValues);
    assertNotNull(context);
  }

  @Test
  void onDuplicateKeyUpdate_emptyKey() {
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
        new UpsertAssemblerContext(
            buf,
            metaDept.asType(),
            DuplicateKeyType.UPDATE,
            Naming.NONE,
            config.getDialect(),
            keys,
            insertValues,
            setValues);
    assertNotNull(context);
  }

  @Test
  void onDuplicateKeyUpdate_emptyInsertValues() {
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
              new UpsertAssemblerContext(
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
  void onDuplicateKeyUpdate_emptySetValues() {
    List<EntityPropertyType<?, ?>> keys =
        propertyTypes.stream().filter(EntityPropertyType::isId).collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues =
        propertyTypes.stream()
            .filter(c -> !c.isId())
            .map(c -> new Tuple2<EntityPropertyType<?, ?>, InParameter<?>>(c, mockInParameter()))
            .collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues = Collections.emptyList();
    UpsertAssemblerContext context =
        new UpsertAssemblerContext(
            buf,
            metaDept.asType(),
            DuplicateKeyType.UPDATE,
            Naming.NONE,
            config.getDialect(),
            keys,
            insertValues,
            setValues);
    assertNotNull(context);
  }

  @Test
  void onDuplicateKeyIgnore() {
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
        new UpsertAssemblerContext(
            buf,
            metaDept.asType(),
            DuplicateKeyType.IGNORE,
            Naming.NONE,
            config.getDialect(),
            keys,
            insertValues,
            setValues);
    assertNotNull(context);
  }

  @Test
  void onDuplicateKeyIgnore_emptyKey() {
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
        new UpsertAssemblerContext(
            buf,
            metaDept.asType(),
            DuplicateKeyType.IGNORE,
            Naming.NONE,
            config.getDialect(),
            keys,
            insertValues,
            setValues);
    assertNotNull(context);
  }

  @Test
  void onDuplicateKeyIgnore_emptyInsertValues() {
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
              new UpsertAssemblerContext(
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
  void onDuplicateKeyIgnore_emptySetValues() {
    List<EntityPropertyType<?, ?>> keys =
        propertyTypes.stream().filter(EntityPropertyType::isId).collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues =
        propertyTypes.stream()
            .filter(c -> !c.isId())
            .map(c -> new Tuple2<EntityPropertyType<?, ?>, InParameter<?>>(c, mockInParameter()))
            .collect(toList());
    List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues = Collections.emptyList();
    UpsertAssemblerContext context =
        new UpsertAssemblerContext(
            buf,
            metaDept.asType(),
            DuplicateKeyType.IGNORE,
            Naming.NONE,
            config.getDialect(),
            keys,
            insertValues,
            setValues);
    assertNotNull(context);
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
