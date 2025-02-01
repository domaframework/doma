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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.AggregateStrategyAnnot;
import org.seasar.doma.internal.apt.annot.AssociationLinkerAnnot;
import org.seasar.doma.internal.apt.cttype.BiFunctionCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.CtTypeVisitor;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.message.Message;

public class AggregateStrategyMetaFactory implements TypeElementMetaFactory<AggregateStrategyMeta> {

  private final Context ctx;

  public AggregateStrategyMetaFactory(Context ctx) {
    this.ctx = Objects.requireNonNull(ctx);
  }

  @Override
  public AggregateStrategyMeta createTypeElementMeta(TypeElement typeElement) {
    AggregateStrategyAnnot aggregateStrategyAnnot =
        ctx.getAnnotations().newAggregateStrategyAnnot(typeElement);
    if (aggregateStrategyAnnot == null) {
      throw new AptIllegalStateException(
          "aggregateStrategyAnnot is null. typeElement=" + typeElement);
    }
    if (!typeElement.getKind().isInterface()) {
      throw new AptException(Message.DOMA4482, typeElement, new Object[] {});
    }
    if (typeElement.getModifiers().contains(Modifier.PRIVATE)) {
      throw new AptException(Message.DOMA4483, typeElement, new Object[] {});
    }
    EntityCtType root = ctx.getCtTypes().newEntityCtType(aggregateStrategyAnnot.getRootValue());
    if (root == null) {
      throw new AptException(Message.DOMA4478, typeElement, new Object[] {});
    }
    List<AssociationLinkerMeta> associationLinkerMetas =
        findAssociationLinkerMetas(typeElement, root);
    validateTableAliases(aggregateStrategyAnnot.getTableAliasValue(), associationLinkerMetas);
    return new AggregateStrategyMeta(
        root, aggregateStrategyAnnot.getTableAliasValue(), associationLinkerMetas);
  }

  private void validateTableAliases(
      String rootTableAlias, List<AssociationLinkerMeta> associationLinkerMetas) {
    Set<String> seen = new HashSet<>(1 + associationLinkerMetas.size());
    seen.add(rootTableAlias);
    for (AssociationLinkerMeta linkerMeta : associationLinkerMetas) {
      if (!seen.add(linkerMeta.tableAlias())) {
        throw new AptException(
            Message.DOMA4481, linkerMeta.filedElement(), new Object[] {linkerMeta.tableAlias()});
      }
    }
  }

  private List<AssociationLinkerMeta> findAssociationLinkerMetas(
      TypeElement aggregateStrategyElement, EntityCtType root) {
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

    if (associationLinkerMetas.isEmpty()) {
      ctx.getReporter()
          .report(
              Diagnostic.Kind.WARNING,
              Message.DOMA4469,
              aggregateStrategyElement,
              new Object[] {aggregateStrategyElement});
    }

    return associationLinkerMetas;
  }

  private AssociationLinkerAnnot getAssociationLinkerAnnot(VariableElement linkerElement) {
    AssociationLinkerAnnot associationLinkerAnnot =
        ctx.getAnnotations().newAssociationLinkerAnnot(linkerElement);
    if (associationLinkerAnnot == null) {
      return null;
    }
    if (associationLinkerAnnot.getPropertyPathValue().isBlank()) {
      throw new AptException(
          Message.DOMA4472,
          linkerElement,
          associationLinkerAnnot.getAnnotationMirror(),
          associationLinkerAnnot.getPropertyPath(),
          new Object[] {});
    }
    if (associationLinkerAnnot.getTableAliasValue().isBlank()) {
      throw new AptException(
          Message.DOMA4468,
          linkerElement,
          associationLinkerAnnot.getAnnotationMirror(),
          associationLinkerAnnot.getTableAlias(),
          new Object[] {});
    }
    return associationLinkerAnnot;
  }

  private void validateModifiers(VariableElement linkerElement) {
    if (!linkerElement.getModifiers().contains(Modifier.STATIC)) {
      throw new AptException(Message.DOMA4464, linkerElement, new Object[] {});
    }
    if (!linkerElement.getModifiers().contains(Modifier.PUBLIC)) {
      throw new AptException(Message.DOMA4470, linkerElement, new Object[] {});
    }
    if (!linkerElement.getModifiers().contains(Modifier.FINAL)) {
      throw new AptException(Message.DOMA4471, linkerElement, new Object[] {});
    }
  }

  private BiFunctionMeta createBiFunctionMeta(VariableElement linkerElement) {
    BiFunctionCtType ctType = ctx.getCtTypes().newBiFunctionCtType(linkerElement.asType());
    if (ctType == null) {
      throw new AptException(Message.DOMA4465, linkerElement, new Object[] {});
    }
    CtTypeVisitor<EntityCtType, String, RuntimeException> entityCtTypeVisitor =
        new SimpleCtTypeVisitor<>() {
          @Override
          protected EntityCtType defaultAction(CtType ctType, String ordinalNumber)
              throws RuntimeException {
            throw new AptException(Message.DOMA4466, linkerElement, new Object[] {ordinalNumber});
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
      throw new AptException(Message.DOMA4467, linkerElement, new Object[] {});
    }

    return new BiFunctionMeta(source, target, result);
  }

  private void validateAssociation(
      EntityCtType root,
      AssociationLinkerAnnot associationLinkerAnnot,
      BiFunctionMeta biFunctionMeta,
      VariableElement linkerElement) {
    TypeMirror source = root.getType();
    TypeMirror target = root.getType();
    String[] segments = associationLinkerAnnot.getPropertyPathValue().split("\\.");
    if (segments.length == 0) {
      throw new AptIllegalStateException("fragments must not be empty.");
    }
    for (String segment : segments) {
      source = target;
      target = resolveEntity(source, segment, linkerElement, associationLinkerAnnot);
    }
    if (!ctx.getMoreTypes().isSameType(biFunctionMeta.source.getType(), source)) {
      throw new AptException(
          Message.DOMA4475, linkerElement, new Object[] {biFunctionMeta.source.getType(), source});
    }
    if (!ctx.getMoreTypes().isSameType(biFunctionMeta.target.getType(), target)) {
      throw new AptException(
          Message.DOMA4476, linkerElement, new Object[] {biFunctionMeta.target.getType(), target});
    }
  }

  private TypeMirror resolveEntity(
      TypeMirror typeMirror,
      String name,
      VariableElement linkerElement,
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
          linkerElement,
          associationLinkerAnnot.getAnnotationMirror(),
          associationLinkerAnnot.getPropertyPath(),
          new Object[] {name, sourceElement.getQualifiedName()});
    }
    TypeMirror fieldType = field.get().asType();
    CtType ctType = ctx.getCtTypes().newCtType(fieldType);
    EntityCtType entityCtType =
        EntityCtType.resolveEntityCtType(
            ctType,
            () -> {
              throw new AptException(
                  Message.DOMA4477,
                  linkerElement,
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

  private record BiFunctionMeta(EntityCtType source, EntityCtType target, EntityCtType result) {}
}
