package __.org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.internal.apt.processor.entity.VersionNo;
import org.seasar.doma.internal.apt.processor.entity.VersionNoConverter;
import org.seasar.doma.wrapper.IntegerWrapper;

public final class _VersionNo
    extends org.seasar.doma.jdbc.holder.AbstractHolderDesc<java.lang.Integer, VersionNo> {

  static {
    org.seasar.doma.internal.Artifact.validateVersion("@VERSION@");
  }

  private static final _VersionNo singleton = new _VersionNo();

  private static final VersionNoConverter converter = new VersionNoConverter();

  private _VersionNo() {
    super(IntegerWrapper::new);
  }

  @Override
  public VersionNo newHolder(java.lang.Integer value) {
    return converter.fromValueToHolder(value);
  }

  @Override
  public Integer getBasicValue(VersionNo holder) {
    if (holder == null) {
      return null;
    }
    return converter.fromHolderToValue(holder);
  }

  @Override
  public Class<Integer> getBasicClass() {
    return Integer.class;
  }

  @Override
  public Class<VersionNo> getHolderClass() {
    return VersionNo.class;
  }

  /** @return the singleton */
  public static _VersionNo getSingletonInternal() {
    return singleton;
  }
}
