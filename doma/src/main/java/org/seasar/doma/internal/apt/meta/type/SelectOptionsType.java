package org.seasar.doma.internal.apt.meta.type;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.jdbc.SelectOptions;

public class SelectOptionsType {

    protected TypeMirror type;

    protected String typeName;

    protected SelectOptionsType() {
    }

    public TypeMirror getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public static SelectOptionsType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        if (!TypeUtil.isAssignable(type, SelectOptions.class, env)) {
            return null;
        }
        SelectOptionsType selectOptionsType = new SelectOptionsType();
        selectOptionsType.type = type;
        selectOptionsType.typeName = TypeUtil.getTypeName(type, env);
        return selectOptionsType;
    }
}
