package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.Domain;

@Domain(valueType = OfEnumDomain.JobType.class, factoryMethod = "of", acceptNull = true)
public class OfEnumDomain {

  private final JobType jobType;

  private OfEnumDomain(JobType jobType) {
    this.jobType = jobType;
  }

  public JobType getValue() {
    return jobType;
  }

  public static OfEnumDomain of(JobType value) {
    return new OfEnumDomain(value);
  }

  public static enum JobType {
    SALESMAN
  }
}
