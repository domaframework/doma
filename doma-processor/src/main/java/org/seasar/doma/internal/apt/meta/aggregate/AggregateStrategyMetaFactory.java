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
package org.seasar.doma.internal.apt.meta.aggregate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import org.seasar.doma.Association;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.annot.AggregateStrategyAnnot;
import org.seasar.doma.internal.apt.annot.AssociationLinkerAnnot;
import org.seasar.doma.internal.apt.cttype.BiConsumerCtType;
import org.seasar.doma.internal.apt.cttype.BiFunctionCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.CtTypeVisitor;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.message.Message;

public class AggregateStrategyMetaFactory implements TypeElementMetaFactory<AggregateStrategyMeta> {

  private final RoundContext ctx;

  public AggregateStrategyMetaFactory(RoundContext ctx) {
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
    if (!typeElement.getInterfaces().isEmpty()) {
      throw new AptException(Message.DOMA4487, typeElement, new Object[] {});
    }
    EntityCtType root = ctx.getCtTypes().newEntityCtType(aggregateStrategyAnnot.getRootValue());
    if (root == null) {
      throw new AptException(Message.DOMA4478, typeElement, new Object[] {});
    }
    List<AssociationLinkerMeta> associationLinkerMetas = findAssociationLinkerMetas(typeElement);
    validateAllPropertyPaths(associationLinkerMetas);
    validateAllTableAliases(aggregateStrategyAnnot.getTableAliasValue(), associationLinkerMetas);
    validateNavigation(root, associationLinkerMetas);
    return new AggregateStrategyMeta(
        typeElement, root, aggregateStrategyAnnot.getTableAliasValue(), associationLinkerMetas);
  }

  private void validateAllPropertyPaths(List<AssociationLinkerMeta> associationLinkerMetas) {
    // validate uniqueness
    Set<String> seen = new HashSet<>(associationLinkerMetas.size());
    for (AssociationLinkerMeta linkerMeta : associationLinkerMetas) {
      if (!seen.add(linkerMeta.propertyPath())) {
        throw new AptException(
            Message.DOMA4489,
            linkerMeta.filedElement(),
            linkerMeta.associationLinkerAnnot().getAnnotationMirror(),
            linkerMeta.associationLinkerAnnot().getPropertyPath(),
            new Object[] {linkerMeta.propertyPath()});
      }
    }

    // validate navigation
    Map<String, AssociationLinkerMeta> map =
        associationLinkerMetas.stream()
            .collect(Collectors.toMap(AssociationLinkerMeta::propertyPath, Function.identity()));
    List<AssociationLinkerMeta> validationTargets =
        associationLinkerMetas.stream().filter(it -> it.propertyPathDepth() > 1).toList();
    for (AssociationLinkerMeta linkerMeta : validationTargets) {
      if (!map.containsKey(linkerMeta.ancestorPath())) {
        throw new AptException(
            Message.DOMA4488,
            linkerMeta.filedElement(),
            linkerMeta.associationLinkerAnnot().getAnnotationMirror(),
            linkerMeta.associationLinkerAnnot().getPropertyPath(),
            new Object[] {linkerMeta.propertyPath(), linkerMeta.ancestorPath()});
      }
    }
  }

  private void validateAllTableAliases(
      String rootTableAlias, List<AssociationLinkerMeta> associationLinkerMetas) {
    Set<String> seen = new HashSet<>(1 + associationLinkerMetas.size());
    seen.add(rootTableAlias);
    for (AssociationLinkerMeta linkerMeta : associationLinkerMetas) {
      if (!seen.add(linkerMeta.tableAlias())) {
        throw new AptException(
            Message.DOMA4481,
            linkerMeta.filedElement(),
            linkerMeta.associationLinkerAnnot().getAnnotationMirror(),
            linkerMeta.associationLinkerAnnot().getTableAlias(),
            new Object[] {linkerMeta.tableAlias()});
      }
    }
  }

  private void validateNavigation(
      EntityCtType root, List<AssociationLinkerMeta> associationLinkerMetas) {
    for (AssociationLinkerMeta linkerMeta : associationLinkerMetas) {
      TypeMirror source = root.getType();
      TypeMirror target = root.getType();
      for (String segment : linkerMeta.propertyPathSegments()) {
        source = target;
        target =
            resolveEntity(
                source, segment, linkerMeta.filedElement(), linkerMeta.associationLinkerAnnot());
      }
      if (!ctx.getMoreTypes().isSameType(linkerMeta.source().getType(), source)) {
        throw new AptException(
            Message.DOMA4475,
            linkerMeta.filedElement(),
            new Object[] {linkerMeta.source().getType(), source});
      }
      if (!ctx.getMoreTypes().isSameType(linkerMeta.target().getType(), target)) {
        throw new AptException(
            Message.DOMA4476,
            linkerMeta.filedElement(),
            new Object[] {linkerMeta.target().getType(), target});
      }
    }
  }

