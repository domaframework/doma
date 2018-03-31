package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.holder.HolderConverter;

/** @author taedium */
public class BranchConverter implements HolderConverter<Branch, String> {

  @Override
  public String fromHolderToValue(Branch holder) {
    return null;
  }

  @Override
  public Branch fromValueToHolder(String value) {
    return null;
  }
}
