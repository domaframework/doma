package org.seasar.doma.internal.apt.meta.holder;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;

/**
 * @author taedium
 * 
 */
public class ExternalHolderMeta implements TypeElementMeta {

    private final TypeElement typeElement;

    private final TypeElement holderElement;

    private final BasicCtType basicCtType;

    public ExternalHolderMeta(TypeElement typeElement, TypeElement holderElement,
            BasicCtType basicCtType) {
        assertNotNull(typeElement, holderElement, basicCtType);
        this.typeElement = typeElement;
        this.holderElement = holderElement;
        this.basicCtType = basicCtType;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public String getValueTypeName() {
        return basicCtType.getTypeName();
    }

    public TypeElement getHolderElement() {
        return holderElement;
    }

    public BasicCtType getBasicCtType() {
        return basicCtType;
    }

}
