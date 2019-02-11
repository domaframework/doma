package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityPropertyMeta;
import org.seasar.doma.jdbc.entity.AssignedIdPropertyType;
import org.seasar.doma.jdbc.entity.DefaultPropertyType;
import org.seasar.doma.jdbc.entity.EmbeddedPropertyType;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.jdbc.entity.TenantIdPropertyType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

public class EntityDescPropertyGenerator extends AbstractGenerator {

  private static final String NULL = "null";

  private final EntityMeta entityMeta;

  private final EntityPropertyMeta pm;

  EntityDescPropertyGenerator(
      Context ctx,
      ClassName className,
      Printer printer,
      EntityMeta entityMeta,
      EntityPropertyMeta entityPropertyMeta) {
    super(ctx, className, printer);
    assertNotNull(entityMeta);
    this.entityMeta = entityMeta;
    this.pm = entityPropertyMeta;
  }

  @Override
  public void generate() {
    printPropertyTypeField();
  }

  private void printPropertyTypeField() {
    iprint("/** the %1$s */%n", pm.getName());
    if (pm.isEmbedded()) {
      EmbeddableCtTypeVisitor visitor = new EmbeddableCtTypeVisitor();
      EmbeddableCtType embeddableCtType = pm.getCtType().accept(visitor, null);
      printEmbeddedPropertyTypeField(embeddableCtType);
    } else {
      PropertyCtTypeVisitor visitor = new PropertyCtTypeVisitor();
      pm.getCtType().accept(visitor, null);
      BasicCtType basicCtType = visitor.getBasicCtType();
      LazyFormatter wrapperCode = visitor.getWrapperCode();
      LazyFormatter domainDescCode = visitor.getDomainDescCode();
      LazyFormatter domainTypeCode = visitor.getDomainTypeCode();
      if (pm.isId()) {
        if (pm.getIdGeneratorMeta() != null) {
          printGeneratedIdPropertyTypeField(
              basicCtType, wrapperCode, domainDescCode, domainTypeCode);
        } else {
          printAssignedIdPropertyTypeField(
              basicCtType, wrapperCode, domainDescCode, domainTypeCode);
        }
      } else if (pm.isVersion()) {
        printVersionPropertyTypeField(basicCtType, wrapperCode, domainDescCode, domainTypeCode);
      } else if (pm.isTenantId()) {
        printTenantIdPropertyTypeField(basicCtType, wrapperCode, domainDescCode, domainTypeCode);
      } else {
        printDefaultPropertyTypeField(basicCtType, wrapperCode, domainDescCode, domainTypeCode);
      }
    }
  }

  private void printEmbeddedPropertyTypeField(EmbeddableCtType embeddableCtType) {
    iprint(
        "public final %1$s<%2$s, %3$s> %4$s = "
            + "new %1$s<>(\"%5$s\", %2$s.class, %6$s.getEmbeddablePropertyTypes(\"%5$s\", %2$s.class, __namingType));%n",
        /* 1 */ EmbeddedPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ pm.getType(),
        /* 4 */ pm.getFieldName(),
        /* 5 */ pm.getName(),
        /* 6 */ embeddableCtType.embeddableDescSingletonCode());
  }

  private void printGeneratedIdPropertyTypeField(
      BasicCtType basicCtType,
      LazyFormatter wrapperCode,
      LazyFormatter domainDescCode,
      LazyFormatter domainTypeCode) {
    iprint(
        "public final %1$s<%11$s, %2$s, %3$s, %14$s> %12$s = "
            + "new %1$s<>(%6$s.class, %13$s.class, %3$s.class, () -> %7$s, %10$s, %8$s, \"%4$s\", \"%5$s\", __namingType, %15$s, __idGenerator);%n",
        /* 1 */ GeneratedIdPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ basicCtType.getBoxedType(),
        /* 4 */ pm.getName(),
        /* 5 */ pm.getColumnName(),
        /* 6 */ entityMeta.getType(),
        /* 7 */ wrapperCode,
        /* 8 */ domainDescCode,
        /* 9 */ NULL,
        /* 10 */ NULL,
        /* 11 */ Object.class,
        /* 12 */ pm.getFieldName(),
        /* 13 */ pm.getQualifiedName(),
        /* 14 */ domainTypeCode,
        /* 15 */ pm.isColumnQuoteRequired());
  }

