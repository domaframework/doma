package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.apt.generator.CodeHelper.wrapperSupplier;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Formatter;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.meta.holder.HolderMeta;
import org.seasar.doma.internal.util.BoxedPrimitiveUtil;
import org.seasar.doma.jdbc.holder.AbstractHolderDesc;

public class HolderDescGenerator extends AbstractGenerator {

  private final HolderMeta holderMeta;

  private final String typeName;

  public HolderDescGenerator(
      Context ctx, HolderMeta holderMeta, CodeSpec codeSpec, Formatter formatter) {
    super(ctx, codeSpec, formatter);
    assertNotNull(holderMeta);
    this.holderMeta = holderMeta;
    this.typeName = ctx.getTypes().getTypeName(holderMeta.getType());
  }

  @Override
  public void generate() {
    printPackage();
    printClass();
  }

  private void printClass() {
    var typeElement = holderMeta.getHolderElement();
    if (typeElement.getTypeParameters().isEmpty()) {
      iprint("/** */%n");
    } else {
      iprint("/**%n");
      for (var typeParam : typeElement.getTypeParameters()) {
        iprint(" * @param <%1$s> %1$s%n", typeParam.getSimpleName());
      }
      iprint(" */%n");
    }
    printGenerated();
    if (holderMeta.isParameterized()) {
      iprint(
          "public final class %1$s<%5$s> extends %2$s<%3$s, %4$s> {%n",
          /* 1 */ codeSpec.getSimpleName(),
          /* 2 */ AbstractHolderDesc.class.getName(),
          /* 3 */ ctx.getTypes().boxIfPrimitive(holderMeta.getValueType()),
          /* 4 */ typeName,
          /* 5 */ codeSpec.getTypeParamsName());
    } else {
      iprint(
          "public final class %1$s extends %2$s<%3$s, %4$s> {%n",
          /* 1 */ codeSpec.getSimpleName(),
          /* 2 */ AbstractHolderDesc.class.getName(),
          /* 3 */ ctx.getTypes().boxIfPrimitive(holderMeta.getValueType()),
          /* 4 */ typeName);
    }
    print("%n");
    indent();
    printValidateVersionStaticInitializer();
    printFields();
    printConstructors();
    printMethods();
    unindent();
    unindent();
    iprint("}%n");
  }

  private void printFields() {
    if (holderMeta.isParameterized()) {
      iprint("@SuppressWarnings(\"rawtypes\")%n");
    }
    iprint("private static final %1$s singleton = new %1$s();%n", codeSpec.getSimpleName());
    print("%n");
  }

  private void printConstructors() {
    iprint("private %1$s() {%n", codeSpec.getSimpleName());
    BasicCtType basicCtType = holderMeta.getBasicCtType();
    iprint("    super(%1$s);%n", wrapperSupplier(basicCtType));
    iprint("}%n");
    print("%n");
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
    iprint("@Override%n");
    iprint(
        "protected %1$s newHolder(%2$s value) {%n",
        /* 1 */ typeName, /* 2 */ ctx.getTypes().boxIfPrimitive(holderMeta.getValueType()));
    if (!primitive && !holderMeta.getAcceptNull()) {
      iprint("    if (value == null) {%n");
      iprint("        return null;%n");
      iprint("    }%n");
    }
    if (holderMeta.providesConstructor()) {
      if (primitive) {
        iprint(
            "    return new %1$s(%2$s.unbox(value));%n",
            /* 1 */ typeName, /* 2 */ BoxedPrimitiveUtil.class.getName());
      } else {
        iprint("    return new %1$s(value);%n", /* 1 */ typeName);
      }
    } else {
      if (primitive) {
        iprint(
            "    return %1$s.%2$s(%3$s.unbox(value));%n",
            /* 1 */ holderMeta.getHolderElement().getQualifiedName(),
            /* 2 */ holderMeta.getFactoryMethod(),
            /* 3 */ BoxedPrimitiveUtil.class.getName());
      } else {
        iprint(
            "    return %1$s.%2$s(value);%n",
            /* 1 */ holderMeta.getHolderElement().getQualifiedName(),
            /* 2 */ holderMeta.getFactoryMethod());
      }
    }
    iprint("}%n");
    print("%n");
  }

  private void printGetBasicValueMethod() {
    iprint("@Override%n");
    iprint(
        "protected %1$s getBasicValue(%2$s holder) {%n",
        /* 1 */ ctx.getTypes().boxIfPrimitive(holderMeta.getValueType()), /* 2 */ typeName);
    iprint("    if (holder == null) {%n");
    iprint("        return null;%n");
    iprint("    }%n");
    iprint("    return holder.%1$s();%n", holderMeta.getAccessorMethod());
    iprint("}%n");
    print("%n");
  }

  private void printGetBasicClassMethod() {
    iprint("@Override%n");
    iprint("public Class<?> getBasicClass() {%n");
    iprint("    return %1$s.class;%n", holderMeta.getValueType());
    iprint("}%n");
    print("%n");
  }

  private void printGetHolderClassMethod() {
    if (holderMeta.isParameterized()) {
      iprint("@SuppressWarnings(\"unchecked\")%n");
    }
    iprint("@Override%n");
    iprint("public Class<%1$s> getHolderClass() {%n", typeName);
    if (holderMeta.isParameterized()) {
      iprint(
          "    Class<?> clazz = %1$s.class;%n", holderMeta.getHolderElement().getQualifiedName());
      iprint("    return (Class<%1$s>) clazz;%n", typeName);
    } else {
      iprint("    return %1$s.class;%n", holderMeta.getHolderElement().getQualifiedName());
    }
    iprint("}%n");
    print("%n");
  }

  private void printGetSingletonInternalMethod() {
    iprint("/**%n");
    iprint(" * @return the singleton%n");
    iprint(" */%n");
    if (holderMeta.isParameterized()) {
      iprint("@SuppressWarnings(\"unchecked\")%n");
      iprint(
          "public static <%1$s> %2$s<%1$s> getSingletonInternal() {%n",
          /* 1 */ codeSpec.getTypeParamsName(), /* 2 */ codeSpec.getSimpleName());
      iprint(
          "    return (%2$s<%1$s>) singleton;%n",
          /* 1 */ codeSpec.getTypeParamsName(), /* 2 */ codeSpec.getSimpleName());
    } else {
      iprint("public static %1$s getSingletonInternal() {%n", codeSpec.getSimpleName());
      iprint("    return singleton;%n");
    }
    iprint("}%n");
    print("%n");
  }
}
