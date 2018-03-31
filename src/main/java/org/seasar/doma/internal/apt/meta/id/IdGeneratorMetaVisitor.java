package org.seasar.doma.internal.apt.meta.id;

public interface IdGeneratorMetaVisitor<P> {

  void visitIdentityIdGeneratorMeta(IdentityIdGeneratorMeta m, P p);

  void visitSequenceIdGeneratorMeta(SequenceIdGeneratorMeta m, P p);

  void visitTableIdGeneratorMeta(TableIdGeneratorMeta m, P p);
}
