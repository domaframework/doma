package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNonNullValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class MetamodelAnnot extends AbstractAnnot {

  private static final String PREFIX = "prefix";

  private static final String SUFFIX = "suffix";

  private static final String SCOPE = "scope";

  private final AnnotationValue prefix;

  private final AnnotationValue suffix;

  private final AnnotationValue scopeClasses;

  MetamodelAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.prefix = assertNonNullValue(values, PREFIX);
    this.suffix = assertNonNullValue(values, SUFFIX);
    this.scopeClasses = assertNonNullValue(values, SCOPE);
  }

  public AnnotationValue getPrefix() {
    return prefix;
  }

  public AnnotationValue getSuffix() {
    return suffix;
  }

  public String getPrefixValue() {
    String value = AnnotationValueUtil.toString(prefix);
    if (value == null) {
      throw new AptIllegalStateException(PREFIX);
    }
    return value;
  }

  public String getSuffixValue() {
    String value = AnnotationValueUtil.toString(suffix);
    if (value == null) {
      throw new AptIllegalStateException(SUFFIX);
    }
    return value;
  }

  public List<ScopeClass> getScopeValue() {
    List<TypeMirror> type = AnnotationValueUtil.toTypeList(scopeClasses);
    return type.stream().map(ScopeClass::new).collect(Collectors.toList());
  }
}
