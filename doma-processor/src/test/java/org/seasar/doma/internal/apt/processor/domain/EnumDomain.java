package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.Domain;

@Domain(valueType = EnumDomain.JobType.class, acceptNull = true)
public class EnumDomain {

  private final JobType jobType;

  public EnumDomain(JobType jobType) {
    this.jobType = jobType;
  }

  public JobType getValue() {
    return jobType;
  }

  public static enum JobType {
    SALESMAN
  }
}
