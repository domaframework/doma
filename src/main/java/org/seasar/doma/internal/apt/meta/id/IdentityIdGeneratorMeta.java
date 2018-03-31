package org.seasar.doma.internal.apt.meta.id;

import org.seasar.doma.jdbc.id.BuiltinIdentityIdGenerator;

public class IdentityIdGeneratorMeta implements IdGeneratorMeta {

  @Override
  public String getIdGeneratorClassName() {
    return BuiltinIdentityIdGenerator.class.getName();
  }

  @Override
  public <P> void accept(IdGeneratorMetaVisitor<P> visitor, P p) {
    visitor.visitIdentityIdGeneratorMeta(this, p);
  }
}
