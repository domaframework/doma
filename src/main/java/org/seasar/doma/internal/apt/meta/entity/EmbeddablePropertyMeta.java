package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.annot.ColumnAnnot;
import org.seasar.doma.internal.apt.cttype.CtType;

public class EmbeddablePropertyMeta extends AbstractPropertyMeta {

  public EmbeddablePropertyMeta(
      VariableElement fieldElement, String name, CtType ctType, ColumnAnnot columnAnnot) {
    super(fieldElement, name, ctType, columnAnnot);
    assertNotNull(fieldElement);
  }
}
