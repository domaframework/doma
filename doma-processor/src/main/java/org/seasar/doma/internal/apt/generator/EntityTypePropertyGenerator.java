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

import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.generator.ScalarMetaFactory.ScalarMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityPropertyMeta;
import org.seasar.doma.jdbc.entity.AssignedIdPropertyType;
import org.seasar.doma.jdbc.entity.DefaultPropertyType;
import org.seasar.doma.jdbc.entity.EmbeddedPropertyType;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.jdbc.entity.TenantIdPropertyType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

public class EntityTypePropertyGenerator extends AbstractGenerator {

  private final EntityMeta entityMeta;

  private final EntityPropertyMeta propertyMeta;

  EntityTypePropertyGenerator(
      RoundContext ctx,
      ClassName className,
      Printer printer,
      EntityMeta entityMeta,
      EntityPropertyMeta entityPropertyMeta) {
    super(ctx, className, printer);
    assertNotNull(entityMeta);
    this.entityMeta = entityMeta;
    this.propertyMeta = entityPropertyMeta;
  }

  @Override
  public void generate() {
    printPropertyTypeField();
  }

  private void printPropertyTypeField() {
    if (propertyMeta.isEmbedded()) {
      EmbeddableCtTypeVisitor visitor = new EmbeddableCtTypeVisitor();
      EmbeddableCtType embeddableCtType = propertyMeta.getCtType().accept(visitor, null);
      printEmbeddedPropertyTypeField(embeddableCtType);
    } else {
      ScalarMeta scalarMeta = propertyMeta.getCtType().accept(new ScalarMetaFactory(), false);
      if (propertyMeta.isId()) {
        if (propertyMeta.getIdGeneratorMeta() != null) {
          printGeneratedIdPropertyTypeField(scalarMeta);
        } else {
          printAssignedIdPropertyTypeField(scalarMeta);
        }
      } else if (propertyMeta.isVersion()) {
        printVersionPropertyTypeField(scalarMeta);
      } else if (propertyMeta.isTenantId()) {
        printTenantIdPropertyTypeField(scalarMeta);
      } else {
        printDefaultPropertyTypeField(scalarMeta);
      }
    }
  }

  private void printEmbeddedPropertyTypeField(EmbeddableCtType embeddableCtType) {
    print(
        "new %1$s<%2$s, %3$s>(\"%5$s\", %2$s.class, %6$s.getEmbeddablePropertyTypes(\"%5$s\", %2$s.class, __namingType, \"%7$s\"))",
        /* 1 */ EmbeddedPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ propertyMeta.getType(),
        /* 4 */ null,
        /* 5 */ propertyMeta.getName(),
        /* 6 */ embeddableCtType.getTypeCode(),
        /* 7 */ propertyMeta.getColumnNamePrefix());
  }

  private void printGeneratedIdPropertyTypeField(ScalarMeta scalarMeta) {
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

  private void printAssignedIdPropertyTypeField(ScalarMeta scalarMeta) {
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

  private void printVersionPropertyTypeField(ScalarMeta scalarMeta) {
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

  private void printTenantIdPropertyTypeField(ScalarMeta scalarMeta) {
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

  private void printDefaultPropertyTypeField(ScalarMeta scalarMeta) {
    print(
        "new %1$s<%2$s, %3$s, %4$s>(%6$s.class, %7$s, \"%8$s\", \"%9$s\", __namingType, %10$s, %11$s, %12$s)",
        /* 1 */ DefaultPropertyType.class,
        /* 2 */ entityMeta.getType(),
        /* 3 */ scalarMeta.getBasicType(),
        /* 4 */ scalarMeta.getContainerType(),
        /* 5 */ null,
        /* 6 */ entityMeta.getType(),
        /* 7 */ scalarMeta.getSupplier(),
        /* 8 */ propertyMeta.getName(),
        /* 9 */ propertyMeta.getColumnName(),
        /* 10 */ propertyMeta.isColumnInsertable(),
        /* 11 */ propertyMeta.isColumnUpdatable(),
        /* 12 */ propertyMeta.isColumnQuoteRequired());
  }

  private static class EmbeddableCtTypeVisitor
      extends SimpleCtTypeVisitor<EmbeddableCtType, Void, RuntimeException> {

    @Override
    public EmbeddableCtType visitEmbeddableCtType(EmbeddableCtType ctType, Void aVoid)
        throws RuntimeException {
      return ctType;
    }
  }
}
