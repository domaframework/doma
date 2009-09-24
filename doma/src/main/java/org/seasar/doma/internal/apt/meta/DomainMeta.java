package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public class DomainMeta {

    protected final TypeElement typeElement;

    protected final TypeMirror type;

    protected String wrapperTypeName;

    protected TypeElement valueTypeElement;

    protected TypeMirror valueType;

    protected String accessorMethod;

    public DomainMeta(TypeElement typeElement, TypeMirror type) {
        assertNotNull(typeElement, type);
        this.typeElement = typeElement;
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

    public TypeElement getValueTypeElement() {
        return valueTypeElement;
    }

    public void setValueTypeElement(TypeElement valueTypeElement) {
        this.valueTypeElement = valueTypeElement;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

}
