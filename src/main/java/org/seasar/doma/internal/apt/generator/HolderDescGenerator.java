package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.apt.generator.CodeHelper.wrapperSupplier;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.meta.holder.HolderMeta;
import org.seasar.doma.internal.util.BoxedPrimitiveUtil;
import org.seasar.doma.jdbc.holder.AbstractHolderDesc;

public class HolderDescGenerator implements Generator {

  private final CodeSpec codeSpec;

  private final Printer printer;

  private final HolderMeta holderMeta;

  private final Context ctx;

  private final String typeName;

  public HolderDescGenerator(
      CodeSpec codeSpec, Printer printer, HolderMeta holderMeta, Context ctx) {
    assertNotNull(codeSpec, printer, holderMeta, ctx);
    this.ctx = ctx;
    this.holderMeta = holderMeta;
    this.codeSpec = codeSpec;
    this.printer = printer;
    this.typeName = ctx.getTypes().getTypeName(holderMeta.getType());
  }

  @Override
  public void generate() {
    printClass();
  }

  private void printClass() {
    printer.printPackage();
    var typeElement = holderMeta.getHolderElement();
    if (typeElement.getTypeParameters().isEmpty()) {
      printer.iprint("/** */%n");
    } else {
      printer.iprint("/**%n");
      for (var typeParam : typeElement.getTypeParameters()) {
        printer.iprint(" * @param <%1$s> %1$s%n", typeParam.getSimpleName());
      }
      printer.iprint(" */%n");
    }
    printer.printGenerated();
    if (holderMeta.isParameterized()) {
      printer.iprint(
          "public final class %1$s<%5$s> extends %2$s<%3$s, %4$s> {%n",
          /* 1 */ codeSpec.getSimpleName(),
          /* 2 */ AbstractHolderDesc.class.getName(),
          /* 3 */ ctx.getTypes().boxIfPrimitive(holderMeta.getValueType()),
          /* 4 */ typeName,
          /* 5 */ codeSpec.getTypeParamsName());
    } else {
      printer.iprint(
          "public final class %1$s extends %2$s<%3$s, %4$s> {%n",
          /* 1 */ codeSpec.getSimpleName(),
          /* 2 */ AbstractHolderDesc.class.getName(),
          /* 3 */ ctx.getTypes().boxIfPrimitive(holderMeta.getValueType()),
          /* 4 */ typeName);
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
    if (holderMeta.isParameterized()) {
      printer.iprint("@SuppressWarnings(\"rawtypes\")%n");
    }
    printer.iprint("private static final %1$s singleton = new %1$s();%n", codeSpec.getSimpleName());
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
    var primitive = holderMeta.getBasicCtType().isPrimitive();
    printer.iprint("@Override%n");
    printer.iprint(
        "protected %1$s newHolder(%2$s value) {%n",
        /* 1 */ typeName, /* 2 */ ctx.getTypes().boxIfPrimitive(holderMeta.getValueType()));
    if (!primitive && !holderMeta.getAcceptNull()) {
      printer.iprint("    if (value == null) {%n");
      printer.iprint("        return null;%n");
      printer.iprint("    }%n");
    }
    if (holderMeta.providesConstructor()) {
      if (primitive) {
        printer.iprint(
            "    return new %1$s(%2$s.unbox(value));%n",
            /* 1 */ typeName, /* 2 */ BoxedPrimitiveUtil.class.getName());
      } else {
        printer.iprint("    return new %1$s(value);%n", /* 1 */ typeName);
      }
    } else {
      if (primitive) {
        printer.iprint(
            "    return %1$s.%2$s(%3$s.unbox(value));%n",
            /* 1 */ holderMeta.getHolderElement().getQualifiedName(),
            /* 2 */ holderMeta.getFactoryMethod(),
            /* 3 */ BoxedPrimitiveUtil.class.getName());
      } else {
        printer.iprint(
            "    return %1$s.%2$s(value);%n",
            /* 1 */ holderMeta.getHolderElement().getQualifiedName(),
            /* 2 */ holderMeta.getFactoryMethod());
      }
    }
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetBasicValueMethod() {
    printer.iprint("@Override%n");
    printer.iprint(
        "protected %1$s getBasicValue(%2$s holder) {%n",
        /* 1 */ ctx.getTypes().boxIfPrimitive(holderMeta.getValueType()), /* 2 */ typeName);
    printer.iprint("    if (holder == null) {%n");
    printer.iprint("        return null;%n");
    printer.iprint("    }%n");
    printer.iprint("    return holder.%1$s();%n", holderMeta.getAccessorMethod());
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetBasicClassMethod() {
    printer.iprint("@Override%n");
    printer.iprint("public Class<?> getBasicClass() {%n");
    printer.iprint("    return %1$s.class;%n", holderMeta.getValueType());
    printer.iprint("}%n");
    printer.print("%n");
  }

  private void printGetHolderClassMethod() {
    if (holderMeta.isParameterized()) {
      printer.iprint("@SuppressWarnings(\"unchecked\")%n");
    }
    printer.iprint("@Override%n");
    printer.iprint("public Class<%1$s> getHolderClass() {%n", typeName);
    if (holderMeta.isParameterized()) {
      printer.iprint(
          "    Class<?> clazz = %1$s.class;%n", holderMeta.getHolderElement().getQualifiedName());
      printer.iprint("    return (Class<%1$s>) clazz;%n", typeName);
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
    if (holderMeta.isParameterized()) {
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
