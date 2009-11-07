package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.mirror.DomainMirror;
import org.seasar.doma.internal.apt.type.WrapperType;

public class DomainMeta {

    protected final TypeElement typeElement;

    protected final TypeMirror type;

    protected WrapperType wrapperType;

    protected DomainMirror domainMirror;

    public DomainMeta(TypeElement typeElement, TypeMirror type) {
        assertNotNull(typeElement, type);
        this.typeElement = typeElement;
        this.type = type;
    }

    public TypeMirror getType() {
        return type;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public WrapperType getWrapperType() {
        return wrapperType;
    }

    public void setWrapperType(WrapperType wrapperType) {
        this.wrapperType = wrapperType;
    }

    public TypeMirror getValueType() {
        return domainMirror.getValueTypeValue();
    }

    public String getAccessorMethod() {
        return domainMirror.getAccessorMethodValue();
    }

    DomainMirror getDomainMirror() {
        return domainMirror;
    }

    void setDomainMirror(DomainMirror domainMirror) {
        this.domainMirror = domainMirror;
    }

}
