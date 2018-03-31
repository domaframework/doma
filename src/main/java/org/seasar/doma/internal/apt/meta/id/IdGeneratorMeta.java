package org.seasar.doma.internal.apt.meta.id;

public interface IdGeneratorMeta {

  String getIdGeneratorClassName();

  <P> void accept(IdGeneratorMetaVisitor<P> visitor, P p);
}
