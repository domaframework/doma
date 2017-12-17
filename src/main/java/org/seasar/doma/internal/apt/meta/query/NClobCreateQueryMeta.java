package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.internal.apt.reflection.NClobFactoryReflection;

/**
 * @author taedium
 * 
 */
public class NClobCreateQueryMeta extends AbstractCreateQueryMeta {

    private NClobFactoryReflection nClobFactoryReflection;

    public NClobCreateQueryMeta(ExecutableElement method) {
        super(method);
    }

    public NClobFactoryReflection getNClobFactoryReflection() {
        return nClobFactoryReflection;
    }

    public void setNClobFactoryReflection(NClobFactoryReflection nClobFactoryReflection) {
        this.nClobFactoryReflection = nClobFactoryReflection;
    }

}
