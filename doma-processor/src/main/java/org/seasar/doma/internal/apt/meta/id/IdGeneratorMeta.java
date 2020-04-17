package org.seasar.doma.internal.apt.meta.id;

public interface IdGeneratorMeta {

  String getIdGeneratorClassName();

  <R, P> R accept(IdGeneratorMetaVisitor<R, P> visitor, P p);
}
