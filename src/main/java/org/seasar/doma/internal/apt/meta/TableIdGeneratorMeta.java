package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.mirror.TableGeneratorMirror;

/** @author taedium */
public class TableIdGeneratorMeta implements IdGeneratorMeta {

  protected final TableGeneratorMirror tableGeneratorMirror;

  public TableIdGeneratorMeta(TableGeneratorMirror tableGeneratorMirror) {
    assertNotNull(tableGeneratorMirror);
    this.tableGeneratorMirror = tableGeneratorMirror;
  }

  public String getQualifiedTableName() {
    StringBuilder buf = new StringBuilder();
    String catalogName = tableGeneratorMirror.getCatalogValue();
    if (!catalogName.isEmpty()) {
      buf.append(catalogName);
      buf.append(".");
    }
    String schemaName = tableGeneratorMirror.getCatalogValue();
    if (!schemaName.isEmpty()) {
      buf.append(schemaName);
      buf.append(".");
    }
    buf.append(tableGeneratorMirror.getTableValue());
    return buf.toString();
  }

  public String getPkColumnName() {
    return tableGeneratorMirror.getPkColumnNameValue();
  }

  public String getValueColumnName() {
    return tableGeneratorMirror.getValueColumnNameValue();
  }

  public String getPkColumnValue() {
    return tableGeneratorMirror.getPkColumnValueValue();
  }

  public long getInitialValue() {
    return tableGeneratorMirror.getInitialValueValue();
  }

  public long getAllocationSize() {
    return tableGeneratorMirror.getAllocationSizeValue();
  }

  @Override
  public String getIdGeneratorClassName() {
    return tableGeneratorMirror.getImplementerValue().toString();
  }

  @Override
  public <R, P> R accept(IdGeneratorMetaVisitor<R, P> visitor, P p) {
    return visitor.visistTableIdGeneratorMeta(this, p);
  }
}
