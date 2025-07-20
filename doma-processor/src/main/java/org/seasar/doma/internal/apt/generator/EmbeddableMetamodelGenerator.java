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

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableFieldMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddablePropertyMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddedMeta;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.criteria.metamodel.DefaultPropertyMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityType;

public class EmbeddableMetamodelGenerator extends AbstractGenerator {

  private final EmbeddableMeta embeddableMeta;

  public EmbeddableMetamodelGenerator(
      RoundContext ctx, ClassName className, Printer printer, EmbeddableMeta embeddableMeta) {
    super(ctx, className, printer);
    assertNotNull(embeddableMeta);
    this.embeddableMeta = embeddableMeta;
  }

  @Override
  public void generate() {
    printClass();
  }

  private void printClass() {
    iprint("/** */%n");
    iprint("public static final class Metamodel {%n");
    print("%n");
    indent();
    printFields();
    printConstructor();
    printMethods();
    unindent();
    iprint("}%n");
  }

  private void printFields() {
    printAllPropertyModelFields();
    printPropertyMetamodelFields();
  }

  private void printAllPropertyModelFields() {
    iprint(
        "private final java.util.List<%1$s<?>> __allPropertyMetamodels;%n",
        PropertyMetamodel.class);
    print("%n");
  }

  private void printPropertyMetamodelFields() {
    UnwrapOptionalVisitor visitor = new UnwrapOptionalVisitor();
    for (EmbeddableFieldMeta f : embeddableMeta.getEmbeddableFieldMetas()) {
      if (f instanceof EmbeddedMeta embeddedMeta) {
        ClassName className =
            createEmbeddableTypeClassName(embeddedMeta.embeddableMeta().getTypeElement());
        iprint(
            "public final %1$s.Metamodel %2$s;%n",
            /* 1 */ className, /* 2 */ embeddedMeta.getName());

      } else if (f instanceof EmbeddablePropertyMeta p) {
        Pair<CtType, TypeMirror> pair = p.getCtType().accept(visitor, null);
        iprint(
            "public final %1$s<%2$s> %3$s;%n",
            /* 1 */ PropertyMetamodel.class, /* 2 */ pair.snd, /* 3 */ p.getName());
      } else {
        throw new AptIllegalStateException(f.toString());
      }
      print("%n");
    }
  }

  private void printConstructor() {
    iprint("public Metamodel(%1$s<?> entityType, String name) {%n", EntityType.class);
    indent();
    UnwrapOptionalVisitor visitor = new UnwrapOptionalVisitor();
    for (EmbeddableFieldMeta f : embeddableMeta.getEmbeddableFieldMetas()) {
      if (f instanceof EmbeddedMeta embeddedMeta) {
        ClassName className =
            createEmbeddableTypeClassName(embeddedMeta.embeddableMeta().getTypeElement());
        iprint(
            "this.%2$s = new %1$s.Metamodel(entityType, name + \".%2$s\");%n",
            className, embeddedMeta.getName());
      } else if (f instanceof EmbeddablePropertyMeta propertyMeta) {
        Pair<CtType, TypeMirror> pair = propertyMeta.getCtType().accept(visitor, null);
        iprint(
            "this.%1$s = new %2$s<%3$s>(%4$s.class, entityType, name + \".%1$s\");%n",
            /* 1 */ propertyMeta.getName(),
            /* 2 */ DefaultPropertyMetamodel.class,
            /* 3 */ pair.snd,
            /* 4 */ pair.fst.getQualifiedName());
      }
    }
    iprint(
        "java.util.List<%1$s<?>> __list = new java.util.ArrayList<>(%2$s);%n",
        PropertyMetamodel.class, embeddableMeta.getEmbeddablePropertyMetas().size());
    for (EmbeddableFieldMeta f : embeddableMeta.getEmbeddableFieldMetas()) {
      if (f instanceof EmbeddedMeta embeddedMeta) {
        iprint("__list.addAll(this.%1$s.allPropertyMetamodels());%n", embeddedMeta.getName());
      } else if (f instanceof EmbeddablePropertyMeta propertyMeta) {
        iprint("__list.add(this.%1$s);%n", propertyMeta.getName());
      }
    }
    iprint("__allPropertyMetamodels = java.util.Collections.unmodifiableList(__list);%n");
    unindent();
    iprint("}%n");
    print("%n");
  }

  private void printMethods() {
    printAllPropertyMetamodelsMethod();
  }

  private void printAllPropertyMetamodelsMethod() {
    iprint("public java.util.List<%1$s<?>> allPropertyMetamodels() {%n", PropertyMetamodel.class);
    indent();
    iprint("return __allPropertyMetamodels;%n");
    unindent();
    iprint("}%n");
    print("%n");
  }

  private ClassName createEmbeddableTypeClassName(TypeElement embeddableTypeElement) {
    if (embeddableTypeElement == null) {
      throw new AptIllegalStateException("embeddableTypeElement");
    }
    Name binaryName = ctx.getMoreElements().getBinaryName(embeddableTypeElement);
    return ClassNames.newEmbeddableTypeClassName(binaryName);
  }
}
