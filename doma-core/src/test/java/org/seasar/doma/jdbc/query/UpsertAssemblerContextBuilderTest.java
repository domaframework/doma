package org.seasar.doma.jdbc.query;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.entity.Dept;
import org.seasar.doma.jdbc.criteria.entity.Dept_;
import org.seasar.doma.jdbc.entity.EntityPropertyType;

class UpsertAssemblerContextBuilderTest {
  private final MockConfig config = new MockConfig();
  private final PreparedSqlBuilder buf =
      new PreparedSqlBuilder(config, SqlKind.BATCH_INSERT, SqlLogType.RAW);
  private final Dept_ metaDept = new Dept_();
  private final List<EntityPropertyType<Dept, ?>> propertyTypes =
      metaDept.asType().getEntityPropertyTypes();
  private final List<EntityPropertyType<?, ?>> keys =
      propertyTypes.stream().filter(EntityPropertyType::isId).collect(toList());
  private final List<QueryOperandPair> insertValues =
      propertyTypes.stream()
          .filter(p -> !p.isId())
          .map(p -> mockQueryOperandPair(p, p))
          .collect(toList());
  private final List<QueryOperandPair> setValues =
      propertyTypes.stream()
          .filter(p -> !p.isId())
          .map(p -> mockQueryOperandPair(p, p))
          .collect(toList());

  @Test
  void build_onDuplicateKeyException() {
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
  void build_onDuplicateKeyUpdate_emptyKeys() {
    List<EntityPropertyType<?, ?>> emptyKeys = Collections.emptyList();
    UpsertAssemblerContext context =
        UpsertAssemblerContextBuilder.build(
            buf,
            metaDept.asType(),
            DuplicateKeyType.UPDATE,
            Naming.NONE,
            config.getDialect(),
            emptyKeys,
            insertValues,
            setValues);
    assertFalse(context.isKeysSpecified);
    assertFalse(context.keys.isEmpty());
  }

  @Test
  void build_onDuplicateKeyUpdate_emptyInsertValues() {
    List<QueryOperandPair> emptyInsertValues = Collections.emptyList();
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
                  emptyInsertValues,
                  setValues);
            });
    System.out.println(ex.getMessage());
  }

  @Test
  void build_onDuplicateKeyUpdate_emptySetValues() {
    List<QueryOperandPair> emptySetValues = Collections.emptyList();
    UpsertAssemblerContext context =
        UpsertAssemblerContextBuilder.build(
            buf,
            metaDept.asType(),
            DuplicateKeyType.UPDATE,
            Naming.NONE,
            config.getDialect(),
            keys,
            insertValues,
            emptySetValues);
    assertFalse(context.setValues.isEmpty());
  }

  @Test
  void build_onDuplicateKeyIgnore() {
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
  void build_onDuplicateKeyIgnore_emptyKeys() {
    List<EntityPropertyType<?, ?>> emptyKeys = Collections.emptyList();
    UpsertAssemblerContext context =
        UpsertAssemblerContextBuilder.build(
            buf,
            metaDept.asType(),
            DuplicateKeyType.IGNORE,
            Naming.NONE,
            config.getDialect(),
            emptyKeys,
            insertValues,
            setValues);
    assertFalse(context.isKeysSpecified);
    assertFalse(context.keys.isEmpty());
  }

  @Test
  void build_onDuplicateKeyIgnore_emptyInsertValues() {
    List<QueryOperandPair> emptyInsertValues = Collections.emptyList();
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
                  emptyInsertValues,
                  setValues);
            });
    System.out.println(ex.getMessage());
  }

  @Test
  void build_onDuplicateKeyIgnore_emptySetValues() {
    List<QueryOperandPair> emptySetValues = Collections.emptyList();
    UpsertAssemblerContext context =
        UpsertAssemblerContextBuilder.build(
            buf,
            metaDept.asType(),
            DuplicateKeyType.IGNORE,
            Naming.NONE,
            config.getDialect(),
            keys,
            insertValues,
            emptySetValues);
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

  private QueryOperandPair mockQueryOperandPair(
      EntityPropertyType<?, ?> left, EntityPropertyType<?, ?> right) {
    return new QueryOperandPair(mockQueryOperand(left), mockQueryOperand(right));
  }

  private QueryOperand mockQueryOperand(final EntityPropertyType<?, ?> propertyType) {
    return new QueryOperand() {

      @Override
      public EntityPropertyType<?, ?> getEntityPropertyType() {
        return propertyType;
      }

      @Override
      public void accept(Visitor visitor) {}
    };
  }
}
