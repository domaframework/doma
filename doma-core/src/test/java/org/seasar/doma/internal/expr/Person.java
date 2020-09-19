package org.seasar.doma.internal.expr;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Person {

  public String name;

  public static final String staticName = "hoge";

  public Optional<String> optionalName;

  public static final Optional<String> staticOptionalName = Optional.of("foo");

  public Optional<String> getOptionalName() {
    return optionalName;
  }

  public static Optional<String> getStaticOptionalName() {
    return staticOptionalName;
  }

  public OptionalInt age;

  public static OptionalInt staticAge;

  public OptionalInt getAge() {
    return age;
  }

  public static OptionalInt getStaticAge() {
    return staticAge;
  }

  public OptionalLong salary;

  public OptionalDouble temperature;
}
