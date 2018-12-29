package __.org.seasar.doma.internal.apt.entity;

import org.seasar.doma.internal.apt.entity.VersionNo;
import org.seasar.doma.internal.apt.entity.VersionNoConverter;

public final class _VersionNo
    extends org.seasar.doma.jdbc.domain.AbstractDomainType<java.lang.Integer, VersionNo> {

  static {
    org.seasar.doma.internal.Artifact.validateVersion("@VERSION@");
  }

  private static final _VersionNo singleton = new _VersionNo();

  private static final VersionNoConverter converter = new VersionNoConverter();

  private _VersionNo() {
    super(() -> new org.seasar.doma.wrapper.IntegerWrapper());
  }

  @Override
  public VersionNo newDomain(java.lang.Integer value) {
    return converter.fromValueToDomain(value);
  }

  @Override
  public Integer getBasicValue(VersionNo domain) {
    if (domain == null) {
      return null;
    }
    return converter.fromDomainToValue(domain);
  }

  @Override
  public Class<Integer> getBasicClass() {
    return Integer.class;
  }

  @Override
  public Class<VersionNo> getDomainClass() {
    return VersionNo.class;
  }

  /** @return the singleton */
  public static _VersionNo getSingletonInternal() {
    return singleton;
  }
}
