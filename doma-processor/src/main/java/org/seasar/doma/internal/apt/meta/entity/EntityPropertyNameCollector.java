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
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.EmbeddableAnnot;

public class EntityPropertyNameCollector {

  private final Context ctx;

  public EntityPropertyNameCollector(Context ctx) {
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
