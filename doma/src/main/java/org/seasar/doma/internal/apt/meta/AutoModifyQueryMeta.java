package org.seasar.doma.internal.apt.meta;


/**
 * @author taedium
 * 
 */
public class AutoModifyQueryMeta extends AbstractQueryMeta {

    protected String entityTypeName;

    protected String entityName;

    public String getEntityTypeName() {
        return entityTypeName;
    }

    public void setEntityTypeName(String entityTypeName) {
        this.entityTypeName = entityTypeName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visistAutoModifyQueryMeta(this, p);
    }

}
