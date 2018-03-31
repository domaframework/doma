package __.org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.internal.apt.processor.entity.Branch;
import org.seasar.doma.internal.apt.processor.entity.BranchConverter;
import org.seasar.doma.wrapper.StringWrapper;

public final class _Branch
    extends org.seasar.doma.jdbc.holder.AbstractHolderDesc<java.lang.String, Branch> {

  static {
    org.seasar.doma.internal.Artifact.validateVersion("@VERSION@");
  }

  private static final _Branch singleton = new _Branch();

  private static final BranchConverter converter = new BranchConverter();

  private _Branch() {
    super(StringWrapper::new);
  }

  @Override
  public Branch newHolder(java.lang.String value) {
    return converter.fromValueToHolder(value);
  }

  @Override
  public String getBasicValue(Branch holder) {
    if (holder == null) {
      return null;
    }
    return converter.fromHolderToValue(holder);
  }

  @Override
  public Class<String> getBasicClass() {
    return String.class;
  }

  @Override
  public Class<Branch> getHolderClass() {
    return Branch.class;
  }

  /** @return the singleton */
  public static _Branch getSingletonInternal() {
    return singleton;
  }
}
