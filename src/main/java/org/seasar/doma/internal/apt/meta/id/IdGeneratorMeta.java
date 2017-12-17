package org.seasar.doma.internal.apt.meta.id;

/**
 * @author taedium
 * 
 */
public interface IdGeneratorMeta {

    String getIdGeneratorClassName();

    <R, P> R accept(IdGeneratorMetaVisitor<R, P> visitor, P p);

}