  private void printAssignedIdPropertyTypeField(
      BasicCtType basicCtType,
      LazyFormatter wrapperCode,
      LazyFormatter domainDescCode,
      LazyFormatter domainTypeCode) {
    iprint(
        "public final %1$s<%11$s, %2$s, %3$s, %14$s> %12$s = "
            + "new %1$s<>(%6$s.class, %13$s.class, %3$s.class, () -> %7$s, %10$s, %8$s, \"%4$s\", \"%5$s\", __namingType, %15$s);%n",
        /* 1 */ AssignedIdPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ basicCtType.getBoxedType(),
        /* 4 */ pm.getName(),
        /* 5 */ pm.getColumnName(),
        /* 6 */ entityMeta.getType(),
        /* 7 */ wrapperCode,
        /* 8 */ domainDescCode,
        /* 9 */ NULL,
        /* 10 */ NULL,
        /* 11 */ Object.class,
        /* 12 */ pm.getFieldName(),
        /* 13 */ pm.getQualifiedName(),
        /* 14 */ domainTypeCode,
        /* 15 */ pm.isColumnQuoteRequired());
  }

  private void printVersionPropertyTypeField(
      BasicCtType basicCtType,
      LazyFormatter wrapperCode,
      LazyFormatter domainDescCode,
      LazyFormatter domainTypeCode) {
    iprint(
        "public final %1$s<%11$s, %2$s, %3$s, %14$s> %12$s = "
            + "new %1$s<>(%6$s.class,  %13$s.class, %3$s.class, () -> %7$s, %10$s, %8$s, \"%4$s\", \"%5$s\", __namingType, %15$s);%n",
        /* 1 */ VersionPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ basicCtType.getBoxedType(),
        /* 4 */ pm.getName(),
        /* 5 */ pm.getColumnName(),
        /* 6 */ entityMeta.getType(),
        /* 7 */ wrapperCode,
        /* 8 */ domainDescCode,
        /* 9 */ NULL,
        /* 10 */ NULL,
        /* 11 */ Object.class,
        /* 12 */ pm.getFieldName(),
        /* 13 */ pm.getQualifiedName(),
        /* 14 */ domainTypeCode,
        /* 15 */ pm.isColumnQuoteRequired());
  }

  private void printTenantIdPropertyTypeField(
      BasicCtType basicCtType,
      LazyFormatter wrapperCode,
      LazyFormatter domainDescCode,
      LazyFormatter domainTypeCode) {
    iprint(
        "public final %1$s<%11$s, %2$s, %3$s, %14$s> %12$s = "
            + "new %1$s<>(%6$s.class,  %13$s.class, %3$s.class, () -> %7$s, %10$s, %8$s, \"%4$s\", \"%5$s\", __namingType, %15$s);%n",
        /* 1 */ TenantIdPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ basicCtType.getBoxedType(),
        /* 4 */ pm.getName(),
        /* 5 */ pm.getColumnName(),
        /* 6 */ entityMeta.getType(),
        /* 7 */ wrapperCode,
        /* 8 */ domainDescCode,
        /* 9 */ NULL,
        /* 10 */ NULL,
        /* 11 */ Object.class,
        /* 12 */ pm.getFieldName(),
        /* 13 */ pm.getQualifiedName(),
        /* 14 */ domainTypeCode,
        /* 15 */ pm.isColumnQuoteRequired());
  }

  private void printDefaultPropertyTypeField(
      BasicCtType basicCtType,
      LazyFormatter wrapperCode,
      LazyFormatter domainDescCode,
      LazyFormatter domainTypeCode) {
    iprint(
        "public final %1$s<%13$s, %2$s, %3$s, %16$s> %14$s = "
            + "new %1$s<>(%8$s.class, %15$s.class, %3$s.class, () -> %9$s, %12$s, %10$s, \"%4$s\", \"%5$s\", __namingType, %6$s, %7$s, %17$s);%n",
        /* 1 */ DefaultPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ basicCtType.getBoxedType(),
        /* 4 */ pm.getName(),
        /* 5 */ pm.getColumnName(),
        /* 6 */ pm.isColumnInsertable(),
        /* 7 */ pm.isColumnUpdatable(),
        /* 8 */ entityMeta.getType(),
        /* 9 */ wrapperCode,
        /* 10 */ domainDescCode,
        /* 11 */ NULL,
        /* 12 */ NULL,
        /* 13 */ Object.class,
        /* 14 */ pm.getFieldName(),
        /* 15 */ pm.getQualifiedName(),
        /* 16 */ domainTypeCode,
        /* 17 */ pm.isColumnQuoteRequired());
  }

  private class EmbeddableCtTypeVisitor
      extends SimpleCtTypeVisitor<EmbeddableCtType, Void, RuntimeException> {

    @Override
    public EmbeddableCtType visitEmbeddableCtType(EmbeddableCtType ctType, Void aVoid)
        throws RuntimeException {
      return ctType;
    }
  }
}