  private List<AssociationLinkerMeta> findAssociationLinkerMetas(
      TypeElement aggregateStrategyElement) {
    List<AssociationLinkerMeta> associationLinkerMetas = new ArrayList<>();

    for (VariableElement fieldElement :
        ElementFilter.fieldsIn(aggregateStrategyElement.getEnclosedElements())) {
      AssociationLinkerAnnot associationLinkerAnnot = getAssociationLinkerAnnot(fieldElement);
      if (associationLinkerAnnot == null) {
        continue;
      }
      validateModifiers(fieldElement);
      AssociationLinkerMeta associationLinkerMeta =
          createAssociationLinkerMeta(
              fieldElement, associationLinkerAnnot, aggregateStrategyElement);
      associationLinkerMetas.add(associationLinkerMeta);
    }

    if (associationLinkerMetas.isEmpty()) {
      ctx.getReporter()
          .report(
              Diagnostic.Kind.WARNING,
              Message.DOMA4469,
              aggregateStrategyElement,
              new Object[] {aggregateStrategyElement});
      return associationLinkerMetas;
    }

    Comparator<AssociationLinkerMeta> reversedComparator =
        Comparator.comparingInt(AssociationLinkerMeta::propertyPathDepth).reversed();

    return associationLinkerMetas.stream().sorted(reversedComparator).toList();
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

  private TypeMirror resolveEntity(
      TypeMirror typeMirror,
      String name,
      VariableElement linkerElement,
      AssociationLinkerAnnot associationLinkerAnnot) {
    TypeElement sourceElement = ctx.getMoreTypes().toTypeElement(typeMirror);
    if (sourceElement == null) {
      throw new AptIllegalStateException("sourceElement is null. typeMirror=" + typeMirror);
    }
    Optional<VariableElement> field = findAssociationField(sourceElement, name);
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
    EntityCtType entityCtType = EntityCtType.resolveEntityCtType(ctType);
    if (entityCtType == null) {
      throw new AptException(
          Message.DOMA4477,
          linkerElement,
          new Object[] {name, sourceElement.getQualifiedName(), fieldType});
    }
    return entityCtType.getType();
  }

  private Optional<VariableElement> findAssociationField(TypeElement typeElement, String name) {
    for (TypeElement t = typeElement;
        t != null && t.asType().getKind() != TypeKind.NONE;
        t = ctx.getMoreTypes().toTypeElement(t.getSuperclass())) {
      if (t.getAnnotation(Entity.class) == null) {
        continue;
      }
      Optional<VariableElement> field =
          ElementFilter.fieldsIn(t.getEnclosedElements()).stream()
              .filter(it -> it.getSimpleName().toString().equals(name))
              .findFirst();
      if (field.isPresent()) {
        VariableElement f = field.get();
        if (f.getAnnotation(Association.class) == null) {
          throw new AptException(Message.DOMA4486, field.get(), new Object[] {name, f.asType()});
        } else {
          return field;
        }
      }
    }
    return Optional.empty();
  }

  private AssociationLinkerMeta createAssociationLinkerMeta(
      VariableElement fieldElement,
      AssociationLinkerAnnot associationLinkerAnnot,
      TypeElement aggregateStrategyElement) {

    String propertyPath = associationLinkerAnnot.getPropertyPathValue();
    List<String> propertyPathSegments = Arrays.stream(propertyPath.split("\\.")).toList();
    int propertyPathDepth = propertyPathSegments.size();
    String ancestorPath;
    if (propertyPathDepth == 0) {
      throw new AptIllegalStateException("propertyPath=" + propertyPath);
    } else if (propertyPathDepth == 1) {
      ancestorPath = "";
    } else {
      ancestorPath = propertyPath.substring(0, propertyPath.lastIndexOf('.'));
    }

    LinkMeta linkMeta = createLinkMeta(fieldElement);

    return new AssociationLinkerMeta(
        associationLinkerAnnot,
        ancestorPath,
        associationLinkerAnnot.getPropertyPathValue(),
        propertyPathSegments,
        propertyPathDepth,
        associationLinkerAnnot.getTableAliasValue(),
        linkMeta.kind,
        linkMeta.source,
        linkMeta.target,
        aggregateStrategyElement,
        fieldElement);
  }

  private LinkMeta createLinkMeta(VariableElement linkerElement) {
    BiFunctionCtType biFunctionCtType =
        ctx.getCtTypes().newBiFunctionCtType(linkerElement.asType());
    if (biFunctionCtType != null) {
      return createLinkMetaForBiFunction(linkerElement, biFunctionCtType);
    }
    BiConsumerCtType biConsumerCtType =
        ctx.getCtTypes().newBiConsumerCtType(linkerElement.asType());
    if (biConsumerCtType != null) {
      return createLinkMetaForBiConsumer(linkerElement, biConsumerCtType);
    }
    throw new AptException(Message.DOMA4465, linkerElement, new Object[] {});
  }

  private LinkMeta createLinkMetaForBiFunction(
      VariableElement linkerElement, BiFunctionCtType ctType) {
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

    return new LinkMeta(AssociationLinkerKind.BI_FUNCTION, source, target);
  }

  private LinkMeta createLinkMetaForBiConsumer(
      VariableElement linkerElement, BiConsumerCtType ctType) {
    CtTypeVisitor<EntityCtType, String, RuntimeException> entityCtTypeVisitor =
        new SimpleCtTypeVisitor<>() {
          @Override
          protected EntityCtType defaultAction(CtType ctType, String ordinalNumber)
              throws RuntimeException {
            throw new AptException(Message.DOMA4497, linkerElement, new Object[] {ordinalNumber});
          }

          @Override
          public EntityCtType visitEntityCtType(EntityCtType ctType, String ordinalNumber)
              throws RuntimeException {
            return ctType;
          }
        };

    EntityCtType source = ctType.getFirstArgCtType().accept(entityCtTypeVisitor, "first");
    EntityCtType target = ctType.getSecondArgCtType().accept(entityCtTypeVisitor, "second");

    return new LinkMeta(AssociationLinkerKind.BI_CONSUMER, source, target);
  }

  private record LinkMeta(AssociationLinkerKind kind, EntityCtType source, EntityCtType target) {}
}
