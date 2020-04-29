package org.seasar.doma.internal.apt.processor.entitydesc;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import org.seasar.doma.Entity;

@Entity
public class Emp {
  public Integer id;
  public Name name;
  public EmpInfo empInfo;
  public Optional<String> optional;
  public OptionalDouble optionalDouble;
  public OptionalInt optionalInt;
  public OptionalLong optionalLong;
}
