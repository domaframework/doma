package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;

public class SequenceGeneratorAnnot {

  protected final AnnotationMirror annotationMirror;

  protected AnnotationValue catalog;

  protected AnnotationValue schema;

  protected AnnotationValue sequence;

  protected AnnotationValue initialValue;

  protected AnnotationValue allocationSize;

  protected AnnotationValue implementer;

  protected SequenceGeneratorAnnot(AnnotationMirror annotationMirror) {
    assertNotNull(annotationMirror);
    this.annotationMirror = annotationMirror;
  }

  public static SequenceGeneratorAnnot newInstance(
      VariableElement field, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror =
        ElementUtil.getAnnotationMirror(field, SequenceGenerator.class, env);
    if (annotationMirror == null) {
      return null;
    }
    SequenceGeneratorAnnot result = new SequenceGeneratorAnnot(annotationMirror);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        env.getElementUtils().getElementValuesWithDefaults(annotationMirror).entrySet()) {
      String name = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      if ("catalog".equals(name)) {
        result.catalog = value;
      } else if ("schema".equals(name)) {
        result.schema = value;
      } else if ("sequence".equals(name)) {
        result.sequence = value;
      } else if ("initialValue".equals(name)) {
        result.initialValue = value;
      } else if ("allocationSize".equals(name)) {
        result.allocationSize = value;
      } else if ("implementer".equals(name)) {
        result.implementer = value;
      }
    }
    return result;
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }

  public AnnotationValue getCatalog() {
    return catalog;
  }

  public AnnotationValue getSchema() {
    return schema;
  }

  public AnnotationValue getSequence() {
    return sequence;
  }

  public AnnotationValue getInitialValue() {
    return initialValue;
  }

  public AnnotationValue getAllocationSize() {
    return allocationSize;
  }

  public AnnotationValue getImplementer() {
    return implementer;
  }

  public String getCatalogValue() {
    String value = AnnotationValueUtil.toString(catalog);
    if (value == null) {
      throw new AptIllegalStateException("catalog");
    }
    return value;
  }

  public String getSchemaValue() {
    String value = AnnotationValueUtil.toString(schema);
    if (value == null) {
      throw new AptIllegalStateException("schema");
    }
    return value;
  }

  public String getSequenceValue() {
    String value = AnnotationValueUtil.toString(sequence);
    if (value == null) {
      throw new AptIllegalStateException("sequence");
    }
    return value;
  }

  public Long getInitialValueValue() {
    Long value = AnnotationValueUtil.toLong(initialValue);
    if (value == null) {
      throw new AptIllegalStateException("initialValue");
    }
    return value;
  }

  public Long getAllocationSizeValue() {
    Long value = AnnotationValueUtil.toLong(allocationSize);
    if (value == null) {
      throw new AptIllegalStateException("allocationSize");
    }
    return value;
  }

  public TypeMirror getImplementerValue() {
    TypeMirror value = AnnotationValueUtil.toType(implementer);
    if (value == null) {
      throw new AptIllegalStateException("implementer");
    }
    return value;
  }
}
