package org.seasar.doma.internal.apt.meta;


/**
 * @author taedium
 * 
 */
public class SqlFileBatchModifyQueryMeta extends AbstractSqlFileQueryMeta {

    protected String elementTypeName;

    protected String entityListTypeName;

    protected String entityListName;

    public String getElementTypeName() {
        return elementTypeName;
    }

    public void setElementTypeName(String elementTypeName) {
        this.elementTypeName = elementTypeName;
    }

    public String getEntityListTypeName() {
        return entityListTypeName;
    }

    public void setEntityListTypeName(String entityListTypeName) {
        this.entityListTypeName = entityListTypeName;
    }

    public String getEntityListName() {
        return entityListName;
    }

    public void setEntityListName(String entityListName) {
        this.entityListName = entityListName;
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitSqlFileBatchModifyQueryMeta(this, p);
    }

}
