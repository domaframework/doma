package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.internal.util.BoxedPrimitiveUtil;
import org.seasar.doma.jdbc.domain.AbstractDomainType;
import org.seasar.doma.wrapper.IntegerWrapper;

public final class _Age extends AbstractDomainType<Integer, Age> {

  private static final _Age singleton = new _Age();

  private _Age() {
    super(IntegerWrapper::new);
  }

  @Override
  protected Age newDomain(java.lang.Integer value) {
    return new Age(BoxedPrimitiveUtil.unbox(value));
  }

  @Override
  protected java.lang.Integer getBasicValue(Age domain) {
    if (domain == null) {
      return null;
    }
    return domain.getValue();
  }

  @Override
  public Class<?> getBasicClass() {
    return int.class;
  }

  @Override
  public Class<Age> getDomainClass() {
    return Age.class;
  }

  public static _Age getSingletonInternal() {
    return singleton;
  }
}
