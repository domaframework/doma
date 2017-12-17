package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.VariableElement;

import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.reflection.ColumnReflection;

/**
 * @author nakamura-to
 *
 */
public class EmbeddablePropertyMeta extends AbstractPropertyMeta {

    public EmbeddablePropertyMeta(VariableElement fieldElement, String name, CtType ctType,
            ColumnReflection columnReflection) {
        super(fieldElement, name, ctType, columnReflection);
        assertNotNull(fieldElement);
    }

}
