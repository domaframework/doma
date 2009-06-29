package org.seasar.doma.internal.apt.meta;

/**
 * @author taedium
 * 
 */
public class AutoProcedureQueryMeta extends AutoModuleQueryMeta {

    protected String procedureName;

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitAutoProcedureQueryMeta(this, p);
    }
}
