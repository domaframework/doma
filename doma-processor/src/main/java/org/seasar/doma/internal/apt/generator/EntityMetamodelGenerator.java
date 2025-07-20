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
import java.util.stream.Collectors;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.meta.entity.EmbeddedMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityFieldMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityPropertyMeta;
import org.seasar.doma.internal.apt.meta.entity.ScopeClassMeta;
import org.seasar.doma.internal.apt.meta.entity.ScopeMethodMeta;
import org.seasar.doma.internal.apt.meta.entity.ScopeParameterMeta;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.criteria.metamodel.DefaultPropertyMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.EntityTypeProxy;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityType;

public class EntityMetamodelGenerator extends AbstractGenerator {

  private final EntityMeta entityMeta;
  private final ClassName entityTypeName;

  public EntityMetamodelGenerator(
      RoundContext ctx,
      ClassName className,
      Printer printer,
      EntityMeta entityMeta,
      ClassName entityTypeName) {
    super(ctx, className, printer);
    assertNotNull(entityMeta, entityTypeName);
    this.entityMeta = entityMeta;
    this.entityTypeName = entityTypeName;
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
    iprint(
        "public final class %1$s implements %2$s<%3$s> {%n",
        /* 1 */ simpleName, /* 2 */ EntityMetamodel.class, /* 3 */ entityMeta.getType());
    print("%n");
    indent();
    printValidateVersionStaticInitializer();
    printFields();
    printConstructors();
    printMethods();
    unindent();
    iprint("}%n");
  }

  private void printFields() {
    printQualifiedTableNameField();
    printEntityTypeField();
    printAllPropertyMetamodelsFields();
    printPropertyMetamodelFields();
    printScopeFields();
  }

  private void printEntityTypeField() {
    iprint("private final %1$s __entityType = %1$s.getSingletonInternal();%n", entityTypeName);
    print("%n");
  }

  private void printAllPropertyMetamodelsFields() {
    iprint(
        "private final java.util.List<%1$s<?>> __allPropertyMetamodels;%n",
        PropertyMetamodel.class);
    print("%n");
  }

  private void printQualifiedTableNameField() {
    iprint("private final String __qualifiedTableName;%n");
    print("%n");
  }

  private void printScopeFields() {
    for (ScopeClassMeta scopeClassMeta : entityMeta.getScopeClassMetas()) {
      iprint(
          "private final %1$s %2$s = new %1$s();%n",
          scopeClassMeta.getTypeElement(), scopeClassMeta.getIdentifier());
      print("%n");
    }
  }

  private void printConstructors() {
    printNoArgsConstructor();
    printOneArgConstructor();
  }

