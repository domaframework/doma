package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.apt.reflection.ProcedureReflection;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author taedium
 * 
 */
public class AutoProcedureQueryMeta extends AutoModuleQueryMeta {

    private ProcedureReflection procedureReflection;

    public AutoProcedureQueryMeta(ExecutableElement method) {
        super(method);
    }

    public ProcedureReflection getProcedureReflection() {
        return procedureReflection;
    }

    public void setProcedureReflection(ProcedureReflection procedureReflection) {
        this.procedureReflection = procedureReflection;
    }

    public String getCatalogName() {
        return procedureReflection.getCatalogValue();
    }

    public String getSchemaName() {
        return procedureReflection.getSchemaValue();
    }

    public String getProcedureName() {
        return procedureReflection.getNameValue();
    }

    public boolean isQuoteRequired() {
        return procedureReflection.getQuoteValue();
    }

    public int getQueryTimeout() {
        return procedureReflection.getQueryTimeoutValue();
    }

    @Override
    public MapKeyNamingType getMapKeyNamingType() {
        return procedureReflection.getMapKeyNamingValue();
    }

    public SqlLogType getSqlLogType() {
        return procedureReflection.getSqlLogValue();
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitAutoProcedureQueryMeta(this, p);
    }

}
