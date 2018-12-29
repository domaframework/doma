package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.Transient;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

public class EntityPropertyNameCollector {

  protected final ProcessingEnvironment env;

  public EntityPropertyNameCollector(ProcessingEnvironment env) {
    assertNotNull(env);
    this.env = env;
  }

  public Set<String> collect(TypeMirror entityType) {
    Set<String> names = new HashSet<String>();
    collectNames(entityType, names);
    return names;
  }

  protected void collectNames(TypeMirror type, Set<String> names) {
    for (TypeElement t = TypeMirrorUtil.toTypeElement(type, env);
        t != null && t.asType().getKind() != TypeKind.NONE;
        t = TypeMirrorUtil.toTypeElement(t.getSuperclass(), env)) {
      for (VariableElement field : ElementFilter.fieldsIn(t.getEnclosedElements())) {
        if (isPersistent(field)) {
          names.add(field.getSimpleName().toString());
        }
      }
    }
  }

  protected boolean isPersistent(VariableElement field) {
    return field.getAnnotation(Transient.class) == null
        && !field.getModifiers().contains(Modifier.STATIC);
  }
}
