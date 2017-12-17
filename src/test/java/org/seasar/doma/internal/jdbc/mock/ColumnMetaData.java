package org.seasar.doma.internal.jdbc.mock;

/**
 * 
 * @author taedium
 * 
 */
public class ColumnMetaData {

    protected final String label;

    public ColumnMetaData(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
