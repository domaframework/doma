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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.AssociationLinkerAnnot;
import org.seasar.doma.internal.apt.annot.SelectAnnot;
import org.seasar.doma.internal.apt.cttype.BiFunctionCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.CtTypeVisitor;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.query.SqlFileSelectQueryMeta;
import org.seasar.doma.message.Message;

public class AggregateStrategyMetaFactory {

  private final Context ctx;
  private final SqlFileSelectQueryMeta queryMeta;

  public AggregateStrategyMetaFactory(Context ctx, SqlFileSelectQueryMeta queryMeta) {
    this.ctx = Objects.requireNonNull(ctx);
    this.queryMeta = Objects.requireNonNull(queryMeta);
  }

  public AggregateStrategyMeta createAggregateStrategyMeta() {
    TypeMirror aggregateStrategyType = queryMeta.getSelectAnnot().getAggregateStrategyValue();
    if (ctx.getMoreTypes().isSameTypeWithErasure(aggregateStrategyType, Void.class)) {
      return null;
    }
    List<AssociationLinkerMeta> associationLinkerMetas =
        findAssociationLinkerMetas(aggregateStrategyType);
    return new AggregateStrategyMeta(associationLinkerMetas);
  }

  private List<AssociationLinkerMeta> findAssociationLinkerMetas(TypeMirror aggregateStrategyType) {
    TypeElement aggregateStrategyElement = ctx.getMoreTypes().toTypeElement(aggregateStrategyType);
    if (aggregateStrategyElement == null) {
      throw new AptIllegalStateException("aggregateStrategyElement must not be null");
    }
    EntityCtType root = resolveRootEntityCtType();
    List<AssociationLinkerMeta> associationLinkerMetas = new ArrayList<>();

    for (VariableElement fieldElement :
        ElementFilter.fieldsIn(aggregateStrategyElement.getEnclosedElements())) {
      AssociationLinkerAnnot associationLinkerAnnot = getAssociationLinkerAnnot(fieldElement);
      if (associationLinkerAnnot == null) {
        continue;
      }
      validateModifiers(fieldElement);
      BiFunctionMeta biFunctionMeta = createBiFunctionMeta(fieldElement);
      validateAssociation(root, associationLinkerAnnot, biFunctionMeta, fieldElement);
      AssociationLinkerMeta associationLinkerMeta =
          createAssociationLinkerMeta(
              fieldElement, associationLinkerAnnot, biFunctionMeta, aggregateStrategyElement);
      associationLinkerMetas.add(associationLinkerMeta);
    }

    validateAssociationLinkerMetas(queryMeta, associationLinkerMetas);
    return associationLinkerMetas;
  }

  private EntityCtType resolveRootEntityCtType() {
    CtType ctType = queryMeta.getReturnMeta().getCtType();
    return resolveEntityCtType(
        ctType,
        () -> {
          throw new AptException(Message.DOMA4473, queryMeta.getMethodElement(), new Object[] {});
        });
  }

  private EntityCtType resolveEntityCtType(CtType ctType, Runnable errorCallback) {
    class EntityCtTypeVisitor extends SimpleCtTypeVisitor<EntityCtType, Void, RuntimeException> {
      @Override
      protected EntityCtType defaultAction(CtType ctType, Void p) throws RuntimeException {
        errorCallback.run();
        return null;
      }

      @Override
      public EntityCtType visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
        return ctType;
      }
    }

