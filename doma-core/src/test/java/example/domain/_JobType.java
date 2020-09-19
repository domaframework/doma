package example.domain;

import org.seasar.doma.jdbc.domain.AbstractDomainType;
import org.seasar.doma.wrapper.IntegerWrapper;

public class _JobType extends AbstractDomainType<Integer, JobType> {

  private static final _JobType singleton = new _JobType();

  private _JobType() {
    super(IntegerWrapper::new);
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
