package org.seasar.doma.internal.apt.meta;

/**
 * 
 * @author taedium
 * 
 */
public class TableMeta {

    protected String catalog;

    protected String schema;

    protected String name;

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
