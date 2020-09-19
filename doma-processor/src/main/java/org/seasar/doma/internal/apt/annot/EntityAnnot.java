package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNonNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.entity.NamingType;

public class EntityAnnot extends AbstractAnnot {

  private static final String LISTENER = "listener";

  private static final String NAMING = "naming";

  private static final String IMMUTABLE = "immutable";

  static final String METAMODEL = "metamodel";

  private final AnnotationValue listener;

  private final AnnotationValue naming;

  private final AnnotationValue immutable;

  private final MetamodelAnnot metamodelAnnot;

  EntityAnnot(
      AnnotationMirror annotationMirror,
      MetamodelAnnot metamodelAnnot,
      Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.listener = assertNonNullValue(values, LISTENER);
    this.naming = assertNonNullValue(values, NAMING);
    this.immutable = assertNonNullValue(values, IMMUTABLE);
    this.metamodelAnnot = metamodelAnnot;
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
      throw new AptIllegalStateException(LISTENER);
    }
    return result;
  }

  public NamingType getNamingValue() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(naming);
    if (enumConstant == null) {
      throw new AptIllegalStateException(NAMING);
    }
    return NamingType.valueOf(enumConstant.getSimpleName().toString());
  }

  public boolean getImmutableValue() {
    Boolean result = AnnotationValueUtil.toBoolean(immutable);
    if (result == null) {
      throw new AptIllegalStateException(IMMUTABLE);
    }
    return result;
  }

  public MetamodelAnnot getMetamodelValue() {
    return metamodelAnnot;
  }
}
