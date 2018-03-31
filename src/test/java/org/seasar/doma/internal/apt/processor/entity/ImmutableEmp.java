package org.seasar.doma.internal.apt.processor.entity;

import java.math.BigDecimal;
import java.util.List;
import org.seasar.doma.*;

@Entity(immutable = true)
public class ImmutableEmp {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(sequence = "EMP_ID")
  final Integer id;

  final String name;

  @Column(name = "SALARY", insertable = false, updatable = false)
  final BigDecimal salary;

  @Version final Integer version;

  public ImmutableEmp(
      Integer id, String name, BigDecimal salary, Integer version, List<String> names) {
    this.id = id;
    this.name = name;
    this.salary = salary;
    this.version = version;
  }
}
