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
      Code wrapperSupplierCode = visitor.getWrapperSupplierCode();
      Code domainDescCode = visitor.getDomainDescCode();
      Code domainTypeCode = visitor.getDomainTypeCode();
      if (pm.isId()) {
        if (pm.getIdGeneratorMeta() != null) {
          printGeneratedIdPropertyTypeField(
              basicCtType, wrapperSupplierCode, domainDescCode, domainTypeCode);
        } else {
          printAssignedIdPropertyTypeField(
              basicCtType, wrapperSupplierCode, domainDescCode, domainTypeCode);
        }
      } else if (pm.isVersion()) {
        printVersionPropertyTypeField(
            basicCtType, wrapperSupplierCode, domainDescCode, domainTypeCode);
      } else if (pm.isTenantId()) {
        printTenantIdPropertyTypeField(
            basicCtType, wrapperSupplierCode, domainDescCode, domainTypeCode);
      } else {
        printDefaultPropertyTypeField(
            basicCtType, wrapperSupplierCode, domainDescCode, domainTypeCode);
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
        /* 6 */ embeddableCtType.getDescCode());
  }

  private void printGeneratedIdPropertyTypeField(
      BasicCtType basicCtType, Code wrapperSupplierCode, Code domainDescCode, Code domainTypeCode) {
    iprint(
        "public final %1$s<java.lang.Object, %2$s, %3$s, %4$s> %5$s = "
            + "new %1$s<>(%6$s.class, %7$s.class, %3$s.class, %8$s, null, %9$s, \"%10$s\", \"%11$s\", __namingType, %12$s, __idGenerator);%n",
        /* 1 */ GeneratedIdPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ basicCtType.getBoxedType(),
        /* 4 */ domainTypeCode,
        /* 5 */ pm.getFieldName(),
        /* 6 */ entityMeta.getType(),
        /* 7 */ pm.getQualifiedName(),
        /* 8 */ wrapperSupplierCode,
        /* 9 */ domainDescCode,
        /* 10 */ pm.getName(),
        /* 11 */ pm.getColumnName(),
        /* 12 */ pm.isColumnQuoteRequired());
  }

  private void printAssignedIdPropertyTypeField(
      BasicCtType basicCtType, Code wrapperSupplierCode, Code domainDescCode, Code domainTypeCode) {
    iprint(
        "public final %1$s<java.lang.Object, %2$s, %3$s, %4$s> %5$s = "
            + "new %1$s<>(%6$s.class, %7$s.class, %3$s.class, %8$s, null, %9$s, \"%10$s\", \"%11$s\", __namingType, %12$s);%n",
        /* 1 */ AssignedIdPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ basicCtType.getBoxedType(),
        /* 4 */ domainTypeCode,
        /* 5 */ pm.getFieldName(),
        /* 6 */ entityMeta.getType(),
        /* 7 */ pm.getQualifiedName(),
        /* 8 */ wrapperSupplierCode,
        /* 9 */ domainDescCode,
        /* 10 */ pm.getName(),
        /* 11 */ pm.getColumnName(),
        /* 12 */ pm.isColumnQuoteRequired());
  }

  private void printVersionPropertyTypeField(
      BasicCtType basicCtType, Code wrapperSupplierCode, Code domainDescCode, Code domainTypeCode) {
    iprint(
        "public final %1$s<java.lang.Object, %2$s, %3$s, %4$s> %5$s = "
            + "new %1$s<>(%6$s.class,  %7$s.class, %3$s.class, %8$s, null, %9$s, \"%10$s\", \"%11$s\", __namingType, %12$s);%n",
        /* 1 */ VersionPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ basicCtType.getBoxedType(),
        /* 4 */ domainTypeCode,
        /* 5 */ pm.getFieldName(),
        /* 6 */ entityMeta.getType(),
        /* 7 */ pm.getQualifiedName(),
        /* 8 */ wrapperSupplierCode,
        /* 9 */ domainDescCode,
        /* 10 */ pm.getName(),
        /* 11 */ pm.getColumnName(),
        /* 12 */ pm.isColumnQuoteRequired());
  }

  private void printTenantIdPropertyTypeField(
      BasicCtType basicCtType, Code wrapperSupplierCode, Code domainDescCode, Code domainTypeCode) {
    iprint(
        "public final %1$s<java.lang.Object, %2$s, %3$s, %4$s> %5$s = "
            + "new %1$s<>(%6$s.class,  %7$s.class, %3$s.class, %8$s, null, %9$s, \"%10$s\", \"%11$s\", __namingType, %12$s);%n",
        /* 1 */ TenantIdPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ basicCtType.getBoxedType(),
        /* 4 */ domainTypeCode,
        /* 5 */ pm.getFieldName(),
        /* 6 */ entityMeta.getType(),
        /* 7 */ pm.getQualifiedName(),
        /* 8 */ wrapperSupplierCode,
        /* 9 */ domainDescCode,
        /* 10 */ pm.getName(),
        /* 11 */ pm.getColumnName(),
        /* 12 */ pm.isColumnQuoteRequired());
  }

  private void printDefaultPropertyTypeField(
      BasicCtType basicCtType, Code wrapperSupplierCode, Code domainDescCode, Code domainTypeCode) {
    iprint(
        "public final %1$s<java.lang.Object, %2$s, %3$s, %4$s> %5$s = "
            + "new %1$s<>(%6$s.class, %7$s.class, %3$s.class, %8$s, null, %9$s, \"%10$s\", \"%11$s\", __namingType, %12$s, %13$s, %14$s);%n",
        /* 1 */ DefaultPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ basicCtType.getBoxedType(),
        /* 4 */ domainTypeCode,
        /* 5 */ pm.getFieldName(),
        /* 6 */ entityMeta.getType(),
        /* 7 */ pm.getQualifiedName(),
        /* 8 */ wrapperSupplierCode,
        /* 9 */ domainDescCode,
        /* 10 */ pm.getName(),
        /* 11 */ pm.getColumnName(),
        /* 12 */ pm.isColumnInsertable(),
        /* 13 */ pm.isColumnUpdatable(),
        /* 14 */ pm.isColumnQuoteRequired());
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
