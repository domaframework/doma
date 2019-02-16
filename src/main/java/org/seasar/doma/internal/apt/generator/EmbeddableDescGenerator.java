package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.*;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddablePropertyMeta;
import org.seasar.doma.jdbc.entity.DefaultPropertyType;
import org.seasar.doma.jdbc.entity.EmbeddableType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.Property;

public class EmbeddableDescGenerator extends AbstractGenerator {

  private final EmbeddableMeta embeddableMeta;

  public EmbeddableDescGenerator(
      Context ctx, ClassName className, Printer printer, EmbeddableMeta embeddableMeta) {
    super(ctx, className, printer);
    assertNotNull(embeddableMeta);
    this.embeddableMeta = embeddableMeta;
  }

  @Override
  public void generate() {
    printPackage();
    printClass();
  }

  private void printPackage() {
    if (!packageName.isEmpty()) {
      iprint("package %1$s;%n", packageName);
      iprint("%n");
    }
  }

  private void printClass() {
    iprint("/** */%n");
    printGenerated();
    iprint(
        "public final class %1$s implements %2$s<%3$s> {%n",
        /* 1 */ simpleName, /* 2 */ EmbeddableType.class, /* 3 */ embeddableMeta.getType());
    print("%n");
    indent();
    printValidateVersionStaticInitializer();
    printFields();
    printMethods();
    unindent();
    iprint("}%n");
  }

  private void printFields() {
    printSingletonField();
  }

  private void printSingletonField() {
    iprint("private static final %1$s __singleton = new %1$s();%n", simpleName);
    print("%n");
  }

  private void printMethods() {
    printGetEmbeddablePropertyTypesMethod();
    printNewEmbeddableMethod();
    printGetSingletonInternalMethod();
  }

  private void printGetEmbeddablePropertyTypesMethod() {
    iprint("@Override%n");
    iprint(
        "public <ENTITY> %1$s<%2$s<ENTITY, ?>> getEmbeddablePropertyTypes"
            + "(String embeddedPropertyName, Class<ENTITY> entityClass, %3$s namingType) {%n",
        List.class, EntityPropertyType.class, NamingType.class);
    iprint("    return %1$s.asList(%n", Arrays.class);
    for (Iterator<EmbeddablePropertyMeta> it =
            embeddableMeta.getEmbeddablePropertyMetas().iterator();
        it.hasNext(); ) {
      EmbeddablePropertyMeta pm = it.next();
      PropertyCtTypeVisitor visitor = new PropertyCtTypeVisitor();
      pm.getCtType().accept(visitor, null);
      iprint(
          "        new %1$s<Object, ENTITY, %2$s, %10$s>(entityClass, %9$s.class, %2$s.class, "
              + "%7$s, null, %8$s, embeddedPropertyName + \".%3$s\", \"%4$s\", namingType, %5$s, %6$s, %11$s)",
          /* 1 */ DefaultPropertyType.class,
          /* 2 */ visitor.getBasicCtType().getBoxedType(),
          /* 3 */ pm.getName(),
          /* 4 */ pm.getColumnName(),
          /* 5 */ pm.isColumnInsertable(),
          /* 6 */ pm.isColumnUpdatable(),
          /* 7 */ visitor.getWrapperSupplierCode(),
          /* 8 */ visitor.getDomainDescCode(),
          /* 9 */ pm.getQualifiedName(),
          /* 10 */ visitor.getDomainTypeCode(),
          /* 11 */ pm.isColumnQuoteRequired());
      print(it.hasNext() ? ",%n" : "");
    }
    print(");%n");
    iprint("}%n");
    print("%n");
  }

  private void printNewEmbeddableMethod() {
    iprint("@Override%n");
    iprint(
        "public <ENTITY> %1$s newEmbeddable(String embeddedPropertyName, %2$s<String, %3$s<ENTITY, ?>> __args) {%n",
        embeddableMeta.getType(), Map.class, Property.class);
    if (embeddableMeta.isAbstract()) {
      iprint("    return null;%n");
    } else {
      iprint("    return new %1$s(%n", embeddableMeta.getType());
      for (Iterator<EmbeddablePropertyMeta> it =
              embeddableMeta.getEmbeddablePropertyMetas().iterator();
          it.hasNext(); ) {
        EmbeddablePropertyMeta propertyMeta = it.next();
        iprint(
            "        (%1$s)(__args.get(embeddedPropertyName + \".%2$s\") "
                + "!= null ? __args.get(embeddedPropertyName + \".%2$s\").get() : null)",
            propertyMeta.getBoxedType(), propertyMeta.getName());
        if (it.hasNext()) {
          print(",%n");
        }
      }
      print(");%n");
    }
    iprint("}%n");
    print("%n");
  }

  private void printGetSingletonInternalMethod() {
    iprint("/**%n");
    iprint(" * @return the singleton%n");
    iprint(" */%n");
    iprint("public static %1$s getSingletonInternal() {%n", simpleName);
    iprint("    return __singleton;%n");
    iprint("}%n");
    print("%n");
  }
}
