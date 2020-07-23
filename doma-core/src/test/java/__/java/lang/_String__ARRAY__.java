package __.java.lang;

import org.seasar.doma.wrapper.ObjectWrapper;

public final class _String__ARRAY__
    extends org.seasar.doma.jdbc.domain.AbstractDomainType<Object, String[]> {

  private static final _String__ARRAY__ singleton = new _String__ARRAY__();

  private _String__ARRAY__() {
    super(ObjectWrapper::new);
  }

  @Override
  public String[] newDomain(Object value) {
    return null;
  }

  @Override
  public Object getBasicValue(String[] domain) {
    if (domain == null) {
      return null;
    }
    return null;
  }

  @Override
  public Class<Object> getBasicClass() {
    return Object.class;
  }

  @Override
  public Class<String[]> getDomainClass() {
    return String[].class;
  }

  /** @return the singleton */
  public static _String__ARRAY__ getSingletonInternal() {
    return singleton;
  }
}
