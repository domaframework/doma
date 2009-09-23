package org.seasar.doma.internal.apt.meta;

import javax.lang.model.type.TypeMirror;

public class IterationCallbackMeta {

    private QueryParameterMeta queryParameterMeta;

    private boolean entityTarget;

    private String resultTypeName;

    private TypeMirror resultType;

    private String targetTypeName;

    private TypeMirror targetType;

    private String targetWrapperTypeName;

    private TypeMirror targetWrapperType;

    public QueryParameterMeta getQueryParameterMeta() {
        return queryParameterMeta;
    }

    public void setQueryParameterMeta(QueryParameterMeta queryParameterMeta) {
        this.queryParameterMeta = queryParameterMeta;
    }

    public boolean isEntityTarget() {
        return entityTarget;
    }

    public void setEntityTarget(boolean entityTarget) {
        this.entityTarget = entityTarget;
    }

    public String getResultTypeName() {
        return resultTypeName;
    }

    public void setResultTypeName(String resultTypeName) {
        this.resultTypeName = resultTypeName;
    }

    public String getTargetTypeName() {
        return targetTypeName;
    }

    public void setTargetTypeName(String targetTypeName) {
        this.targetTypeName = targetTypeName;
    }

    public String getTargetWrapperTypeName() {
        return targetWrapperTypeName;
    }

    public void setTargetWrapperTypeName(String targetWrapperTypeName) {
        this.targetWrapperTypeName = targetWrapperTypeName;
    }

    public TypeMirror getResultType() {
        return resultType;
    }

    public void setResultType(TypeMirror resultType) {
        this.resultType = resultType;
    }

    public TypeMirror getTargetType() {
        return targetType;
    }

    public void setTargetType(TypeMirror targetType) {
        this.targetType = targetType;
    }

    public TypeMirror getTargetWrapperType() {
        return targetWrapperType;
    }

    public void setTargetWrapperType(TypeMirror targetWrapperType) {
        this.targetWrapperType = targetWrapperType;
    }

}
