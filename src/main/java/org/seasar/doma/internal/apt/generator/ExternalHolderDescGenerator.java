package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.apt.generator.CodeHelper.wrapperSupplier;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeParameterElement;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.meta.holder.ExternalHolderMeta;
import org.seasar.doma.jdbc.holder.AbstractHolderDesc;

public class ExternalHolderDescGenerator implements Generator {

  private final CodeSpec codeSpec;

  private final Printer printer;

  private final ExternalHolderMeta holderMeta;

  private final String holderTypeName;

  private final boolean parameterized;

  public ExternalHolderDescGenerator(
      CodeSpec codeSpec, Printer printer, ExternalHolderMeta holderMeta, Context ctx) {
    assertNotNull(codeSpec, printer, holderMeta, ctx);
    this.codeSpec = codeSpec;
    this.printer = printer;
    this.holderMeta = holderMeta;
    this.holderTypeName = ctx.getTypes().getTypeName(holderMeta.getHolderElement().asType());
    this.parameterized = !holderMeta.getHolderElement().getTypeParameters().isEmpty();
  }

  @Override
  public void generate() {
    printClass();
  }

  private void printClass() {
    printer.printPackage();
    if (holderMeta.getHolderElement().getTypeParameters().isEmpty()) {
      printer.iprint("/** */%n");
    } else {
      printer.iprint("/**%n");
      for (TypeParameterElement typeParam : holderMeta.getHolderElement().getTypeParameters()) {
        printer.iprint(" * @param <%1$s> %1$s%n", typeParam.getSimpleName());
      }
      printer.iprint(" */%n");
    }
    printer.printGenerated();
    if (parameterized) {
      printer.iprint(
          "public final class %1$s<%5$s> extends %2$s<%3$s, %4$s> {%n",
          /* 1 */ codeSpec.getSimpleName(),
          /* 2 */ AbstractHolderDesc.class.getName(),
          /* 3 */ holderMeta.getValueTypeName(),
          /* 4 */ holderTypeName,
          /* 5 */ codeSpec.getTypeParamsName());
    } else {
      printer.iprint(
          "public final class %1$s extends %2$s<%3$s, %4$s> {%n",
          /* 1 */ codeSpec.getSimpleName(),
          /* 2 */ AbstractHolderDesc.class.getName(),
          /* 3 */ holderMeta.getValueTypeName(),
          /* 4 */ holderTypeName);
    }
    printer.print("%n");
    printer.indent();
    printer.printValidateVersionStaticInitializer();
    printFields();
    printConstructors();
    printMethods();
    printer.unindent();
    printer.unindent();
    printer.iprint("}%n");
  }

  private void printFields() {
    if (parameterized) {
      printer.iprint("@SuppressWarnings(\"rawtypes\")%n");
    }
    printer.iprint("private static final %1$s singleton = new %1$s();%n", codeSpec.getSimpleName());
    printer.print("%n");
    printer.iprint(
        "private static final %1$s converter = new %1$s();%n",
        holderMeta.getTypeElement().getQualifiedName());
    printer.print("%n");
  }

  private void printConstructors() {
    printer.iprint("private %1$s() {%n", codeSpec.getSimpleName());
    var basicCtType = holderMeta.getBasicCtType();
    printer.iprint("    super(%1$s);%n", wrapperSupplier(basicCtType));
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printMethods() {
    printNewHolderMethod();
    printGetBasicValueMethod();
    printGetBasicClassMethod();
    printGetHolderClassMethod();
    printGetSingletonInternalMethod();
  }

  private void printNewHolderMethod() {
    if (parameterized) {
      printer.iprint("@SuppressWarnings(\"unchecked\")%n");
    }
    printer.iprint("@Override%n");
    printer.iprint(
        "protected %1$s newHolder(%2$s value) {%n",
        /* 1 */ holderTypeName, /* 2 */ holderMeta.getValueTypeName());
    if (parameterized) {
      printer.iprint("    return (%1$s) converter.fromValueToHolder(value);%n", holderTypeName);
    } else {
      printer.iprint("    return converter.fromValueToHolder(value);%n");
    }
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetBasicValueMethod() {
    printer.iprint("@Override%n");
    printer.iprint(
        "protected %1$s getBasicValue(%2$s holder) {%n",
        /* 1 */ holderMeta.getValueTypeName(), /* 2 */ holderTypeName);
    printer.iprint("    if (holder == null) {%n");
    printer.iprint("        return null;%n");
    printer.iprint("    }%n");
    printer.iprint("    return converter.fromHolderToValue(holder);%n");
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetBasicClassMethod() {
    printer.iprint("@Override%n");
    printer.iprint("public Class<?> getBasicClass() {%n");
    printer.iprint("    return %1$s.class;%n", holderMeta.getValueTypeName());
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetHolderClassMethod() {
    if (parameterized) {
      printer.iprint("@SuppressWarnings(\"unchecked\")%n");
    }
    printer.iprint("@Override%n");
    printer.iprint("public Class<%1$s> getHolderClass() {%n", holderTypeName);
    if (parameterized) {
      printer.iprint(
          "    Class<?> clazz = %1$s.class;%n", holderMeta.getHolderElement().getQualifiedName());
      printer.iprint("    return (Class<%1$s>) clazz;%n", holderTypeName);
    } else {
      printer.iprint("    return %1$s.class;%n", holderMeta.getHolderElement().getQualifiedName());
    }
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetSingletonInternalMethod() {
    printer.iprint("/**%n");
    printer.iprint(" * @return the singleton%n");
    printer.iprint(" */%n");
    if (parameterized) {
      printer.iprint("@SuppressWarnings(\"unchecked\")%n");
      printer.iprint(
          "public static <%1$s> %2$s<%1$s> getSingletonInternal() {%n",
          /* 1 */ codeSpec.getTypeParamsName(), /* 2 */ codeSpec.getSimpleName());
      printer.iprint(
          "    return (%2$s<%1$s>) singleton;%n",
          /* 1 */ codeSpec.getTypeParamsName(), /* 2 */ codeSpec.getSimpleName());
    } else {
      printer.iprint("public static %1$s getSingletonInternal() {%n", codeSpec.getSimpleName());
      printer.iprint("    return singleton;%n");
    }
    printer.iprint("}%n");
    printer.print("%n");
  }
}
