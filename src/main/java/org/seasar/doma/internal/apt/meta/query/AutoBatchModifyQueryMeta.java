package org.seasar.doma.internal.apt.meta.query;

import java.util.List;

import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.reflection.BatchModifyReflection;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author taedium
 * 
 */
public class AutoBatchModifyQueryMeta extends AbstractQueryMeta {

    private EntityCtType entityCtType;

    private String entitiesParameterName;

    private BatchModifyReflection batchModifyReflection;

    public AutoBatchModifyQueryMeta(ExecutableElement method) {
        super(method);
    }

    public EntityCtType getEntityCtType() {
        return entityCtType;
    }

    public void setEntityCtType(EntityCtType entityCtType) {
        this.entityCtType = entityCtType;
    }

    public String getEntitiesParameterName() {
        return entitiesParameterName;
    }

    public void setEntitiesParameterName(String entitiesParameterName) {
        this.entitiesParameterName = entitiesParameterName;
    }

    BatchModifyReflection getBatchModifyReflection() {
        return batchModifyReflection;
    }

    void setBatchModifyReflection(BatchModifyReflection batchModifyReflection) {
        this.batchModifyReflection = batchModifyReflection;
    }

    public int getQueryTimeout() {
        return batchModifyReflection.getQueryTimeoutValue();
    }

    public int getBatchSize() {
        return batchModifyReflection.getBatchSizeValue();
    }

    public Boolean getIgnoreVersion() {
        return batchModifyReflection.getIgnoreVersionValue();
    }

    public Boolean getSuppressOptimisticLockException() {
        return batchModifyReflection.getSuppressOptimisticLockExceptionValue();
    }

    public List<String> getInclude() {
        return batchModifyReflection.getIncludeValue();
    }

    public List<String> getExclude() {
        return batchModifyReflection.getExcludeValue();
    }

    public SqlLogType getSqlLogType() {
        return batchModifyReflection.getSqlLogValue();
    }

    @Override
    public <P> void accept(QueryMetaVisitor<P> visitor, P p) {
        visitor.visitAutoBatchModifyQueryMeta(this, p);
    }

}
