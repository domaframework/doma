package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.reflection.ColumnReflection;

public abstract class AbstractPropertyMeta {

  protected final String name;

  protected final VariableElement fieldElement;

  protected final CtType ctType;

  protected final ColumnReflection columnReflection;

  protected AbstractPropertyMeta(
      VariableElement fieldElement, String name, CtType ctType, ColumnReflection columnReflection) {
    assertNotNull(fieldElement, name, ctType);
    this.fieldElement = fieldElement;
    this.name = name;
    this.ctType = ctType;
    this.columnReflection = columnReflection;
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

  public ColumnReflection getColumnReflection() {
    return columnReflection;
  }

  public String getColumnName() {
    return columnReflection != null ? columnReflection.getNameValue() : "";
  }

  public boolean isColumnInsertable() {
    return columnReflection == null || columnReflection.getInsertableValue();
  }

  public boolean isColumnUpdatable() {
    return columnReflection == null || columnReflection.getUpdatableValue();
  }

  public boolean isColumnQuoteRequired() {
    return columnReflection != null && columnReflection.getQuoteValue();
  }
}