    return ctType.accept(
        new EntityCtTypeVisitor() {

          @Override
          public EntityCtType visitIterableCtType(IterableCtType ctType, Void unused)
              throws RuntimeException {
            return ctType
                .getElementCtType()
                .accept(
                    new EntityCtTypeVisitor() {
                      @Override
                      public EntityCtType visitOptionalCtType(OptionalCtType ctType, Void unused)
                          throws RuntimeException {
                        return ctType.getElementCtType().accept(new EntityCtTypeVisitor(), null);
                      }
                    },
                    null);
          }

          @Override
          public EntityCtType visitOptionalCtType(OptionalCtType ctType, Void unused)
              throws RuntimeException {
            return ctType.getElementCtType().accept(new EntityCtTypeVisitor(), null);
          }
        },
        null);
  }

  private AssociationLinkerAnnot getAssociationLinkerAnnot(VariableElement fieldElement) {
    AssociationLinkerAnnot associationLinkerAnnot =
        ctx.getAnnotations().newAssociationLinkerAnnot(fieldElement);
    if (associationLinkerAnnot == null) {
      return null;
    }
    if (associationLinkerAnnot.getPropertyPathValue().isBlank()) {
      throw new AptException(
          Message.DOMA4472,
          fieldElement,
          associationLinkerAnnot.getAnnotationMirror(),
          associationLinkerAnnot.getPropertyPath(),
          new Object[] {});
    }
    if (associationLinkerAnnot.getTableAliasValue().isBlank()) {
      throw new AptException(
          Message.DOMA4468,
          fieldElement,
          associationLinkerAnnot.getAnnotationMirror(),
          associationLinkerAnnot.getTableAlias(),
          new Object[] {});
    }
    return associationLinkerAnnot;
  }

  private void validateModifiers(VariableElement fieldElement) {
    if (!fieldElement.getModifiers().contains(Modifier.STATIC)) {
      throw new AptException(Message.DOMA4464, fieldElement, new Object[] {});
    }
    if (!fieldElement.getModifiers().contains(Modifier.PUBLIC)) {
      throw new AptException(Message.DOMA4470, fieldElement, new Object[] {});
    }
    if (!fieldElement.getModifiers().contains(Modifier.FINAL)) {
      throw new AptException(Message.DOMA4471, fieldElement, new Object[] {});
    }
  }

  private BiFunctionMeta createBiFunctionMeta(VariableElement fieldElement) {
    BiFunctionCtType ctType = ctx.getCtTypes().newBiFunctionCtType(fieldElement.asType());
    if (ctType == null) {
      throw new AptException(Message.DOMA4465, fieldElement, new Object[] {});
    }
    CtTypeVisitor<EntityCtType, String, RuntimeException> entityCtTypeVisitor =
        new SimpleCtTypeVisitor<>() {
          @Override
          protected EntityCtType defaultAction(CtType ctType, String ordinalNumber)
              throws RuntimeException {
            throw new AptException(Message.DOMA4466, fieldElement, new Object[] {ordinalNumber});
          }

          @Override
          public EntityCtType visitEntityCtType(EntityCtType ctType, String ordinalNumber)
              throws RuntimeException {
            return ctType;
          }
        };

    EntityCtType source = ctType.getFirstArgCtType().accept(entityCtTypeVisitor, "first");
    EntityCtType target = ctType.getSecondArgCtType().accept(entityCtTypeVisitor, "second");
    EntityCtType result = ctType.getResultCtType().accept(entityCtTypeVisitor, "third");

    if (!ctx.getMoreTypes().isSameType(result.getType(), source.getType())) {
      throw new AptException(Message.DOMA4467, fieldElement, new Object[] {});
    }

    return new BiFunctionMeta(source, target, result);
  }

  private void validateAssociation(
      EntityCtType root,
      AssociationLinkerAnnot associationLinkerAnnot,
      BiFunctionMeta biFunctionMeta,
      Element element) {
    TypeMirror source = root.getType();
    TypeMirror target = root.getType();
    String[] segments = associationLinkerAnnot.getPropertyPathValue().split("\\.");
    if (segments.length == 0) {
      throw new AptIllegalStateException("fragments must not be empty.");
    }
    for (String segment : segments) {
      source = target;
      target = resolveEntity(source, segment, element, associationLinkerAnnot);
    }
    if (!ctx.getMoreTypes().isSameType(biFunctionMeta.source.getType(), source)) {
      throw new AptException(
          Message.DOMA4475, element, new Object[] {biFunctionMeta.source.getType(), source});
    }
    if (!ctx.getMoreTypes().isSameType(biFunctionMeta.target.getType(), target)) {
      throw new AptException(
          Message.DOMA4476, element, new Object[] {biFunctionMeta.target.getType(), target});
    }
  }

  private TypeMirror resolveEntity(
      TypeMirror typeMirror,
      String name,
      Element element,
      AssociationLinkerAnnot associationLinkerAnnot) {
    TypeElement sourceElement = ctx.getMoreTypes().toTypeElement(typeMirror);
    if (sourceElement == null) {
      throw new AptIllegalStateException("sourceElement is null. typeMirror=" + typeMirror);
    }
    Optional<VariableElement> field =
        ElementFilter.fieldsIn(sourceElement.getEnclosedElements()).stream()
            .filter(it -> it.getSimpleName().toString().equals(name))
            .findFirst();
    if (field.isEmpty()) {
      throw new AptException(
          Message.DOMA4474,
          element,
          associationLinkerAnnot.getAnnotationMirror(),
          associationLinkerAnnot.getPropertyPath(),
          new Object[] {name, sourceElement.getQualifiedName()});
    }
    TypeMirror fieldType = field.get().asType();
    CtType ctType = ctx.getCtTypes().newCtType(fieldType);
    EntityCtType entityCtType =
        resolveEntityCtType(
            ctType,
            () -> {
              throw new AptException(
                  Message.DOMA4477,
                  queryMeta.getMethodElement(),
                  new Object[] {name, sourceElement.getQualifiedName(), fieldType});
            });
    return entityCtType.getType();
  }

  private AssociationLinkerMeta createAssociationLinkerMeta(
      VariableElement fieldElement,
      AssociationLinkerAnnot associationLinkerAnnot,
      BiFunctionMeta biFunctionMeta,
      TypeElement aggregateStrategyElement) {
    return new AssociationLinkerMeta(
        associationLinkerAnnot.getPropertyPathValue(),
        associationLinkerAnnot.getTableAliasValue(),
        biFunctionMeta.source,
        biFunctionMeta.target,
        aggregateStrategyElement,
        fieldElement);
  }

  private static void validateAssociationLinkerMetas(
      SqlFileSelectQueryMeta queryMeta, List<AssociationLinkerMeta> associationLinkerMetas) {
    if (associationLinkerMetas.isEmpty()) {
      ExecutableElement method = queryMeta.getMethodElement();
      SelectAnnot selectAnnot = queryMeta.getSelectAnnot();
      throw new AptException(
          Message.DOMA4469,
          method,
          selectAnnot.getAnnotationMirror(),
          selectAnnot.getAggregateStrategy(),
          new Object[] {});
    }
  }

  private record BiFunctionMeta(EntityCtType source, EntityCtType target, EntityCtType result) {}
}
