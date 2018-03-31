package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.annot.ColumnAnnot;
import org.seasar.doma.internal.apt.cttype.CtType;

public abstract class AbstractPropertyMeta {

  protected final String name;

  protected final VariableElement fieldElement;

  protected final CtType ctType;

  protected final ColumnAnnot columnAnnot;

  protected AbstractPropertyMeta(
      VariableElement fieldElement, String name, CtType ctType, ColumnAnnot columnAnnot) {
    assertNotNull(fieldElement, name, ctType);
    this.fieldElement = fieldElement;
    this.name = name;
    this.ctType = ctType;
    this.columnAnnot = columnAnnot;
  }

  public String getName() {
    return name;
  }

  public String getTypeName() {
    return ctType.getTypeName();
  }

  public CtType getCtType() {
    return ctType;
  }

  public TypeMirror getType() {
    return ctType.getType();
  }

  public ColumnAnnot getColumnAnnot() {
    return columnAnnot;
  }

  public String getColumnName() {
    return columnAnnot != null ? columnAnnot.getNameValue() : "";
  }

  public boolean isColumnInsertable() {
    return columnAnnot == null || columnAnnot.getInsertableValue();
  }

  public boolean isColumnUpdatable() {
    return columnAnnot == null || columnAnnot.getUpdatableValue();
  }

  public boolean isColumnQuoteRequired() {
    return columnAnnot != null && columnAnnot.getQuoteValue();
  }
}
