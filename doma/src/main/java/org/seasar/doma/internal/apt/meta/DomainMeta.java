package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.type.TypeMirror;

public class DomainMeta {

    protected final TypeMirror type;

    protected String wrapperTypeName;

    protected TypeMirror valueType;

    protected String accessorMethod;

    public DomainMeta(TypeMirror type) {
        assertNotNull(type);
        this.type = type;
    }

    public TypeMirror getType() {
        return type;
    }

    public String getWrapperTypeName() {
        return wrapperTypeName;
    }

    public void setWrapperTypeName(String wrapperTypeName) {
        this.wrapperTypeName = wrapperTypeName;
    }

    public String getAccessorMethod() {
        return accessorMethod;
    }

    public void setAccessorMethod(String accessorMethod) {
        this.accessorMethod = accessorMethod;
    }

    public TypeMirror getValueType() {
        return valueType;
    }

    public void setValueType(TypeMirror valueType) {
        this.valueType = valueType;
    }

}
