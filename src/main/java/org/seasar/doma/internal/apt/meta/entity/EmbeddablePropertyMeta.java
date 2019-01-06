package org.seasar.doma.internal.apt.meta.entity;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.annot.ColumnAnnot;
import org.seasar.doma.internal.apt.cttype.CtType;

public class EmbeddablePropertyMeta {

  private final CtType ctType;

  private String name;

  private ColumnAnnot columnAnnot;

  public EmbeddablePropertyMeta(CtType ctType) {
    this.ctType = ctType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setColumnAnnot(ColumnAnnot columnAnnot) {
    this.columnAnnot = columnAnnot;
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

  public CtType getCtType() {
    return ctType;
  }

  public TypeMirror getType() {
    return ctType.getType();
  }

  public String getTypeName() {
    return ctType.getTypeName();
  }

  public String getBoxedTypeName() {
    return ctType.getBoxedTypeName();
  }

  public String getBoxedClassName() {
    return ctType.getBoxedClassName();
  }
}
