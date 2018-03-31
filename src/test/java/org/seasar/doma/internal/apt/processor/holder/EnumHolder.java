package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;

/** @author taedium */
@Holder(valueType = EnumHolder.JobType.class, acceptNull = true)
public class EnumHolder {

  private final JobType jobType;

  public EnumHolder(JobType jobType) {
    this.jobType = jobType;
  }

  public JobType getValue() {
    return jobType;
  }

  public static enum JobType {
    SALESMAN
  }
}
