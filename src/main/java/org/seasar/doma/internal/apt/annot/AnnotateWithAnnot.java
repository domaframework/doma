package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.AnnotateWith;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;

public class AnnotateWithAnnot {

  protected final javax.lang.model.element.AnnotationMirror annotationMirror;

  protected TypeElement ownerElement;

  protected AnnotationValue annotations;

  protected List<AnnotationAnnot> annotationsValue;

  protected AnnotateWithAnnot(
      javax.lang.model.element.AnnotationMirror annotationMirror, TypeElement ownerElement) {
    assertNotNull(annotationMirror, ownerElement);
    this.annotationMirror = annotationMirror;
    this.ownerElement = ownerElement;
  }

  public static AnnotateWithAnnot newInstance(TypeElement clazz, ProcessingEnvironment env) {
    assertNotNull(env);
    javax.lang.model.element.AnnotationMirror annotateWith =
        ElementUtil.getAnnotationMirror(clazz, AnnotateWith.class, env);
    TypeElement ownerElement = null;
    if (annotateWith == null) {
      for (javax.lang.model.element.AnnotationMirror annotationMirror :
          clazz.getAnnotationMirrors()) {
        ownerElement =
            ElementUtil.toTypeElement(annotationMirror.getAnnotationType().asElement(), env);
        if (ownerElement == null) {
          continue;
        }
        annotateWith = ElementUtil.getAnnotationMirror(ownerElement, AnnotateWith.class, env);
        if (annotateWith != null) {
          break;
        }
      }
      if (annotateWith == null) {
        return null;
      }
    } else {
      ownerElement = clazz;
    }
    AnnotateWithAnnot result = new AnnotateWithAnnot(annotateWith, ownerElement);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        env.getElementUtils().getElementValuesWithDefaults(annotateWith).entrySet()) {
      String name = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      if ("annotations".equals(name)) {
        result.annotations = value;
        result.annotationsValue = new ArrayList<AnnotationAnnot>();
        for (javax.lang.model.element.AnnotationMirror a :
            AnnotationValueUtil.toAnnotationList(value)) {
          result.annotationsValue.add(AnnotationAnnot.newInstance(a, env));
        }
      }
    }
    return result;
  }

  public TypeElement getOwnerElement() {
    return ownerElement;
  }

  public javax.lang.model.element.AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }

  public AnnotationValue getAnnotations() {
    return annotations;
  }

  public List<AnnotationAnnot> getAnnotationsValue() {
    return annotationsValue;
  }
}
