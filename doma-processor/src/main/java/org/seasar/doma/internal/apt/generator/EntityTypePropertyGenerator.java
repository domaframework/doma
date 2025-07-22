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

import java.util.List;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.annot.ColumnOverrideAnnot;
import org.seasar.doma.internal.apt.annot.EmbeddedAnnot;
import org.seasar.doma.internal.apt.generator.ScalarMetaFactory.ScalarMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddedMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityFieldMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityPropertyMeta;
import org.seasar.doma.jdbc.entity.AssignedIdPropertyType;
import org.seasar.doma.jdbc.entity.ColumnType;
import org.seasar.doma.jdbc.entity.DefaultPropertyType;
import org.seasar.doma.jdbc.entity.EmbeddedPropertyType;
import org.seasar.doma.jdbc.entity.EmbeddedType;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.jdbc.entity.TenantIdPropertyType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

public class EntityTypePropertyGenerator extends AbstractGenerator {

  private final EntityMeta entityMeta;

  private final EntityFieldMeta fieldMeta;

  EntityTypePropertyGenerator(
      RoundContext ctx,
      ClassName className,
      Printer printer,
      EntityMeta entityMeta,
      EntityFieldMeta entityFieldMeta) {
    super(ctx, className, printer);
    assertNotNull(entityMeta);
    this.entityMeta = entityMeta;
    this.fieldMeta = entityFieldMeta;
  }

  @Override
  public void generate() {
    printPropertyTypeField();
  }

  private void printPropertyTypeField() {
    if (fieldMeta instanceof EmbeddedMeta embeddedMeta) {
      printEmbeddedPropertyTypeField(embeddedMeta);
    } else if (fieldMeta instanceof EntityPropertyMeta propertyMeta) {
      ScalarMeta scalarMeta = propertyMeta.getCtType().accept(new ScalarMetaFactory(), false);
      if (propertyMeta.isId()) {
        if (propertyMeta.getIdGeneratorMeta() != null) {
          printGeneratedIdPropertyTypeField(propertyMeta, scalarMeta);
        } else {
          printAssignedIdPropertyTypeField(propertyMeta, scalarMeta);
        }
      } else if (propertyMeta.isVersion()) {
        printVersionPropertyTypeField(propertyMeta, scalarMeta);
      } else if (propertyMeta.isTenantId()) {
        printTenantIdPropertyTypeField(propertyMeta, scalarMeta);
      } else {
        printDefaultPropertyTypeField(propertyMeta, scalarMeta);
      }
    }
  }

  private void printEmbeddedPropertyTypeField(EmbeddedMeta embeddedMeta) {
    print(
        "new %1$s<%2$s, %3$s>(\"%5$s\", %2$s.class, %6$s.getEmbeddablePropertyTypes(\"%5$s\", %2$s.class, __namingType, %7$s))",
        /* 1 */ EmbeddedPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ fieldMeta.getCtType().getType(),
        /* 4 */ null,
        /* 5 */ fieldMeta.getName(),
        /* 6 */ embeddedMeta.embeddableCtType().getTypeCode(),
        /* 7 */ toEmbeddedTypeCode(embeddedMeta.embeddedAnnot()));
  }

  private void printGeneratedIdPropertyTypeField(
      EntityPropertyMeta propertyMeta, ScalarMeta scalarMeta) {
    print(
        "new %1$s<%2$s, %3$s, %4$s>(%6$s.class, %7$s, \"%8$s\", \"%9$s\", __namingType, %10$s, __idGenerator)",
        /* 1 */ GeneratedIdPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ scalarMeta.getBasicType(),
        /* 4 */ scalarMeta.getContainerType(),
        /* 5 */ null,
        /* 6 */ entityMeta.getType(),
        /* 7 */ scalarMeta.getSupplier(),
        /* 8 */ propertyMeta.getName(),
        /* 9 */ propertyMeta.getColumnName(),
        /* 10 */ propertyMeta.isColumnQuoteRequired());
  }

