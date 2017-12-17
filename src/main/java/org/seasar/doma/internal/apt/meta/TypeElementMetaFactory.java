package org.seasar.doma.internal.apt.meta;

/**
 * @author taedium
 * 
 */
public interface TypeElementMetaFactory<M extends TypeElementMeta> {

    M createTypeElementMeta();
}
