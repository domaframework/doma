package example.holder;

import org.seasar.doma.jdbc.holder.AbstractHolderDesc;

public class _JobType extends AbstractHolderDesc<Integer, JobType> {

  private static final _JobType singleton = new _JobType();

  private _JobType() {
    super(() -> new org.seasar.doma.wrapper.IntegerWrapper());
  }

  @Override
  public JobType newHolder(Integer value) {
    if (value == null) {
      return new JobType(0);
    }
    return new JobType(value);
  }

  @Override
  public Integer getBasicValue(JobType holder) {
    return holder.getValue();
  }

  @Override
  public Class<Integer> getBasicClass() {
    return Integer.class;
  }

  @Override
  public Class<JobType> getHolderClass() {
    return JobType.class;
  }

  public static _JobType getSingletonInternal() {
    return singleton;
  }
}
