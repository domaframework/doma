package org.seasar.doma.internal.apt.meta.dao;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.reflection.DaoReflection;

/**
 * @author taedium
 * 
 */
public class ParentDaoMeta {

    private final DaoReflection daoReflection;

    private final TypeElement daoElement;

    public ParentDaoMeta(DaoReflection daoReflection, TypeElement daoElement) {
        assertNotNull(daoReflection, daoElement);
        this.daoReflection = daoReflection;
        this.daoElement = daoElement;
    }

    public TypeMirror getDaoType() {
        return daoElement.asType();
    }

    public TypeElement getDaoElement() {
        return daoElement;
    }

    public boolean hasUserDefinedConfig() {
        return daoReflection.hasUserDefinedConfig();
    }

}
