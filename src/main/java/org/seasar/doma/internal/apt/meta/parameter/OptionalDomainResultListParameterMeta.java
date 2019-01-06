package org.seasar.doma.internal.apt.meta.parameter;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.cttype.DomainCtType;

public class OptionalDomainResultListParameterMeta implements ResultListParameterMeta {

  protected final DomainCtType domainCtType;

  public OptionalDomainResultListParameterMeta(DomainCtType domainCtType) {
    assertNotNull(domainCtType);
    this.domainCtType = domainCtType;
  }

  public DomainCtType getDomainCtType() {
    return domainCtType;
  }

  @Override
  public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
    return visitor.visitOptionalDomainResultListParameterMeta(this, p);
  }
}
