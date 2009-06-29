package org.seasar.doma.internal.apt.meta;

import javax.lang.model.type.TypeMirror;

import org.seasar.doma.jdbc.Options;
import org.seasar.doma.jdbc.SelectOptions;


/**
 * @author taedium
 * 
 */
public class SqlFileSelectQueryMeta extends AbstractSqlFileQueryMeta {

    protected String optionsTypeName;

    protected String optionsName;

    protected String iterationCallbackTypeName;

    protected String iterationCallbackName;

    protected TypeMirror iterationCallbackResultType;

    protected TypeMirror iterationCallbackTargetType;

    protected boolean singleResult;

    protected String entityTypeName;

    protected String domainTypeName;

    protected boolean iteration;

    protected Integer maxRows;

    protected Integer fetchSize;

    public String getIterationCallbackTypeName() {
        return iterationCallbackTypeName;
    }

    public void setIterationCallbackTypeName(String iterationCallbackTypeName) {
        this.iterationCallbackTypeName = iterationCallbackTypeName;
    }

    public String getIterationCallbackName() {
        return iterationCallbackName;
    }

    public void setIterationCallbackName(String iterationCallbackName) {
        this.iterationCallbackName = iterationCallbackName;
    }

    public TypeMirror getIterationCallbackResultType() {
        return iterationCallbackResultType;
    }

    public void setIterationCallbackResultType(
            TypeMirror iterationCallbackResultType) {
        this.iterationCallbackResultType = iterationCallbackResultType;
    }

    public TypeMirror getIterationCallbackTargetType() {
        return iterationCallbackTargetType;
    }

    public void setIterationCallbackTargetType(
            TypeMirror iterationCallbackTargetType) {
        this.iterationCallbackTargetType = iterationCallbackTargetType;
    }

    public String getOptionsTypeName() {
        return optionsTypeName;
    }

    public void setOptionsTypeName(String optionsTypeName) {
        this.optionsTypeName = optionsTypeName;
    }

    public String getOptionsName() {
        return optionsName;
    }

    public void setOptionsName(String optionsName) {
        this.optionsName = optionsName;
    }

    public boolean isSingleResult() {
        return singleResult;
    }

    public void setSingleResult(boolean singleResult) {
        this.singleResult = singleResult;
    }

    public String getEntityTypeName() {
        return entityTypeName;
    }

    public void setEntityTypeName(String entityTypeName) {
        this.entityTypeName = entityTypeName;
    }

    public String getDomainTypeName() {
        return domainTypeName;
    }

    public void setDomainTypeName(String domainTypeName) {
        this.domainTypeName = domainTypeName;
    }

    public Class<? extends Options> getOptionsClass() {
        return SelectOptions.class;
    }

    public boolean isIteration() {
        return iteration;
    }

    public void setIteration(boolean iteration) {
        this.iteration = iteration;
    }

    public Integer getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(Integer maxRows) {
        this.maxRows = maxRows;
    }

    public Integer getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(Integer fetchSize) {
        this.fetchSize = fetchSize;
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visistSqlFileSelectQueryMeta(this, p);
    }

}
