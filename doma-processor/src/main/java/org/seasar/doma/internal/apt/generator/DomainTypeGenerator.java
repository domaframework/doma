/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.DomainTypeImplementation;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.meta.domain.DomainMeta;
import org.seasar.doma.internal.util.BoxedPrimitiveUtil;
import org.seasar.doma.jdbc.domain.AbstractDomainType;

public class DomainTypeGenerator extends AbstractGenerator {

  private final DomainMeta domainMeta;

  public DomainTypeGenerator(
      Context ctx, ClassName className, Printer printer, DomainMeta domainMeta) {
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
    printDomainTypeImplementation();
    if (domainMeta.isParameterized()) {
      iprint(
          "public final class %1$s<%5$s> extends %2$s<%3$s, %4$s> {%n",
          /* 1 */ simpleName,
          /* 2 */ AbstractDomainType.class,
          /* 3 */ ctx.getMoreTypes().boxIfPrimitive(domainMeta.getValueType()),
          /* 4 */ domainMeta.getType(),
          /* 5 */ domainMeta.getTypeParameters());
    } else {
      iprint(
          "public final class %1$s extends %2$s<%3$s, %4$s> {%n",
          /* 1 */ simpleName,
          /* 2 */ AbstractDomainType.class,
          /* 3 */ ctx.getMoreTypes().boxIfPrimitive(domainMeta.getValueType()),
          /* 4 */ domainMeta.getType());
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

  private void printDomainTypeImplementation() {
    iprint("@%1$s%n", DomainTypeImplementation.class);
  }

  private void printFields() {
    if (domainMeta.isParameterized()) {
      iprint("@SuppressWarnings(\"rawtypes\")%n");
    }
    iprint("private static final %1$s singleton = new %1$s();%n", simpleName);
    print("%n");
  }

  private void printConstructors() {
    iprint("private %1$s() {%n", simpleName);
    iprint("    super(%1$s);%n", domainMeta.getBasicCtType().getWrapperSupplierCode());
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
    boolean primitive = domainMeta.getBasicCtType().isPrimitive();
    iprint("@Override%n");
    iprint(
        "protected %1$s newDomain(%2$s value) {%n",
        /* 1 */ domainMeta.getType(),
        /* 2 */ ctx.getMoreTypes().boxIfPrimitive(domainMeta.getValueType()));
    if (!primitive && !domainMeta.getAcceptNull()) {
      iprint("    if (value == null) {%n");
      iprint("        return null;%n");
      iprint("    }%n");
    }
    if (domainMeta.providesConstructor()) {
      if (primitive) {
        iprint(
            "    return new %1$s(%2$s.unbox(value));%n",
            /* 1 */ domainMeta.getType(), BoxedPrimitiveUtil.class);
      } else {
        iprint("    return new %1$s(value);%n", /* 1 */ domainMeta.getType());
      }
    } else {
      if (primitive) {
        iprint(
            "    return %1$s.%2$s(%3$s.unbox(value));%n",
            /* 1 */ domainMeta.getTypeElement(),
            /* 2 */ domainMeta.getFactoryMethod(),
            /* 3 */ BoxedPrimitiveUtil.class);
      } else {
        iprint(
            "    return %1$s.%2$s(value);%n",
            /* 1 */ domainMeta.getTypeElement(), /* 2 */ domainMeta.getFactoryMethod());
      }
    }
    iprint("}%n");
    print("%n");
  }

  private void printGetBasicValueMethod() {
    iprint("@Override%n");
    iprint(
        "protected %1$s getBasicValue(%2$s domain) {%n",
        /* 1 */ ctx.getMoreTypes().boxIfPrimitive(domainMeta.getValueType()),
        /* 2 */ domainMeta.getType());
    iprint("    if (domain == null) {%n");
    iprint("        return null;%n");
    iprint("    }%n");
    iprint("    return domain.%1$s();%n", domainMeta.getAccessorMethod());
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
    iprint("public Class<%1$s> getDomainClass() {%n", domainMeta.getType());
    if (domainMeta.isParameterized()) {
      iprint("    Class<?> clazz = %1$s.class;%n", domainMeta.getTypeElement());
      iprint("    return (Class<%1$s>) clazz;%n", domainMeta.getType());
    } else {
      iprint("    return %1$s.class;%n", domainMeta.getTypeElement());
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
