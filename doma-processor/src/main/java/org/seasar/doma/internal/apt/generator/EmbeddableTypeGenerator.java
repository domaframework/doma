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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.seasar.doma.EmbeddableTypeImplementation;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.annot.ColumnOverrideAnnot;
import org.seasar.doma.internal.apt.annot.EmbeddedAnnot;
import org.seasar.doma.internal.apt.generator.ScalarMetaFactory.ScalarMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableFieldMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddablePropertyMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddedMeta;
import org.seasar.doma.jdbc.entity.ColumnType;
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
    iprint("    return %1$s.of(%n", List.class);
    for (Iterator<EmbeddableFieldMeta> it = embeddableMeta.getEmbeddableFieldMetas().iterator();
        it.hasNext(); ) {
      EmbeddableFieldMeta fieldMeta = it.next();
      if (fieldMeta instanceof EmbeddedMeta embeddedMeta) {
        iprint(
            "        %1$s.getEmbeddablePropertyTypes(embeddedPropertyName + \".%2$s\", entityClass, namingType, %3$s)",
            /* 1 */ embeddedMeta.embeddableCtType().getTypeCode(),
            /* 2 */ embeddedMeta.name(),
            /* 3 */ toEmbeddedTypeCode(embeddedMeta.name(), embeddedMeta.embeddedAnnot()));

      } else if (fieldMeta instanceof EmbeddablePropertyMeta propertyMeta) {
        ScalarMeta scalarMeta = propertyMeta.getCtType().accept(new ScalarMetaFactory(), false);
        iprint(
            "        java.util.List.<org.seasar.doma.jdbc.entity.EntityPropertyType<ENTITY,?>>of(new %1$s<ENTITY, %2$s, %3$s>("
                + "entityClass, %4$s, embeddedPropertyName + \".%5$s\", \"%6$s\", namingType, %7$s, %8$s, %9$s, embeddedType))",
            /* 1 */ DefaultPropertyType.class,
            /* 2 */ scalarMeta.getBasicType(),
            /* 3 */ scalarMeta.getContainerType(),
            /* 4 */ scalarMeta.getSupplier(),
            /* 5 */ propertyMeta.getName(),
            /* 6 */ propertyMeta.getColumnName(),
            /* 7 */ propertyMeta.isColumnInsertable(),
            /* 8 */ propertyMeta.isColumnUpdatable(),
            /* 9 */ propertyMeta.isColumnQuoteRequired());
      } else {
        throw new AptIllegalStateException(fieldMeta.toString());
      }
      print(it.hasNext() ? ",%n" : "");
    }
    print("%n");
    iprint("    ).stream().flatMap(it -> it.stream()).toList();%n");
    iprint("}%n");
    print("%n");
  }

  private void printNewEmbeddableMethod() {
    if (embeddableMeta.getEmbeddableFieldMetas().stream()
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
      for (Iterator<EmbeddableFieldMeta> it = embeddableMeta.getEmbeddableFieldMetas().iterator();
          it.hasNext(); ) {
        EmbeddableFieldMeta fieldMeta = it.next();
        if (fieldMeta instanceof EmbeddedMeta embeddedMeta) {
          iprint(
              "        (%1$s)%2$s.newEmbeddable(embeddedPropertyName + \".%3$s\", __args)",
              embeddedMeta.embeddableMeta().getType(),
              embeddedMeta.embeddableCtType().getTypeCode(),
              embeddedMeta.name());

        } else if (fieldMeta instanceof EmbeddablePropertyMeta propertyMeta) {
          iprint(
              "        (%1$s)(__args.get(embeddedPropertyName + \".%2$s\") "
                  + "!= null ? __args.get(embeddedPropertyName + \".%2$s\").get() : null)",
              propertyMeta.getBoxedType(), propertyMeta.getName());
        } else {
          throw new AptIllegalStateException(fieldMeta.toString());
        }
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

  private Code toEmbeddedTypeCode(String referenceName, EmbeddedAnnot embeddedAnnot) {
    return new Code(
        p -> {
          if (embeddedAnnot == null) {
            p.print("embeddedType");
          } else {
            p.print(
                "new %1$s(\"%2$s\", java.util.Map.ofEntries(%3$s)).merge(embeddedType)",
                /* 1 */ EmbeddedType.class,
                /* 2 */ embeddedAnnot.getPrefixValue(),
                /* 3 */ toMapEntryCode(referenceName, embeddedAnnot.getColumnOverridesValue()));
          }
        });
  }

  private Code toMapEntryCode(
      String referenceName, List<ColumnOverrideAnnot> columnOverrideAnnotList) {
    return new Code(
        p -> {
          var it = columnOverrideAnnotList.iterator();
          while (it.hasNext()) {
            var columnOverrideAnnot = it.next();
            var name = columnOverrideAnnot.getNameValue();
            var column = columnOverrideAnnot.getColumnValue();
            p.print(
                "java.util.Map.entry(embeddedPropertyName + \".%7$s\" + \".%1$s\", new %2$s(%3$s, %4$s, %5$s, %6$s))",
                /* 1 */ name,
                /* 2 */ ColumnType.class,
                /* 3 */ column.getNameValue() != null ? '"' + column.getNameValue() + '"' : null,
                /* 4 */ column.getInsertableValue(),
                /* 5 */ column.getUpdatableValue(),
                /* 6 */ column.getQuoteValue(),
                /* 7 */ referenceName);
            if (it.hasNext()) {
              p.print(", ");
            }
          }
        });
  }
}
