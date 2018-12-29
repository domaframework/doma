package example.domain;

import org.seasar.doma.jdbc.domain.AbstractDomainType;

public class _JobType extends AbstractDomainType<Integer, JobType> {

  private static final _JobType singleton = new _JobType();

  private _JobType() {
    super(() -> new org.seasar.doma.wrapper.IntegerWrapper());
  }

  @Override
  public JobType newDomain(Integer value) {
    if (value == null) {
      return new JobType(0);
    }
    return new JobType(value);
  }

  @Override
  public Integer getBasicValue(JobType domain) {
    return domain.getValue();
  }

  @Override
  public Class<Integer> getBasicClass() {
    return Integer.class;
  }

  @Override
  public Class<JobType> getDomainClass() {
    return JobType.class;
  }

  public static _JobType getSingletonInternal() {
    return singleton;
  }
}