  private void printNoArgsConstructor() {
    iprint("public %1$s() {%n", simpleName);
    indent();
    iprint("this(\"\");%n");
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printOneArgConstructor() {
    iprint("public %1$s(String qualifiedTableName) {%n", simpleName);
    indent();
    iprint("this.__qualifiedTableName = java.util.Objects.requireNonNull(qualifiedTableName);%n");
    iprint(
        "java.util.ArrayList<%1$s<?>> __list = new java.util.ArrayList<>(%2$s);%n",
        PropertyMetamodel.class, entityMeta.getAllFieldMetas().size());
    for (EntityFieldMeta f : entityMeta.getAllFieldMetas()) {
      if (f instanceof EmbeddedMeta embeddedMeta) {
        iprint("__list.addAll(%1$s.allPropertyMetamodels());%n", embeddedMeta.getName());
      } else if (f instanceof EntityPropertyMeta propertyMeta) {
        iprint("__list.add(%1$s);%n", propertyMeta.getName());
      }
    }
    iprint("__allPropertyMetamodels = java.util.Collections.unmodifiableList(__list);%n");
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printPropertyMetamodelFields() {
    UnwrapOptionalVisitor visitor = new UnwrapOptionalVisitor();
    for (EntityFieldMeta f : entityMeta.getAllFieldMetas()) {
      if (f instanceof EmbeddedMeta embeddedMeta) {
        ClassName className = createEmbeddableTypeClassName(embeddedMeta);
        iprint(
            "public final %1$s.Metamodel %2$s = new %1$s.Metamodel(__entityType, \"%2$s\");%n",
            /* 1 */ className, /* 2 */ embeddedMeta.getName());
      } else if (f instanceof EntityPropertyMeta propertyMeta) {
        Pair<CtType, TypeMirror> pair = propertyMeta.getCtType().accept(visitor, null);
        iprint(
            "public final %1$s<%2$s> %3$s = new %4$s<%2$s>(%5$s.class, __entityType, \"%3$s\");%n",
            /* 1 */ PropertyMetamodel.class,
            /* 2 */ pair.snd,
            /* 3 */ propertyMeta.getName(),
            /* 4 */ DefaultPropertyMetamodel.class,
            /* 5 */ pair.fst.getQualifiedName());
      } else {
        throw new AptIllegalStateException(f.getName());
      }
      print("%n");
    }
  }

  private ClassName createEmbeddableTypeClassName(EmbeddedMeta p) {
    TypeElement embeddableTypeElement = ctx.getMoreTypes().toTypeElement(p.getCtType().getType());
    if (embeddableTypeElement == null) {
      throw new AptIllegalStateException("embeddableTypeElement");
    }
    Name binaryName = ctx.getMoreElements().getBinaryName(embeddableTypeElement);
    return ClassNames.newEmbeddableTypeClassName(binaryName);
  }

  private void printMethods() {
    printAsTypeMethod();
    printAllPropertyMetamodelsMethod();
    printScopeMethods();
  }

  private void printAsTypeMethod() {
    iprint("@Override%n");
    iprint("public %1$s<%2$s> asType() {%n", EntityType.class, entityMeta.getType());
    indent();
    iprint(
        "return __qualifiedTableName.isEmpty() ? __entityType : new %1$s<>(__entityType, __qualifiedTableName);%n",
        EntityTypeProxy.class);
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printAllPropertyMetamodelsMethod() {
    iprint("@Override%n");
    iprint("public java.util.List<%1$s<?>> allPropertyMetamodels() {%n", PropertyMetamodel.class);
    indent();
    iprint("return __allPropertyMetamodels;%n");
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printScopeMethods() {
    for (ScopeClassMeta scopeClassMeta : entityMeta.getScopeClassMetas()) {
      for (ScopeMethodMeta scopeMethodMeta : scopeClassMeta.getMethods()) {
        printScopeMethod(scopeClassMeta, scopeMethodMeta);
      }
    }
  }

  private void printScopeMethod(ScopeClassMeta clazz, ScopeMethodMeta method) {
    iprint(
        "public %1$s%2$s %3$s(%4$s) {%n",
        buildTypeParameters(method),
        method.getReturnType(),
        method.getName(),
        buildParameterList(method));
    indent();
    iprint("return %1$s.%2$s(this", clazz.getIdentifier(), method.getName());
    if (!method.getParameters().isEmpty()) {
      print(", ");
      print(buildArgumentList(method));
    }
    print(");%n");
    unindent();
    iprint("}%n");
    print("%n");
  }

  private String buildTypeParameters(ScopeMethodMeta method) {
    List<? extends TypeParameterElement> typeParameters = method.getTypeParameters();
    if (typeParameters.isEmpty()) {
      return "";
    }
    return typeParameters.stream()
        .map(this::buildTypeParameter)
        .collect(Collectors.joining(", ", "<", "> "));
  }

  private String buildTypeParameter(TypeParameterElement element) {
    return element.getSimpleName().toString()
        + " extends "
        + element.getBounds().stream().map(TypeMirror::toString).collect(Collectors.joining(" & "));
  }

  private String buildParameterList(ScopeMethodMeta method) {
    return String.join(", ", method.getParameters());
  }

  private String buildArgumentList(ScopeMethodMeta method) {
    return method.getParameters().stream()
        .map(ScopeParameterMeta::getName)
        .collect(Collectors.joining(", "));
  }
}
