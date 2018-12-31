package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.ColumnAnnot;
import org.seasar.doma.internal.apt.cttype.CtType;

public class EmbeddablePropertyMeta {

  protected final TypeMirror type;

  protected final String typeName;

  protected final String boxedTypeName;

  protected final String boxedClassName;

  protected String name;

  protected ColumnAnnot columnAnnot;

  protected CtType ctType;

  public EmbeddablePropertyMeta(VariableElement fieldElement, Context ctx) {
    assertNotNull(fieldElement, ctx);
    this.type = fieldElement.asType();
    this.typeName = ctx.getTypes().getTypeName(type);
    this.boxedTypeName = ctx.getTypes().getBoxedTypeName(type);
    this.boxedClassName = ctx.getTypes().getBoxedClassName(type);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ColumnAnnot getColumnAnnot() {
    return columnAnnot;
  }

  public void setColumnAnnot(ColumnAnnot columnAnnot) {
    this.columnAnnot = columnAnnot;
  }

  public String getColumnName() {
    return columnAnnot != null ? columnAnnot.getNameValue() : "";
  }

  public boolean isColumnInsertable() {
    return columnAnnot != null ? columnAnnot.getInsertableValue() : true;
  }

  public boolean isColumnUpdatable() {
    return columnAnnot != null ? columnAnnot.getUpdatableValue() : true;
  }

  public boolean isColumnQuoteRequired() {
    return columnAnnot != null ? columnAnnot.getQuoteValue() : false;
  }

  public CtType getCtType() {
    return ctType;
  }

  public void setCtType(CtType ctType) {
    this.ctType = ctType;
  }

  public TypeMirror getType() {
    return type;
  }

  public String getTypeName() {
    return typeName;
  }

  public String getBoxedTypeName() {
    return boxedTypeName;
  }

  public String getBoxedClassName() {
    return boxedClassName;
  }
}
