package __.org.seasar.doma.jdbc.domain;

import org.seasar.doma.jdbc.domain.Job;

public final class _Job extends org.seasar.doma.jdbc.domain.AbstractDomainType<String, Job> {

  private static final _Job singleton = new _Job();

  private _Job() {
    super(() -> new org.seasar.doma.wrapper.StringWrapper());
  }

  @Override
  public Job newDomain(String value) {
    return null;
  }

  @Override
  public String getBasicValue(Job domain) {
    return null;
  }

  @Override
  public Class<String> getBasicClass() {
    return String.class;
  }

  @Override
  public Class<Job> getDomainClass() {
    return Job.class;
  }

  /** @return the singleton */
  public static _Job getSingletonInternal() {
    return singleton;
  }
}