  private void printAssignedIdPropertyTypeField(
      EntityPropertyMeta propertyMeta, ScalarMeta scalarMeta) {
    print(
        "new %1$s<%2$s, %3$s, %4$s>(%6$s.class, %7$s, \"%8$s\", \"%9$s\", __namingType, %10$s)",
        /* 1 */ AssignedIdPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ scalarMeta.getBasicType(),
        /* 4 */ scalarMeta.getContainerType(),
        /* 5 */ null,
        /* 6 */ entityMeta.getType(),
        /* 7 */ scalarMeta.getSupplier(),
        /* 8 */ propertyMeta.getName(),
        /* 9 */ propertyMeta.getColumnName(),
        /* 10 */ propertyMeta.isColumnQuoteRequired());
  }

  private void printVersionPropertyTypeField(
      EntityPropertyMeta propertyMeta, ScalarMeta scalarMeta) {
    print(
        "new %1$s<%2$s, %3$s, %4$s>(%6$s.class, %7$s, \"%8$s\", \"%9$s\", __namingType, %10$s)",
        /* 1 */ VersionPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ scalarMeta.getBasicType(),
        /* 4 */ scalarMeta.getContainerType(),
        /* 5 */ null,
        /* 6 */ entityMeta.getType(),
        /* 7 */ scalarMeta.getSupplier(),
        /* 8 */ propertyMeta.getName(),
        /* 9 */ propertyMeta.getColumnName(),
        /* 10 */ propertyMeta.isColumnQuoteRequired());
  }

  private void printTenantIdPropertyTypeField(
      EntityPropertyMeta propertyMeta, ScalarMeta scalarMeta) {
    print(
        "new %1$s<%2$s, %3$s, %4$s>(%6$s.class, %7$s, \"%8$s\", \"%9$s\", __namingType, %10$s)",
        /* 1 */ TenantIdPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ scalarMeta.getBasicType(),
        /* 4 */ scalarMeta.getContainerType(),
        /* 5 */ null,
        /* 6 */ entityMeta.getType(),
        /* 7 */ scalarMeta.getSupplier(),
        /* 8 */ propertyMeta.getName(),
        /* 9 */ propertyMeta.getColumnName(),
        /* 10 */ propertyMeta.isColumnQuoteRequired());
  }

  private void printDefaultPropertyTypeField(
      EntityPropertyMeta propertyMeta, ScalarMeta scalarMeta) {
    print(
        "new %1$s<%2$s, %3$s, %4$s>(%6$s.class, %7$s, \"%8$s\", \"%9$s\", __namingType, %10$s, %11$s, %12$s)",
        /* 1 */ DefaultPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ scalarMeta.getBasicType(),
        /* 4 */ scalarMeta.getContainerType(),
        /* 5 */ null,
        /* 6 */ entityMeta.getType(),
        /* 7 */ scalarMeta.getSupplier(),
        /* 8 */ fieldMeta.getName(),
        /* 9 */ propertyMeta.getColumnName(),
        /* 10 */ propertyMeta.isColumnInsertable(),
        /* 11 */ propertyMeta.isColumnUpdatable(),
        /* 12 */ propertyMeta.isColumnQuoteRequired());
  }

  private Code toEmbeddedTypeCode(EmbeddedAnnot embeddedAnnot) {
    return new Code(
        p -> {
          if (embeddedAnnot == null) {
            p.print("null");
          } else {
            p.print(
                "new %1$s(\"%2$s\", java.util.Map.ofEntries(%3$s))",
                /* 1 */ EmbeddedType.class,
                /* 2 */ embeddedAnnot.getPrefixValue(),
                /* 3 */ toMapEntryCode(embeddedAnnot.getColumnOverridesValue()));
          }
        });
  }

  private Code toMapEntryCode(List<ColumnOverrideAnnot> columnOverrideAnnotList) {
    return new Code(
        p -> {
          var it = columnOverrideAnnotList.iterator();
          while (it.hasNext()) {
            var columnOverrideAnnot = it.next();
            var name = columnOverrideAnnot.getNameValue();
            var column = columnOverrideAnnot.getColumnValue();
            p.print(
                "java.util.Map.entry(\"%7$s.%1$s\", new %2$s(%3$s, %4$s, %5$s, %6$s))",
                /* 1 */ name,
                /* 2 */ ColumnType.class,
                /* 3 */ column.getNameValue() != null ? '"' + column.getNameValue() + '"' : null,
                /* 4 */ column.getInsertableValue(),
                /* 5 */ column.getUpdatableValue(),
                /* 6 */ column.getQuoteValue(),
                /* 7 */ fieldMeta.getName());
            if (it.hasNext()) {
              p.print(", ");
            }
          }
        });
  }
}
