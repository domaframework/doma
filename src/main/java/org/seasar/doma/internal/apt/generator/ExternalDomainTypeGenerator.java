package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.meta.domain.ExternalDomainMeta;
import org.seasar.doma.internal.apt.util.MetaUtil;
import org.seasar.doma.jdbc.domain.AbstractDomainType;

public class ExternalDomainTypeGenerator extends AbstractGenerator {

  protected final ExternalDomainMeta domainMeta;

  protected final String domainTypeName;

  protected final String simpleMetaClassName;

  protected final String typeParamDecl;

  protected final boolean parametarized;

  public ExternalDomainTypeGenerator(
      Context ctx, TypeElement typeElement, ExternalDomainMeta domainMeta) throws IOException {
    super(
        ctx,
        typeElement,
        Constants.EXTERNAL_DOMAIN_METATYPE_ROOT_PACKAGE
            + "."
            + ctx.getElements().getPackageName(typeElement),
        null,
        Constants.METATYPE_PREFIX,
        "");
    assertNotNull(domainMeta);
    this.domainMeta = domainMeta;
    this.domainTypeName = ctx.getTypes().getTypeName(domainMeta.getDomainElement().asType());
    this.simpleMetaClassName = MetaUtil.toSimpleMetaName(typeElement, ctx);
    this.typeParamDecl = makeTypeParamDecl(domainTypeName);
    this.parametarized = !domainMeta.getDomainElement().getTypeParameters().isEmpty();
  }

  private String makeTypeParamDecl(String typeName) {
    int pos = typeName.indexOf("<");
    if (pos == -1) {
      return "";
    }
    return typeName.substring(pos);
  }

  @Override
  public void generate() {
    printPackage();
    printClass();
  }

  protected void printPackage() {
    if (!packageName.isEmpty()) {
      iprint("package %1$s;%n", packageName);
      iprint("%n");
    }
  }

  protected void printClass() {
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
        simpleMetaClassName,
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

  protected void printFields() {
    if (parametarized) {
      iprint("@SuppressWarnings(\"rawtypes\")%n");
    }
    iprint("private static final %1$s singleton = new %1$s();%n", simpleName);
    print("%n");
    iprint(
        "private static final %1$s converter = new %1$s();%n",
        domainMeta.getTypeElement().getQualifiedName());
    print("%n");
  }

  protected void printConstructors() {
    iprint("private %1$s() {%n", simpleName);
    if (domainMeta.getWrapperCtType().getBasicCtType().isEnum()) {
      iprint(
          "    super(() -> new %1$s(%2$s.class));%n",
          domainMeta.getWrapperCtType().getTypeName(), domainMeta.getValueTypeName());
      iprint("}%n");
    } else {
      iprint("    super(() -> new %1$s());%n", domainMeta.getWrapperCtType().getTypeName());
      iprint("}%n");
    }
    print("%n");
  }

  protected void printMethods() {
    printNewDomainMethod();
    printGetBasicValueMethod();
    printGetBasicClassMethod();
    printGetDomainClassMethod();
    printGetSingletonInternalMethod();
  }

  protected void printNewDomainMethod() {
    if (parametarized) {
      iprint("@SuppressWarnings(\"unchecked\")%n");
    }
    iprint("@Override%n");
    iprint(
        "protected %1$s newDomain(%2$s value) {%n", domainTypeName, domainMeta.getValueTypeName());
    if (parametarized) {
      iprint("    return (%1$s) converter.fromValueToDomain(value);%n", domainTypeName);
    } else {
      iprint("    return converter.fromValueToDomain(value);%n");
    }
    iprint("}%n");
    print("%n");
  }

  protected void printGetBasicValueMethod() {
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

  protected void printGetBasicClassMethod() {
    iprint("@Override%n");
    iprint("public Class<?> getBasicClass() {%n");
    iprint("    return %1$s.class;%n", domainMeta.getValueTypeName());
    iprint("}%n");
    print("%n");
  }

  protected void printGetDomainClassMethod() {
    if (parametarized) {
      iprint("@SuppressWarnings(\"unchecked\")%n");
    }
    iprint("@Override%n");
    iprint("public Class<%1$s> getDomainClass() {%n", domainTypeName);
    if (parametarized) {
      iprint(
          "    Class<?> clazz = %1$s.class;%n", domainMeta.getDomainElement().getQualifiedName());
      iprint("    return (Class<%1$s>) clazz;%n", domainTypeName);
    } else {
      iprint("    return %1$s.class;%n", domainMeta.getDomainElement().getQualifiedName());
    }
    iprint("}%n");
    print("%n");
  }

  protected void printGetSingletonInternalMethod() {
    iprint("/**%n");
    iprint(" * @return the singleton%n");
    iprint(" */%n");
    if (parametarized) {
      iprint("@SuppressWarnings(\"unchecked\")%n");
      iprint(
          "public static %1$s %2$s%1$s getSingletonInternal() {%n",
          typeParamDecl, simpleMetaClassName);
      iprint("    return (%2$s%1$s) singleton;%n", typeParamDecl, simpleMetaClassName);
    } else {
      iprint("public static %1$s getSingletonInternal() {%n", simpleMetaClassName);
      iprint("    return singleton;%n");
    }
    iprint("}%n");
    print("%n");
  }
}