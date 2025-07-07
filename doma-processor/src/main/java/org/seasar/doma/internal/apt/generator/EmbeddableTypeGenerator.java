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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.seasar.doma.EmbeddableTypeImplementation;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.generator.ScalarMetaFactory.ScalarMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddablePropertyMeta;
import org.seasar.doma.jdbc.entity.DefaultPropertyType;
import org.seasar.doma.jdbc.entity.EmbeddableType;
import org.seasar.doma.jdbc.entity.EmbeddedType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.Property;

public class EmbeddableTypeGenerator extends AbstractGenerator {

  private final EmbeddableMeta embeddableMeta;

  public EmbeddableTypeGenerator(
      RoundContext ctx, ClassName className, Printer printer, EmbeddableMeta embeddableMeta) {
    super(ctx, className, printer);
    assertNotNull(embeddableMeta);
    this.embeddableMeta = embeddableMeta;
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
    iprint("/** */%n");
    printGenerated();
    printEmbeddableTypeImplementation();
    iprint(
        "public final class %1$s implements %2$s<%3$s> {%n",
        /* 1 */ simpleName, /* 2 */ EmbeddableType.class, /* 3 */ embeddableMeta.getType());
    print("%n");
    indent();
    printValidateVersionStaticInitializer();
    printFields();
    printMethods();
    printNestedClass();
    unindent();
    iprint("}%n");
  }

  private void printEmbeddableTypeImplementation() {
    iprint("@%1$s%n", EmbeddableTypeImplementation.class);
  }

  private void printFields() {
    printSingletonField();
  }

  private void printSingletonField() {
    iprint("private static final %1$s __singleton = new %1$s();%n", simpleName);
    print("%n");
  }

  private void printMethods() {
    printGetEmbeddablePropertyTypesMethod();
    printNewEmbeddableMethod();
    printGetSingletonInternalMethod();
  }

  private void printGetEmbeddablePropertyTypesMethod() {
    iprint("@Override%n");
    iprint(
        "public <ENTITY> %1$s<%2$s<ENTITY, ?>> getEmbeddablePropertyTypes"
            + "(String embeddedPropertyName, Class<ENTITY> entityClass, %3$s namingType, %4$s embeddedType) {%n",
        /* 1 */ List.class,
        /* 2 */ EntityPropertyType.class,
        /* 3 */ NamingType.class,
        /* 4 */ EmbeddedType.class);
    iprint("    return %1$s.asList(%n", Arrays.class);
    for (Iterator<EmbeddablePropertyMeta> it =
            embeddableMeta.getEmbeddablePropertyMetas().iterator();
        it.hasNext(); ) {
      EmbeddablePropertyMeta propertyMeta = it.next();
      ScalarMeta scalarMeta = propertyMeta.getCtType().accept(new ScalarMetaFactory(), false);
      iprint(
          "        new %1$s<ENTITY, %2$s, %3$s>("
              + "entityClass, %4$s, embeddedPropertyName + \".%5$s\", \"%6$s\", namingType, %7$s, %8$s, %9$s, embeddedType)",
          /* 1 */ DefaultPropertyType.class,
          /* 2 */ scalarMeta.getBasicType(),
          /* 3 */ scalarMeta.getContainerType(),
          /* 4 */ scalarMeta.getSupplier(),
          /* 5 */ propertyMeta.getName(),
          /* 6 */ propertyMeta.getColumnName(),
          /* 7 */ propertyMeta.isColumnInsertable(),
          /* 8 */ propertyMeta.isColumnUpdatable(),
          /* 9 */ propertyMeta.isColumnQuoteRequired());
      print(it.hasNext() ? ",%n" : "");
    }
    print(");%n");
    iprint("}%n");
    print("%n");
  }

  private void printNewEmbeddableMethod() {
    if (embeddableMeta.getEmbeddablePropertyMetas().stream()
        .anyMatch(p -> p.getCtType().hasTypeParameter())) {
      iprint("@SuppressWarnings(\"unchecked\")%n");
    }
    iprint("@Override%n");
    iprint(
        "public <ENTITY> %1$s newEmbeddable(String embeddedPropertyName, %2$s<String, %3$s<ENTITY, ?>> __args) {%n",
        embeddableMeta.getType(), Map.class, Property.class);
    if (embeddableMeta.isAbstract()) {
      iprint("    return null;%n");
    } else {
      iprint("    return new %1$s(%n", embeddableMeta.getType());
      for (Iterator<EmbeddablePropertyMeta> it =
              embeddableMeta.getEmbeddablePropertyMetas().iterator();
          it.hasNext(); ) {
        EmbeddablePropertyMeta propertyMeta = it.next();
        iprint(
            "        (%1$s)(__args.get(embeddedPropertyName + \".%2$s\") "
                + "!= null ? __args.get(embeddedPropertyName + \".%2$s\").get() : null)",
            propertyMeta.getBoxedType(), propertyMeta.getName());
        if (it.hasNext()) {
          print(",%n");
        }
      }
      print(");%n");
    }
    iprint("}%n");
    print("%n");
  }

  private void printGetSingletonInternalMethod() {
    iprint("/**%n");
    iprint(" * @return the singleton%n");
    iprint(" */%n");
    iprint("public static %1$s getSingletonInternal() {%n", simpleName);
    iprint("    return __singleton;%n");
    iprint("}%n");
    print("%n");
  }

  private void printNestedClass() {
    EmbeddableMetamodelGenerator generator =
        new EmbeddableMetamodelGenerator(ctx, className, printer, embeddableMeta);
    generator.generate();
  }
}
