package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.meta.domain.ExternalDomainMeta;
import org.seasar.doma.jdbc.domain.AbstractDomainType;

public class ExternalDomainDescGenerator extends AbstractGenerator {

  private final ExternalDomainMeta domainMeta;

  public ExternalDomainDescGenerator(
      Context ctx, ClassName className, Printer printer, ExternalDomainMeta domainMeta) {
    super(ctx, className, printer);
    assertNotNull(domainMeta);
    this.domainMeta = domainMeta;
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
    if (domainMeta.isParameterized()) {
      iprint("/**%n");
      for (String typeVariable : domainMeta.getTypeVariables()) {
        iprint(" * @param <%1$s> %1$s%n", typeVariable);
      }
      iprint(" */%n");
    } else {
      iprint("/** */%n");
    }
    printGenerated();
    if (domainMeta.isParameterized()) {
      iprint(
          "public final class %1$s<%5$s> extends %2$s<%3$s, %4$s> {%n",
          /* 1 */ simpleName,
          /* 2 */ AbstractDomainType.class,
          /* 3 */ domainMeta.getValueType(),
          /* 4 */ domainMeta.asType(),
          /* 5 */ domainMeta.getTypeParameters());
    } else {
      iprint(
          "public final class %1$s extends %2$s<%3$s, %4$s> {%n",
          /* 1 */ simpleName,
          /* 2 */ AbstractDomainType.class,
          /* 3 */ domainMeta.getValueType(),
          /* 4 */ domainMeta.asType());
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
    if (domainMeta.isParameterized()) {
      iprint("@SuppressWarnings(\"rawtypes\")%n");
    }
    iprint("private static final %1$s singleton = new %1$s();%n", simpleName);
    print("%n");
    iprint("private static final %1$s converter = new %1$s();%n", domainMeta.getConverterElement());
    print("%n");
  }

  private void printConstructors() {
    BasicCtType basicCtType = domainMeta.getBasicCtType();
    iprint("private %1$s() {%n", simpleName);
    iprint("    super(%1$s);%n", basicCtType.getWrapperSupplierCode());
    iprint("}%n");
    print("%n");
  }

  private void printMethods() {
    printNewDomainMethod();
    printGetBasicValueMethod();
    printGetBasicClassMethod();
    printGetDomainClassMethod();
    printGetSingletonInternalMethod();
  }

  private void printNewDomainMethod() {
    if (domainMeta.isParameterized()) {
      iprint("@SuppressWarnings(\"unchecked\")%n");
    }
    iprint("@Override%n");
    iprint(
        "protected %1$s newDomain(%2$s value) {%n", domainMeta.asType(), domainMeta.getValueType());
    if (domainMeta.isParameterized()) {
      iprint("    return (%1$s) converter.fromValueToDomain(value);%n", domainMeta.asType());
    } else {
      iprint("    return converter.fromValueToDomain(value);%n");
    }
    iprint("}%n");
    print("%n");
  }

  private void printGetBasicValueMethod() {
    iprint("@Override%n");
    iprint(
        "protected %1$s getBasicValue(%2$s domain) {%n",
        domainMeta.getValueType(), domainMeta.asType());
    iprint("    if (domain == null) {%n");
    iprint("        return null;%n");
    iprint("    }%n");
    iprint("    return converter.fromDomainToValue(domain);%n");
    iprint("}%n");
    print("%n");
  }

  private void printGetBasicClassMethod() {
    iprint("@Override%n");
    iprint("public Class<?> getBasicClass() {%n");
    iprint("    return %1$s.class;%n", domainMeta.getValueType());
    iprint("}%n");
    print("%n");
  }

  private void printGetDomainClassMethod() {
    if (domainMeta.isParameterized()) {
      iprint("@SuppressWarnings(\"unchecked\")%n");
    }
    iprint("@Override%n");
    iprint("public Class<%1$s> getDomainClass() {%n", domainMeta.asType());
    if (domainMeta.isParameterized()) {
      iprint("    Class<?> clazz = %1$s.class;%n", domainMeta.getTypeElement());
      iprint("    return (Class<%1$s>) clazz;%n", domainMeta.asType());
    } else {
      if (domainMeta.getTypeElement() != null) {
        iprint("    return %1$s.class;%n", domainMeta.getTypeElement());
      } else {
        iprint("    return %1$s.class;%n", domainMeta.asType());
      }
    }
    iprint("}%n");
    print("%n");
  }

  private void printGetSingletonInternalMethod() {
    iprint("/**%n");
    iprint(" * @return the singleton%n");
    iprint(" */%n");
    if (domainMeta.isParameterized()) {
      iprint("@SuppressWarnings(\"unchecked\")%n");
      iprint(
          "public static <%1$s> %2$s<%3$s> getSingletonInternal() {%n",
          /* 1 */ domainMeta.getTypeParameters(),
          /* 2 */ simpleName,
          /* 3 */ domainMeta.getTypeVariables());
      iprint("    return (%1$s<%2$s>) singleton;%n", simpleName, domainMeta.getTypeVariables());
    } else {
      iprint("public static %1$s getSingletonInternal() {%n", simpleName);
      iprint("    return singleton;%n");
    }
    iprint("}%n");
    print("%n");
  }
}
