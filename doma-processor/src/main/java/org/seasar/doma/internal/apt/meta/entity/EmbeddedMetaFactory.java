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
package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.HashSet;
import java.util.Set;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.Id;
import org.seasar.doma.TenantId;
import org.seasar.doma.Version;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.annot.ColumnAnnot;
import org.seasar.doma.internal.apt.annot.EmbeddedAnnot;
import org.seasar.doma.internal.apt.cttype.*;
import org.seasar.doma.message.Message;

class EmbeddedMetaFactory {

  private final RoundContext ctx;

  private final VariableElement fieldElement;

  private final CtType ctType;

  private final EmbeddableCtType embeddableCtType;

  public EmbeddedMetaFactory(
      RoundContext ctx,
      VariableElement fieldElement,
      CtType ctType,
      EmbeddableCtType embeddableCtType) {
    assertNotNull(ctx, fieldElement, ctType, embeddableCtType);
    this.ctx = ctx;
    this.fieldElement = fieldElement;
    this.ctType = ctType;
    this.embeddableCtType = embeddableCtType;
  }

  public EmbeddedMeta createEmbeddedMeta() {
    var typeElement = ctx.getMoreTypes().toTypeElement(embeddableCtType.getType());
    if (typeElement == null) {
      throw new AptIllegalStateException(
          "failed to convert to TypeElement: " + fieldElement.getSimpleName());
    }

    validateName();
    validateId();
    validateVersion();
    validateTenantId();
    validateColumn();
    validateClassReference(typeElement, new HashSet<>());

    EmbeddedAnnot embeddedAnnot = ctx.getAnnotations().newEmbeddedAnnot(fieldElement);
    validateEmbeddedAnnotation(typeElement, embeddedAnnot);

    var embeddableMetaFactory = new EmbeddableMetaFactory(ctx);
    var embeddableMeta = embeddableMetaFactory.createTypeElementMeta(typeElement);

    return new EmbeddedMeta(
        fieldElement.getSimpleName().toString(),
        embeddableMeta,
        ctType,
        embeddableCtType,
        embeddedAnnot);
  }

  private void validateClassReference(TypeElement typeElement, Set<String> visitedClassNames) {
    if (!visitedClassNames.add(typeElement.getQualifiedName().toString())) {
      // circular reference is found
      throw new AptException(
          Message.DOMA4500,
          fieldElement,
          new Object[] {fieldElement.getSimpleName(), typeElement.getQualifiedName()});
    }
    for (var enclosedElement : ElementFilter.fieldsIn(typeElement.getEnclosedElements())) {
      CtType enclosedCtType = ctx.getCtTypes().newCtType(enclosedElement.asType());
      CtType unwrappedCtType = unwrapOptionalCtType(enclosedCtType);
      if (unwrappedCtType instanceof EmbeddableCtType enclosedEmbeddableCtType) {
        TypeElement enclosedTypeElement =
            ctx.getMoreTypes().toTypeElement(enclosedEmbeddableCtType.getType());
        assertNotNull(enclosedTypeElement);
        validateClassReference(enclosedTypeElement, new HashSet<>(visitedClassNames));
      }
    }
  }

  private void validateEmbeddedAnnotation(TypeElement typeElement, EmbeddedAnnot embeddedAnnot) {
    if (embeddedAnnot == null) {
      return;
    }
    var pathNames = new HashSet<String>();
    collectPropertyPathNames(typeElement, null, pathNames);
    for (var columnOverride : embeddedAnnot.getColumnOverridesValue()) {
      var name = columnOverride.getNameValue();
      if (!pathNames.contains(name)) {
        throw new AptException(
            Message.DOMA4499,
            fieldElement,
            embeddedAnnot.getAnnotationMirror(),
            columnOverride.getName(),
            new Object[] {name, typeElement.getQualifiedName()});
      }
    }
  }

  private void collectPropertyPathNames(
      TypeElement typeElement, String prefix, Set<String> propertyPathNames) {
    for (var enclosedElement : ElementFilter.fieldsIn(typeElement.getEnclosedElements())) {
      CtType enclosedCtType = ctx.getCtTypes().newCtType(enclosedElement.asType());
      CtType unwrappedCtType = unwrapOptionalCtType(enclosedCtType);
      if (unwrappedCtType instanceof EmbeddableCtType enclosedEmbeddableCtType) {
        TypeElement enclosedTypeElement =
            ctx.getMoreTypes().toTypeElement(enclosedEmbeddableCtType.getType());
        assertNotNull(enclosedTypeElement);
        String simpleName = enclosedElement.getSimpleName().toString();
        String newPrefix = prefix != null ? prefix + "." + simpleName : simpleName;
        collectPropertyPathNames(enclosedTypeElement, newPrefix, propertyPathNames);
      } else {
        String simpleName = enclosedElement.getSimpleName().toString();
        String name = prefix != null ? prefix + "." + simpleName : simpleName;
        propertyPathNames.add(name);
      }
    }
  }

  private CtType unwrapOptionalCtType(CtType ctType) {
    if (ctType instanceof OptionalCtType optionalCtType) {
      return optionalCtType.getElementCtType();
    }
    return ctType;
  }

  private void validateName() {
    var name = fieldElement.getSimpleName().toString();
    if (name.startsWith(Constants.RESERVED_IDENTIFIER_PREFIX)) {
      throw new AptException(
          Message.DOMA4025, fieldElement, new Object[] {Constants.RESERVED_IDENTIFIER_PREFIX});
    }
  }

  private void validateId() {
    var id = fieldElement.getAnnotation(Id.class);
    if (id != null) {
      throw new AptException(Message.DOMA4302, fieldElement, new Object[] {});
    }
    var generatedValue = fieldElement.getAnnotation(GeneratedValue.class);
    if (generatedValue != null) {
      throw new AptException(Message.DOMA4303, fieldElement, new Object[] {});
    }
  }

  private void validateVersion() {
    var version = fieldElement.getAnnotation(Version.class);
    if (version != null) {
      throw new AptException(Message.DOMA4304, fieldElement, new Object[] {});
    }
  }

  private void validateTenantId() {
    var tenantId = fieldElement.getAnnotation(TenantId.class);
    if (tenantId != null) {
      throw new AptException(Message.DOMA4441, fieldElement, new Object[] {});
    }
  }

  private void validateColumn() {
    ColumnAnnot columnAnnot = ctx.getAnnotations().newColumnAnnot(fieldElement);
    if (columnAnnot != null) {
      throw new AptException(
          Message.DOMA4306, fieldElement, columnAnnot.getAnnotationMirror(), new Object[] {});
    }
  }
}
