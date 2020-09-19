package __.org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.internal.apt.processor.entity.PrimaryKey;
import org.seasar.doma.internal.apt.processor.entity.PrimaryKeyConverter;
import org.seasar.doma.wrapper.IntegerWrapper;

public final class _PrimaryKey
    extends org.seasar.doma.jdbc.domain.AbstractDomainType<java.lang.Integer, PrimaryKey> {

  static {
    org.seasar.doma.internal.Artifact.validateVersion("@VERSION@");
  }

  private static final _PrimaryKey singleton = new _PrimaryKey();

  private static final PrimaryKeyConverter converter = new PrimaryKeyConverter();

  private _PrimaryKey() {
    super(IntegerWrapper::new);
  }

  @Override
  public PrimaryKey newDomain(java.lang.Integer value) {
    return converter.fromValueToDomain(value);
  }

  @Override
  public Integer getBasicValue(PrimaryKey domain) {
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
  public Class<PrimaryKey> getDomainClass() {
    return PrimaryKey.class;
  }

  /** @return the singleton */
  public static _PrimaryKey getSingletonInternal() {
    return singleton;
  }
}
