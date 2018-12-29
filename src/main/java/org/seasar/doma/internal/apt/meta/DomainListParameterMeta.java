package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.cttype.DomainCtType;

public class DomainListParameterMeta implements CallableSqlParameterMeta {

  protected final String name;

  protected final DomainCtType domainCtType;

  public DomainListParameterMeta(String name, DomainCtType domainCtType) {
    assertNotNull(name, domainCtType);
    this.name = name;
    this.domainCtType = domainCtType;
  }

  public String getName() {
    return name;
  }

  public DomainCtType getDomainCtType() {
    return domainCtType;
  }

  @Override
  public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
    return visitor.visitDomainListParameterMeta(this, p);
  }
}
