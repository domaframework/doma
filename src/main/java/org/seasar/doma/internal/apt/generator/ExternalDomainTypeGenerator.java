package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeParameterElement;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.TypeName;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.WrapperCtType;
import org.seasar.doma.internal.apt.meta.domain.ExternalDomainMeta;
import org.seasar.doma.jdbc.domain.AbstractDomainType;

public class ExternalDomainTypeGenerator extends AbstractGenerator {

  private final ExternalDomainMeta domainMeta;

  private final String domainTypeName;

  private final String typeParamDecl;

  private final boolean parameterized;

  public ExternalDomainTypeGenerator(
      Context ctx, TypeName typeName, Printer printer, ExternalDomainMeta domainMeta) {
    super(ctx, typeName, printer);
    assertNotNull(domainMeta);
    this.domainMeta = domainMeta;
    this.domainTypeName = typeName.getTypeName();
    this.typeParamDecl = typeName.getTypeParametersDeclaration();
    this.parameterized = !domainMeta.getDomainElement().getTypeParameters().isEmpty();
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
    if (domainMeta.getDomainElement().getTypeParameters().isEmpty()) {
      iprint("/** */%n");
    } else {
      iprint("/**%n");
      for (TypeParameterElement typeParam : domainMeta.getDomainElement().getTypeParameters()) {
        iprint(" * @param <%1$s> %1$s%n", typeParam.getSimpleName());
      }
      iprint(" */%n");
    }
    printGenerated();
    iprint(
        "public final class %1$s%5$s extends %2$s<%3$s, %4$s> {%n",
        simpleName,
        AbstractDomainType.class.getName(),
        domainMeta.getValueTypeName(),
        domainTypeName,
        typeParamDecl);
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
    iprint("private static final %1$s singleton = new %1$s();%n", simpleName);
    print("%n");
    iprint(
        "private static final %1$s converter = new %1$s();%n",
        domainMeta.getTypeElement().getQualifiedName());
    print("%n");
  }

  private void printConstructors() {
    iprint("private %1$s() {%n", simpleName);
    BasicCtType basicCtType = domainMeta.getBasicCtType();
    WrapperCtType wrapperCtType = basicCtType.getWrapperCtType();
    if (basicCtType.isEnum()) {
      iprint(
          "    super(() -> new %1$s(%2$s.class));%n",
          wrapperCtType.getTypeName(), domainMeta.getValueTypeName());
      iprint("}%n");
    } else {
      iprint("    super(() -> new %1$s());%n", wrapperCtType.getTypeName());
      iprint("}%n");
    }
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
    if (parameterized) {
      iprint("@SuppressWarnings(\"unchecked\")%n");
    }
    iprint("@Override%n");
    iprint(
        "protected %1$s newDomain(%2$s value) {%n", domainTypeName, domainMeta.getValueTypeName());
    if (parameterized) {
      iprint("    return (%1$s) converter.fromValueToDomain(value);%n", domainTypeName);
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
        domainMeta.getValueTypeName(), domainTypeName);
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
    iprint("    return %1$s.class;%n", domainMeta.getValueTypeName());
    iprint("}%n");
    print("%n");
  }

  private void printGetDomainClassMethod() {
    if (parameterized) {
      iprint("@SuppressWarnings(\"unchecked\")%n");
    }
    iprint("@Override%n");
    iprint("public Class<%1$s> getDomainClass() {%n", domainTypeName);
    if (parameterized) {
      iprint(
          "    Class<?> clazz = %1$s.class;%n", domainMeta.getDomainElement().getQualifiedName());
      iprint("    return (Class<%1$s>) clazz;%n", domainTypeName);
    } else {
      iprint("    return %1$s.class;%n", domainMeta.getDomainElement().getQualifiedName());
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
      iprint("public static %1$s %2$s%1$s getSingletonInternal() {%n", typeParamDecl, simpleName);
      iprint("    return (%2$s%1$s) singleton;%n", typeParamDecl, simpleName);
    } else {
      iprint("public static %1$s getSingletonInternal() {%n", simpleName);
      iprint("    return singleton;%n");
    }
    iprint("}%n");
    print("%n");
  }
}
