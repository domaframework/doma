package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.DomainConverters;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class DomainConvertersAnnot {

  protected final AnnotationMirror annotationMirror;

  protected final Context ctx;

  protected AnnotationValue value;

  protected DomainConvertersAnnot(AnnotationMirror annotationMirror, Context ctx) {
    assertNotNull(annotationMirror, ctx);
    this.annotationMirror = annotationMirror;
    this.ctx = ctx;
  }

  public static DomainConvertersAnnot newInstance(TypeElement typeElement, Context ctx) {
    assertNotNull(ctx);
    AnnotationMirror annotationMirror =
        ctx.getElements().getAnnotationMirror(typeElement, DomainConverters.class);
    if (annotationMirror == null) {
      return null;
    }
    DomainConvertersAnnot result = new DomainConvertersAnnot(annotationMirror, ctx);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        ctx.getElements().getElementValuesWithDefaults(annotationMirror).entrySet()) {
      String name = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      if ("value".equals(name)) {
        result.value = value;
      }
    }
    return result;
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }

  public AnnotationValue getValue() {
    return value;
  }

  public List<TypeMirror> getValueValue() {
    List<TypeMirror> typeList = AnnotationValueUtil.toTypeList(value);
    if (typeList == null) {
      throw new AptIllegalStateException("value");
    }
    return typeList;
  }
}
