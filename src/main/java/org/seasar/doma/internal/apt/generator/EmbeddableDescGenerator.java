package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.apt.generator.CodeHelper.box;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableMeta;
import org.seasar.doma.jdbc.entity.DefaultPropertyDesc;
import org.seasar.doma.jdbc.entity.EmbeddableDesc;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.Property;

public class EmbeddableDescGenerator implements Generator {

  private final CodeSpec codeSpec;

  private final Printer printer;

  private final EmbeddableMeta embeddableMeta;

  public EmbeddableDescGenerator(
      CodeSpec codeSpec, Printer printer, EmbeddableMeta embeddableMeta) {
    assertNotNull(codeSpec, printer, embeddableMeta);
    this.codeSpec = codeSpec;
    this.printer = printer;
    this.embeddableMeta = embeddableMeta;
  }

  @Override
  public void generate() {
    printClass();
  }

  private void printClass() {
    printer.printPackage();
    printer.iprint("/** */%n");
    printer.printGenerated();
    printer.iprint(
        "public final class %1$s implements %2$s<%3$s> {%n",
        /* 1 */ codeSpec.getSimpleName(),
        /* 2 */ EmbeddableDesc.class.getName(),
        /* 3 */ embeddableMeta.getEmbeddableElement().getQualifiedName());
    printer.print("%n");
    printer.indent();
    printer.printValidateVersionStaticInitializer();
    printFields();
    printMethods();
    printer.unindent();
    printer.iprint("}%n");
  }

  private void printFields() {
    printSingletonField();
  }

  private void printSingletonField() {
    printer.iprint(
        "private static final %1$s __singleton = new %1$s();%n", codeSpec.getSimpleName());
    printer.print("%n");
  }

  private void printMethods() {
    printGetEmbeddablePropertyDescsMethod();
    printNewEmbeddableMethod();
    printGetSingletonInternalMethod();
  }

  private void printGetEmbeddablePropertyDescsMethod() {
    printer.iprint("@Override%n");
    printer.iprint(
        "public <ENTITY> %1$s<%2$s<ENTITY, ?>> getEmbeddablePropertyDescs"
            + "(String embeddedPropertyName, Class<ENTITY> entityClass, %3$s namingType) {%n",
        /* 1 */ List.class.getName(),
        /* 2 */ EntityPropertyDesc.class.getName(),
        /* 3 */ NamingType.class.getName());
    printer.iprint("    return %1$s.asList(%n", Arrays.class.getName());
    for (var it = embeddableMeta.getEmbeddablePropertyMetas().iterator(); it.hasNext(); ) {
      var pm = it.next();
      printer.iprint(
          "        new %1$s<ENTITY, %2$s, %3$s>(entityClass, %4$s, "
              + "embeddedPropertyName + \".%5$s\", \"%6$s\", namingType, %7$s, %8$s, %9$s)",
          /* 1 */ DefaultPropertyDesc.class.getName(),
          /* 2 */ pm.getCtType().accept(new BasicTypeArgCodeBuilder(), null),
          /* 3 */ pm.getCtType().accept(new ContainerTypeArgCodeBuilder(), false),
          /* 4 */ pm.getCtType().accept(new ScalarSupplierCodeBuilder(), false),
          /* 5 */ pm.getName(),
          /* 6 */ pm.getColumnName(),
          /* 7 */ pm.isColumnInsertable(),
          /* 8 */ pm.isColumnUpdatable(),
          /* 9 */ pm.isColumnQuoteRequired());
      printer.print(it.hasNext() ? ",%n" : "");
    }
    printer.print(");%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printNewEmbeddableMethod() {
    printer.iprint("@Override%n");
    printer.iprint(
        "public <ENTITY> %1$s newEmbeddable(String embeddedPropertyName, %2$s<String, %3$s<ENTITY, ?>> __args) {%n",
        /* 1 */ embeddableMeta.getEmbeddableElement().getQualifiedName(),
        /* 2 */ Map.class.getName(),
        /* 3 */ Property.class.getName());
    if (embeddableMeta.isAbstract()) {
      printer.iprint("    return null;%n");
    } else {
      printer.iprint(
          "    return new %1$s(%n", embeddableMeta.getEmbeddableElement().getQualifiedName());
      for (var it = embeddableMeta.getEmbeddablePropertyMetas().iterator(); it.hasNext(); ) {
        var propertyMeta = it.next();
        printer.iprint(
            "        (%1$s)(__args.get(embeddedPropertyName + \".%2$s\") != null "
                + "? __args.get(embeddedPropertyName + \".%2$s\").get() : null)",
            /* 1 */ box(propertyMeta.getTypeName()), /* 2 */ propertyMeta.getName());
        if (it.hasNext()) {
          printer.print(",%n");
        }
      }
      printer.print(");%n");
    }
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetSingletonInternalMethod() {
    printer.iprint("/**%n");
    printer.iprint(" * @return the singleton%n");
    printer.iprint(" */%n");
    printer.iprint("public static %1$s getSingletonInternal() {%n", codeSpec.getSimpleName());
    printer.iprint("    return __singleton;%n");
    printer.iprint("}%n");
    printer.print("%n");
  }
}
