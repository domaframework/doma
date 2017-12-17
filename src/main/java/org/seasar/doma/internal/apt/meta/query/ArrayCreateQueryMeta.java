package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.internal.apt.reflection.ArrayFactoryReflection;

/**
 * @author taedium
 * 
 */
public class ArrayCreateQueryMeta extends AbstractCreateQueryMeta {

    private String elementsParameterName;

    private ArrayFactoryReflection arrayFactoryReflection;

    public ArrayCreateQueryMeta(ExecutableElement method) {
        super(method);
    }

    public String getParameterName() {
        return elementsParameterName;
    }

    public void setElementsParameterName(String elementsParameterName) {
        this.elementsParameterName = elementsParameterName;
    }

    void setArrayFactoryReflection(ArrayFactoryReflection arrayFactoryReflection) {
        this.arrayFactoryReflection = arrayFactoryReflection;
    }

    public String getArrayTypeName() {
        return arrayFactoryReflection.getTypeNameValue();
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitArrayCreateQueryMeta(this, p);
    }
}
