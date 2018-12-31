package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.entity.NamingType;

public class EntityAnnot {

  protected final AnnotationMirror annotationMirror;

  protected AnnotationValue listener;

  protected AnnotationValue naming;

  protected AnnotationValue immutable;

  public EntityAnnot(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }

  public AnnotationValue getListener() {
    return listener;
  }

  public AnnotationValue getNaming() {
    return naming;
  }

  public AnnotationValue getImmutable() {
    return immutable;
  }

  public TypeMirror getListenerValue() {
    TypeMirror result = AnnotationValueUtil.toType(listener);
    if (result == null) {
      throw new AptIllegalStateException("listener");
    }
    return result;
  }

  public NamingType getNamingValue() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(naming);
    if (enumConstant == null) {
      throw new AptIllegalStateException("naming");
    }
    return NamingType.valueOf(enumConstant.getSimpleName().toString());
  }

  public boolean getImmutableValue() {
    Boolean result = AnnotationValueUtil.toBoolean(immutable);
    if (result == null) {
      throw new AptIllegalStateException("immutable");
    }
    return result.booleanValue();
  }

  public static EntityAnnot newInstance(TypeElement clazz, Context ctx) {
    assertNotNull(ctx);
    AnnotationMirror annotationMirror = ctx.getElements().getAnnotationMirror(clazz, Entity.class);
    if (annotationMirror == null) {
      return null;
    }
    EntityAnnot result = new EntityAnnot(annotationMirror);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        ctx.getElements().getElementValuesWithDefaults(annotationMirror).entrySet()) {
      String name = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      if ("listener".equals(name)) {
        result.listener = value;
      } else if ("naming".equals(name)) {
        result.naming = value;
      } else if ("immutable".equals(name)) {
        result.immutable = value;
      }
    }
    return result;
  }
}
