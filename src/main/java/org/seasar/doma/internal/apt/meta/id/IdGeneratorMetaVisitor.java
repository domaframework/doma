package org.seasar.doma.internal.apt.meta.id;

/** @author taedium */
public interface IdGeneratorMetaVisitor<P> {

  void visitIdentityIdGeneratorMeta(IdentityIdGeneratorMeta m, P p);

  void visitSequenceIdGeneratorMeta(SequenceIdGeneratorMeta m, P p);

  void visitTableIdGeneratorMeta(TableIdGeneratorMeta m, P p);
}
