package org.seasar.doma.internal.apt.processor.embeddabledesc;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import org.seasar.doma.Embeddable;

@Embeddable
public class Address {

  public final Integer streetNo;
  public final City city;
  public final Optional<String> optional;
  public final OptionalDouble optionalDouble;
  public final OptionalInt optionalInt;
  public final OptionalLong optionalLong;

  public Address(
      Integer streetNo,
      City city,
      Optional<String> optional,
      OptionalDouble optionalDouble,
      OptionalInt optionalInt,
      OptionalLong optionalLong) {
    this.streetNo = streetNo;
    this.city = city;
    this.optional = optional;
    this.optionalDouble = optionalDouble;
    this.optionalInt = optionalInt;
    this.optionalLong = optionalLong;
  }
}
