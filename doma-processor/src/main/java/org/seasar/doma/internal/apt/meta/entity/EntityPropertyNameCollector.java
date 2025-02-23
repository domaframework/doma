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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.HashSet;
import java.util.Set;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.Transient;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.annot.EmbeddableAnnot;

public class EntityPropertyNameCollector {

  private final RoundContext ctx;

  public EntityPropertyNameCollector(RoundContext ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  public Set<String> collect(TypeMirror entityType) {
    Set<String> names = new HashSet<>();
    collectNames(entityType, names);
    return names;
  }

  private void collectNames(TypeMirror type, Set<String> names) {
    for (TypeElement t = ctx.getMoreTypes().toTypeElement(type);
        t != null && t.asType().getKind() != TypeKind.NONE;
        t = ctx.getMoreTypes().toTypeElement(t.getSuperclass())) {
      for (VariableElement field : ElementFilter.fieldsIn(t.getEnclosedElements())) {
        if (!isPersistent(field)) {
          continue;
        }
        String name = field.getSimpleName().toString();
        TypeElement filedTypeElement = ctx.getMoreTypes().toTypeElement(field.asType());
        if (filedTypeElement == null) {
          names.add(name);
          continue;
        }
        EmbeddableAnnot embeddableAnnot = ctx.getAnnotations().newEmbeddableAnnot(filedTypeElement);
        if (embeddableAnnot == null) {
          names.add(name);
        } else {
          EmbeddableMetaFactory embeddableMetaFactory = new EmbeddableMetaFactory(ctx);
          EmbeddableMeta embeddableMeta =
              embeddableMetaFactory.createTypeElementMeta(filedTypeElement);
          for (EmbeddablePropertyMeta propertyMeta : embeddableMeta.getEmbeddablePropertyMetas()) {
            names.add(name + "." + propertyMeta.getName());
          }
        }
      }
    }
  }

  private boolean isPersistent(VariableElement field) {
    return field.getAnnotation(Transient.class) == null
        && !field.getModifiers().contains(Modifier.STATIC);
  }
}
