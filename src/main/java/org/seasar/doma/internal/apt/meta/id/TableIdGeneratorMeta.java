package org.seasar.doma.internal.apt.meta.id;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.apt.annot.TableGeneratorAnnot;

public class TableIdGeneratorMeta implements IdGeneratorMeta {

  private final TableGeneratorAnnot tableGeneratorAnnot;

  public TableIdGeneratorMeta(TableGeneratorAnnot tableGeneratorAnnot) {
    assertNotNull(tableGeneratorAnnot);
    this.tableGeneratorAnnot = tableGeneratorAnnot;
  }

  public String getQualifiedTableName() {
    var buf = new StringBuilder();
    var catalogName = tableGeneratorAnnot.getCatalogValue();
    if (!catalogName.isEmpty()) {
      buf.append(catalogName);
      buf.append(".");
    }
    var schemaName = tableGeneratorAnnot.getCatalogValue();
    if (!schemaName.isEmpty()) {
      buf.append(schemaName);
      buf.append(".");
    }
    buf.append(tableGeneratorAnnot.getTableValue());
    return buf.toString();
  }

  public String getPkColumnName() {
    return tableGeneratorAnnot.getPkColumnNameValue();
  }

  public String getValueColumnName() {
    return tableGeneratorAnnot.getValueColumnNameValue();
  }

  public String getPkColumnValue() {
    return tableGeneratorAnnot.getPkColumnValueValue();
  }

  public long getInitialValue() {
    return tableGeneratorAnnot.getInitialValueValue();
  }

  public long getAllocationSize() {
    return tableGeneratorAnnot.getAllocationSizeValue();
  }

  @Override
  public String getIdGeneratorClassName() {
    return tableGeneratorAnnot.getImplementerValue().toString();
  }

  @Override
  public <P> void accept(IdGeneratorMetaVisitor<P> visitor, P p) {
    visitor.visitTableIdGeneratorMeta(this, p);
  }
}
