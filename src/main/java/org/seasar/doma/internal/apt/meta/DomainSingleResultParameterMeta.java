package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.cttype.DomainCtType;

public class DomainSingleResultParameterMeta implements SingleResultParameterMeta {

  protected final DomainCtType domainCtType;

  public DomainSingleResultParameterMeta(DomainCtType domainCtType) {
    assertNotNull(domainCtType);
    this.domainCtType = domainCtType;
  }

  public DomainCtType getDomainCtType() {
    return domainCtType;
  }

  @Override
  public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
    return visitor.visitDomainSingleResultParameterMeta(this, p);
  }
}
