package __.org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.internal.apt.processor.entity.PrimaryKey;
import org.seasar.doma.internal.apt.processor.entity.PrimaryKeyConverter;

public final class _PrimaryKey
    extends org.seasar.doma.jdbc.holder.AbstractHolderDesc<java.lang.Integer, PrimaryKey> {

  static {
    org.seasar.doma.internal.Artifact.validateVersion("@VERSION@");
  }

  private static final _PrimaryKey singleton = new _PrimaryKey();

  private static final PrimaryKeyConverter converter = new PrimaryKeyConverter();

  private _PrimaryKey() {
    super(() -> new org.seasar.doma.wrapper.IntegerWrapper());
  }

  @Override
  public PrimaryKey newHolder(java.lang.Integer value) {
    return converter.fromValueToHolder(value);
  }

  @Override
  public Integer getBasicValue(PrimaryKey holder) {
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
  public Class<PrimaryKey> getHolderClass() {
    return PrimaryKey.class;
  }

  /** @return the singleton */
  public static _PrimaryKey getSingletonInternal() {
    return singleton;
  }
}
