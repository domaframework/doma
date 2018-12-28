/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.meta.DomainMeta;
import org.seasar.doma.internal.apt.util.MetaUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.util.BoxedPrimitiveUtil;
import org.seasar.doma.jdbc.domain.AbstractDomainType;

/** @author taedium */
public class DomainTypeGenerator extends AbstractGenerator {

  protected final DomainMeta domainMeta;

  protected final String typeName;

  protected final String simpleMetaClassName;

  protected final String typeParamDecl;

  public DomainTypeGenerator(
      ProcessingEnvironment env, TypeElement domainElement, DomainMeta domainMeta)
      throws IOException {
    super(env, domainElement, null, null, Constants.METATYPE_PREFIX, "");
    assertNotNull(domainMeta);
    this.domainMeta = domainMeta;
    this.typeName = TypeMirrorUtil.getTypeName(domainMeta.getType(), env);
    this.simpleMetaClassName = MetaUtil.toSimpleMetaName(domainElement, env);
    this.typeParamDecl = makeTypeParamDecl(typeName);
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
    if (typeElement.getTypeParameters().isEmpty()) {
      iprint("/** */%n");
    } else {
      iprint("/**%n");
      for (TypeParameterElement typeParam : typeElement.getTypeParameters()) {
        iprint(" * @param <%1$s> %1$s%n", typeParam.getSimpleName());
      }
      iprint(" */%n");
    }
    printGenerated();
    iprint(
        "public final class %1$s%5$s extends %2$s<%3$s, %4$s> {%n",
        simpleMetaClassName,
        AbstractDomainType.class.getName(),
        TypeMirrorUtil.boxIfPrimitive(domainMeta.getValueType(), env),
        typeName,
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
    if (domainMeta.isParametarized()) {
      iprint("@SuppressWarnings(\"rawtypes\")%n");
    }
    iprint("private static final %1$s singleton = new %1$s();%n", simpleName);
    print("%n");
  }

  protected void printConstructors() {
    iprint("private %1$s() {%n", simpleName);
    if (domainMeta.getBasicCtType().isEnum()) {
      iprint(
          "    super(() -> new %1$s(%2$s.class));%n",
          domainMeta.getWrapperCtType().getTypeName(),
          TypeMirrorUtil.boxIfPrimitive(domainMeta.getValueType(), env));
    } else {
      iprint("    super(() -> new %1$s());%n", domainMeta.getWrapperCtType().getTypeName());
    }
    iprint("}%n");
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
    boolean primitive = domainMeta.getBasicCtType().isPrimitive();
    iprint("@Override%n");
    iprint(
        "protected %1$s newDomain(%2$s value) {%n",
        typeName, TypeMirrorUtil.boxIfPrimitive(domainMeta.getValueType(), env));
    if (!primitive && !domainMeta.getAcceptNull()) {
      iprint("    if (value == null) {%n");
      iprint("        return null;%n");
      iprint("    }%n");
    }
    if (domainMeta.providesConstructor()) {
      if (primitive) {
        iprint(
            "    return new %1$s(%2$s.unbox(value));%n",
            /* 1 */ typeName, BoxedPrimitiveUtil.class.getName());
      } else {
        iprint("    return new %1$s(value);%n", /* 1 */ typeName);
      }
    } else {
      if (primitive) {
        iprint(
            "    return %1$s.%2$s(%3$s.unbox(value));%n",
            /* 1 */ domainMeta.getTypeElement().getQualifiedName(),
            /* 2 */ domainMeta.getFactoryMethod(),
            /* 3 */ BoxedPrimitiveUtil.class.getName());
      } else {
        iprint(
            "    return %1$s.%2$s(value);%n",
            /* 1 */ domainMeta.getTypeElement().getQualifiedName(),
            /* 2 */ domainMeta.getFactoryMethod());
      }
    }
    iprint("}%n");
    print("%n");
  }

  protected void printGetBasicValueMethod() {
    iprint("@Override%n");
    iprint(
        "protected %1$s getBasicValue(%2$s domain) {%n",
        TypeMirrorUtil.boxIfPrimitive(domainMeta.getValueType(), env), typeName);
    iprint("    if (domain == null) {%n");
    iprint("        return null;%n");
    iprint("    }%n");
    iprint("    return domain.%1$s();%n", domainMeta.getAccessorMethod());
    iprint("}%n");
    print("%n");
  }

  protected void printGetBasicClassMethod() {
    iprint("@Override%n");
    iprint("public Class<?> getBasicClass() {%n");
    iprint("    return %1$s.class;%n", domainMeta.getValueType());
    iprint("}%n");
    print("%n");
  }

  protected void printGetDomainClassMethod() {
    if (domainMeta.isParametarized()) {
      iprint("@SuppressWarnings(\"unchecked\")%n");
    }
    iprint("@Override%n");
    iprint("public Class<%1$s> getDomainClass() {%n", typeName);
    if (domainMeta.isParametarized()) {
      iprint("    Class<?> clazz = %1$s.class;%n", domainMeta.getTypeElement().getQualifiedName());
      iprint("    return (Class<%1$s>) clazz;%n", typeName);
    } else {
      iprint("    return %1$s.class;%n", domainMeta.getTypeElement().getQualifiedName());
    }
    iprint("}%n");
    print("%n");
  }

  protected void printGetSingletonInternalMethod() {
    iprint("/**%n");
    iprint(" * @return the singleton%n");
    iprint(" */%n");
    if (domainMeta.isParametarized()) {
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
