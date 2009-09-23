package org.seasar.doma.internal.apt.meta;

public class QueryResultMeta {

    private String typeName;

    private String wrapperTypeName;

    private String elementTypeName;

    private String elementWrapperTypeName;

    private boolean list;

    private boolean entity;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getWrapperTypeName() {
        return wrapperTypeName;
    }

    public void setWrapperTypeName(String wrapperTypeName) {
        this.wrapperTypeName = wrapperTypeName;
    }

    public String getElementTypeName() {
        return elementTypeName;
    }

    public void setElementTypeName(String elementTypeName) {
        this.elementTypeName = elementTypeName;
    }

    public String getElementWrapperTypeName() {
        return elementWrapperTypeName;
    }

    public void setElementWrapperTypeName(String elementWrapperTypeName) {
        this.elementWrapperTypeName = elementWrapperTypeName;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public boolean isEntity() {
        return entity;
    }

    public void setEntity(boolean entity) {
        this.entity = entity;
    }

}
