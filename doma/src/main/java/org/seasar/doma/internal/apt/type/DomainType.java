package org.seasar.doma.internal.apt.type;

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

    protected ValueType valueType;

    protected String accessorMetod;

    protected DomainType() {
    }

    public TypeMirror getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public String getAccessorMetod() {
        return accessorMetod;
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
        TypeMirror valueTypeMirror = getValueType(domain);
        ValueType valueType = ValueType.newInstance(valueTypeMirror, env);
        if (valueType == null) {
            return null;
        }
        DomainType domainType = new DomainType();
        domainType.type = type;
        domainType.typeName = TypeUtil.getTypeName(type, env);
        domainType.valueType = valueType;
        domainType.accessorMetod = domain.accessorMethod();
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
