package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;

/** @author taedium */
@Holder(valueType = OfEnumHolder.JobType.class, factoryMethod = "of", acceptNull = true)
public class OfEnumHolder {

  private final JobType jobType;

  private OfEnumHolder(JobType jobType) {
    this.jobType = jobType;
  }

  public JobType getValue() {
    return jobType;
  }

  public static OfEnumHolder of(JobType value) {
    return new OfEnumHolder(value);
  }

  public static enum JobType {
    SALESMAN
  }
}
