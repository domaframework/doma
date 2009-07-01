package org.seasar.doma.internal.apt.meta;

/**
 * @author taedium
 * 
 */
public class ArrayCreateQueryMeta extends AbstractCreateQueryMeta {

    protected String jdbcTypeName;

    protected String arrayTypeName;

    protected String arrayName;

    public String getJdbcTypeName() {
        return jdbcTypeName;
    }

    public void setJdbcTypeName(String jdbcTypeName) {
        this.jdbcTypeName = jdbcTypeName;
    }

    public String getArrayTypeName() {
        return arrayTypeName;
    }

    public void setArrayTypeName(String arrayTypeName) {
        this.arrayTypeName = arrayTypeName;
    }

    public String getArrayName() {
        return arrayName;
    }

    public void setArrayName(String arrayName) {
        this.arrayName = arrayName;
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitArrayCreateQueryMeta(this, p);
    }
}
