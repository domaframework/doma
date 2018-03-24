package org.seasar.doma.internal.apt.meta.query;

import java.util.List;

import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.reflection.ModifyReflection;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author taedium
 * 
 */
public class SqlFileModifyQueryMeta extends AbstractSqlFileQueryMeta {

    private EntityCtType entityCtType;

    private String entityParameterName;

    private ModifyReflection modifyReflection;

    public SqlFileModifyQueryMeta(ExecutableElement method) {
        super(method);
    }

    public EntityCtType getEntityCtType() {
        return entityCtType;
    }

    public void setEntityCtType(EntityCtType entityCtType) {
        this.entityCtType = entityCtType;
    }

    public String getEntityParameterName() {
        return entityParameterName;
    }

    public void setEntityParameterName(String entityParameterName) {
        this.entityParameterName = entityParameterName;
    }

    public ModifyReflection getModifyReflection() {
        return modifyReflection;
    }

    public void setModifyReflection(ModifyReflection modifyReflection) {
        this.modifyReflection = modifyReflection;
    }

    public int getQueryTimeout() {
        return modifyReflection.getQueryTimeoutValue();
    }

    public Boolean getIgnoreVersion() {
        return modifyReflection.getIgnoreVersionValue();
    }

    public Boolean getExcludeNull() {
        return modifyReflection.getExcludeNullValue();
    }

    public Boolean getSuppressOptimisticLockException() {
        return modifyReflection.getSuppressOptimisticLockExceptionValue();
    }

    public Boolean getIncludeUnchanged() {
        return modifyReflection.getIncludeUnchangedValue();
    }

    public List<String> getInclude() {
        return modifyReflection.getIncludeValue();
    }

    public List<String> getExclude() {
        return modifyReflection.getExcludeValue();
    }

    public SqlLogType getSqlLogType() {
        return modifyReflection.getSqlLogValue();
    }

    public boolean isPopulatable() {
        return entityCtType != null && queryKind == QueryKind.SQLFILE_UPDATE;
    }

    @Override
    public <P> void accept(QueryMetaVisitor<P> visitor, P p) {
        visitor.visitSqlFileModifyQueryMeta(this, p);
    }
}
