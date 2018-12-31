package org.seasar.doma.internal.apt.meta.id;

public interface IdGeneratorMetaVisitor<R, P> {

  R visistIdentityIdGeneratorMeta(IdentityIdGeneratorMeta m, P p);

  R visistSequenceIdGeneratorMeta(SequenceIdGeneratorMeta m, P p);

  R visistTableIdGeneratorMeta(TableIdGeneratorMeta m, P p);
}
