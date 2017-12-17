package org.seasar.doma.internal.apt.processor.entity;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;

public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "PERSON_ID")
    Integer id;

    Optional<String> name;

    static Optional<String> staticName;

    public Optional<String> getName() {
        return name;
    }

    public static Optional<String> getStaticName() {
        return staticName;
    }

    OptionalInt age;

    static OptionalInt staticAge;

    public OptionalInt getAge() {
        return age;
    }

    public static OptionalInt getStaticAge() {
        return staticAge;
    }

    OptionalLong salary;

    OptionalDouble temperature;
}
