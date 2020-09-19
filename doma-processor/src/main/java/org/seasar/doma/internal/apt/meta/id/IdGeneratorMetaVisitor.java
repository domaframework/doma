package org.seasar.doma.internal.apt.meta.id;

public interface IdGeneratorMetaVisitor<R, P> {

  R visitIdentityIdGeneratorMeta(IdentityIdGeneratorMeta m, P p);

  R visitSequenceIdGeneratorMeta(SequenceIdGeneratorMeta m, P p);

  R visitTableIdGeneratorMeta(TableIdGeneratorMeta m, P p);
}
