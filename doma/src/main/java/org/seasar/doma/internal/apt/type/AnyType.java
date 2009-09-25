package org.seasar.doma.internal.apt.type;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.TypeUtil;

public class AnyType {

    protected TypeMirror type;

    protected String typeName;

    protected WrapperType wrapperType;

    protected AnyType() {
    }

    public TypeMirror getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public static AnyType newInstance(TypeMirror type, ProcessingEnvironment env) {
        assertNotNull(type, env);
        AnyType anyType = new AnyType();
        anyType.type = type;
        anyType.typeName = TypeUtil.getTypeName(type, env);
        return anyType;
    }
}
