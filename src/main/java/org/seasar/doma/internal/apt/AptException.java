package org.seasar.doma.internal.apt;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;
import org.seasar.doma.DomaException;
import org.seasar.doma.message.MessageResource;

public class AptException extends DomaException {

  private static final long serialVersionUID = 1L;

  protected final Kind kind;

  protected final Element element;

  protected AnnotationMirror annotationMirror;

  protected AnnotationValue annotationValue;

  public AptException(MessageResource messageResource, Element element, Object[] args) {
    this(messageResource, Kind.ERROR, element, null, null, null, args);
  }

  public AptException(
      MessageResource messageResource, Element element, Throwable cause, Object[] args) {
    this(messageResource, Kind.ERROR, element, null, null, cause, args);
  }

  public AptException(
      MessageResource messageResource,
      Element element,
      AnnotationMirror annotationMirror,
      Object[] args) {
    this(messageResource, Kind.ERROR, element, annotationMirror, null, null, args);
  }

  public AptException(
      MessageResource messageResource,
      Element element,
      AnnotationMirror annotationMirror,
      AnnotationValue annotationValue,
      Object[] args) {
    this(messageResource, Kind.ERROR, element, annotationMirror, annotationValue, null, args);
  }

  private AptException(
      MessageResource messageResource,
      Kind kind,
      Element element,
      AnnotationMirror annotationMirror,
      AnnotationValue annotationValue,
      Throwable cause,
      Object[] args) {
    super(messageResource, cause, args);
    this.kind = kind;
    this.element = element;
    this.annotationMirror = annotationMirror;
    this.annotationValue = annotationValue;
  }

  public Kind getKind() {
    return kind;
  }

  public Element getElement() {
    return element;
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }

  public void setAnnotationMirror(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public AnnotationValue getAnnotationValue() {
    return annotationValue;
  }

  public void setAnnotationValue(AnnotationValue annotationValue) {
    this.annotationValue = annotationValue;
  }
}
