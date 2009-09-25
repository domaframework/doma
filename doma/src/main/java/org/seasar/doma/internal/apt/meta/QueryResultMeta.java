package org.seasar.doma.internal.apt.meta;

public class QueryResultMeta {

    private String typeName;

    private String wrapperTypeName;

    private String elementTypeName;

    private String elementWrapperTypeName;

    private boolean collection;

    private boolean entity;

    private boolean domain;

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

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public boolean isEntity() {
        return entity;
    }

    public void setEntity(boolean entity) {
        this.entity = entity;
    }

}
