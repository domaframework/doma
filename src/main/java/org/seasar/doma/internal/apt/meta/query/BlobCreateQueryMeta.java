package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.internal.apt.reflection.BlobFactoryReflection;

/**
 * @author taedium
 * 
 */
public class BlobCreateQueryMeta extends AbstractCreateQueryMeta {

    private BlobFactoryReflection blobFactoryReflection;

    public BlobCreateQueryMeta(ExecutableElement method) {
        super(method);
    }

    public BlobFactoryReflection getBlobFactoryReflection() {
        return blobFactoryReflection;
    }

    public void setBlobFactoryReflection(BlobFactoryReflection blobFactoryReflection) {
        this.blobFactoryReflection = blobFactoryReflection;
    }

}
