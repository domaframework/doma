package org.seasar.doma.internal.apt.meta.id;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.apt.reflection.TableGeneratorReflection;

/**
 * @author taedium
 * 
 */
public class TableIdGeneratorMeta implements IdGeneratorMeta {

    private final TableGeneratorReflection tableGeneratorReflection;

    public TableIdGeneratorMeta(TableGeneratorReflection tableGeneratorReflection) {
        assertNotNull(tableGeneratorReflection);
        this.tableGeneratorReflection = tableGeneratorReflection;
    }

    public String getQualifiedTableName() {
        StringBuilder buf = new StringBuilder();
        String catalogName = tableGeneratorReflection.getCatalogValue();
        if (!catalogName.isEmpty()) {
            buf.append(catalogName);
            buf.append(".");
        }
        String schemaName = tableGeneratorReflection.getCatalogValue();
        if (!schemaName.isEmpty()) {
            buf.append(schemaName);
            buf.append(".");
        }
        buf.append(tableGeneratorReflection.getTableValue());
        return buf.toString();
    }

    public String getPkColumnName() {
        return tableGeneratorReflection.getPkColumnNameValue();
    }

    public String getValueColumnName() {
        return tableGeneratorReflection.getValueColumnNameValue();
    }

    public String getPkColumnValue() {
        return tableGeneratorReflection.getPkColumnValueValue();
    }

    public long getInitialValue() {
        return tableGeneratorReflection.getInitialValueValue();
    }

    public long getAllocationSize() {
        return tableGeneratorReflection.getAllocationSizeValue();
    }

    @Override
    public String getIdGeneratorClassName() {
        return tableGeneratorReflection.getImplementerValue().toString();
    }

    @Override
    public <P> void accept(IdGeneratorMetaVisitor<P> visitor, P p) {
        visitor.visitTableIdGeneratorMeta(this, p);
    }
}
