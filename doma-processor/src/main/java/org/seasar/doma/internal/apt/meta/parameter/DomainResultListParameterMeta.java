package org.seasar.doma.internal.apt.meta.parameter;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.cttype.DomainCtType;

public class DomainResultListParameterMeta implements ResultListParameterMeta {

  private final DomainCtType domainCtType;

  public DomainResultListParameterMeta(DomainCtType domainCtType) {
    assertNotNull(domainCtType);
    this.domainCtType = domainCtType;
  }

  public DomainCtType getDomainCtType() {
    return domainCtType;
  }

  @Override
  public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
    return visitor.visitDomainResultListParameterMeta(this, p);
  }
}
