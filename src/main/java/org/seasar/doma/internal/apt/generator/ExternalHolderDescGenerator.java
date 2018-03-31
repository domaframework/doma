package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.apt.generator.CodeHelper.wrapperSupplier;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Formatter;
import javax.lang.model.element.TypeParameterElement;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.meta.holder.ExternalHolderMeta;
import org.seasar.doma.jdbc.holder.AbstractHolderDesc;

public class ExternalHolderDescGenerator extends AbstractGenerator {

  private final ExternalHolderMeta holderMeta;

  private final String holderTypeName;

  private final boolean parameterized;

  public ExternalHolderDescGenerator(
      Context ctx, ExternalHolderMeta holderMeta, CodeSpec codeSpec, Formatter formatter) {
    super(ctx, codeSpec, formatter);
    assertNotNull(holderMeta);
    this.holderMeta = holderMeta;
    this.holderTypeName = ctx.getTypes().getTypeName(holderMeta.getHolderElement().asType());
    this.parameterized = !holderMeta.getHolderElement().getTypeParameters().isEmpty();
  }

  @Override
  public void generate() {
    printPackage();
    printClass();
  }

  private void printClass() {
    if (holderMeta.getHolderElement().getTypeParameters().isEmpty()) {
      iprint("/** */%n");
    } else {
      iprint("/**%n");
      for (TypeParameterElement typeParam : holderMeta.getHolderElement().getTypeParameters()) {
        iprint(" * @param <%1$s> %1$s%n", typeParam.getSimpleName());
      }
      iprint(" */%n");
    }
    printGenerated();
    if (parameterized) {
      iprint(
          "public final class %1$s<%5$s> extends %2$s<%3$s, %4$s> {%n",
          /* 1 */ codeSpec.getSimpleName(),
          /* 2 */ AbstractHolderDesc.class.getName(),
          /* 3 */ holderMeta.getValueTypeName(),
          /* 4 */ holderTypeName,
          /* 5 */ codeSpec.getTypeParamsName());
    } else {
      iprint(
          "public final class %1$s extends %2$s<%3$s, %4$s> {%n",
          /* 1 */ codeSpec.getSimpleName(),
          /* 2 */ AbstractHolderDesc.class.getName(),
          /* 3 */ holderMeta.getValueTypeName(),
          /* 4 */ holderTypeName);
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
    if (parameterized) {
      iprint("@SuppressWarnings(\"rawtypes\")%n");
    }
    iprint("private static final %1$s singleton = new %1$s();%n", codeSpec.getSimpleName());
    print("%n");
    iprint(
        "private static final %1$s converter = new %1$s();%n",
        holderMeta.getTypeElement().getQualifiedName());
    print("%n");
  }

  private void printConstructors() {
    iprint("private %1$s() {%n", codeSpec.getSimpleName());
    var basicCtType = holderMeta.getBasicCtType();
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
    if (parameterized) {
      iprint("@SuppressWarnings(\"unchecked\")%n");
    }
    iprint("@Override%n");
    iprint(
        "protected %1$s newHolder(%2$s value) {%n",
        /* 1 */ holderTypeName, /* 2 */ holderMeta.getValueTypeName());
    if (parameterized) {
      iprint("    return (%1$s) converter.fromValueToHolder(value);%n", holderTypeName);
    } else {
      iprint("    return converter.fromValueToHolder(value);%n");
    }
    iprint("}%n");
    print("%n");
  }

  private void printGetBasicValueMethod() {
    iprint("@Override%n");
    iprint(
        "protected %1$s getBasicValue(%2$s holder) {%n",
        /* 1 */ holderMeta.getValueTypeName(), /* 2 */ holderTypeName);
    iprint("    if (holder == null) {%n");
    iprint("        return null;%n");
    iprint("    }%n");
    iprint("    return converter.fromHolderToValue(holder);%n");
    iprint("}%n");
    print("%n");
  }

  private void printGetBasicClassMethod() {
    iprint("@Override%n");
    iprint("public Class<?> getBasicClass() {%n");
    iprint("    return %1$s.class;%n", holderMeta.getValueTypeName());
    iprint("}%n");
    print("%n");
  }

  private void printGetHolderClassMethod() {
    if (parameterized) {
      iprint("@SuppressWarnings(\"unchecked\")%n");
    }
    iprint("@Override%n");
    iprint("public Class<%1$s> getHolderClass() {%n", holderTypeName);
    if (parameterized) {
      iprint(
          "    Class<?> clazz = %1$s.class;%n", holderMeta.getHolderElement().getQualifiedName());
      iprint("    return (Class<%1$s>) clazz;%n", holderTypeName);
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
    if (parameterized) {
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
