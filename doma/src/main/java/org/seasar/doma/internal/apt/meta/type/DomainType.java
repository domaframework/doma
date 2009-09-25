package org.seasar.doma.internal.apt.meta.type;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Domain;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.TypeUtil;

public class DomainType {

    protected TypeMirror type;

    protected String typeName;

    protected WrapperType wrapperType;

    protected DomainType() {
    }

    public TypeMirror getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public WrapperType getWrapperType() {
        return wrapperType;
    }

    public static DomainType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        TypeElement typeElement = TypeUtil.toTypeElement(type, env);
        if (typeElement == null) {
            return null;
        }
        Domain domain = typeElement.getAnnotation(Domain.class);
        if (domain == null) {
            return null;
        }
        TypeMirror valueType = getValueType(domain);
        WrapperType wrapperType = WrapperType.newInstance(valueType, env);
        if (wrapperType == null) {
            return null;
        }
        DomainType domainType = new DomainType();
        domainType.type = type;
        domainType.typeName = TypeUtil.getTypeName(type, env);
        domainType.wrapperType = wrapperType;
        return domainType;
    }

    protected static TypeMirror getValueType(Domain domain) {
        try {
            domain.valueType();
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
        throw new AptIllegalStateException();
    }
}
