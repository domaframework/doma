package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class BranchConverter implements DomainConverter<Branch, String> {

  @Override
  public String fromDomainToValue(Branch domain) {
    return null;
  }

  @Override
  public Branch fromValueToDomain(String value) {
    return null;
  }
}
