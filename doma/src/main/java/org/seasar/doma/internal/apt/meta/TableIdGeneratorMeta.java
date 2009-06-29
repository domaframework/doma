package org.seasar.doma.internal.apt.meta;

import org.seasar.doma.internal.jdbc.id.TableIdGenerator;

/**
 * @author taedium
 * 
 */
public class TableIdGeneratorMeta implements IdGeneratorMeta {

    protected final String qualifiedTableName;

    protected final String pkColumnName;

    protected final String valueColumnName;

    protected final String pkColumnValue;

    protected final int initialValue;

    protected final int allocationSize;

    public TableIdGeneratorMeta(String qualifiedTableName, String pkColumnName,
            String valueColumnName, String pkColumnValue, int initialValue,
            int allocationSize) {
        this.qualifiedTableName = qualifiedTableName;
        this.pkColumnName = pkColumnName;
        this.valueColumnName = valueColumnName;
        this.pkColumnValue = pkColumnValue;
        this.initialValue = initialValue;
        this.allocationSize = allocationSize;
    }

    public String getQualifiedTableName() {
        return qualifiedTableName;
    }

    public String getPkColumnName() {
        return pkColumnName;
    }

    public String getValueColumnName() {
        return valueColumnName;
    }

    public String getPkColumnValue() {
        return pkColumnValue;
    }

    public int getInitialValue() {
        return initialValue;
    }

    public int getAllocationSize() {
        return allocationSize;
    }

    @Override
    public String getIdGeneratorClassName() {
        return TableIdGenerator.class.getName();
    }

    @Override
    public <R, P> R accept(IdGeneratorMetaVisitor<R, P> visitor, P p) {
        return visitor.visistTableIdGeneratorMeta(this, p);
    }
}
