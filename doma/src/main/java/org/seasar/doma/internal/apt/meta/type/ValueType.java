package org.seasar.doma.internal.apt.meta.type;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.TypeUtil;

public class ValueType {

    protected TypeMirror type;

    protected String typeName;

    protected WrapperType wrapperType;

    protected ValueType() {
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

    public static ValueType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        WrapperType wrapperType = WrapperType.newInstance(type, env);
        if (wrapperType == null) {
            return null;
        }
        ValueType valueType = new ValueType();
        valueType.type = type;
        valueType.typeName = TypeUtil.getTypeName(type, env);
        valueType.wrapperType = wrapperType;
        return valueType;
    }
}
