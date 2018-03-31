package org.seasar.doma.internal.apt.processor.entity;

import java.math.BigDecimal;
import org.seasar.doma.*;

@Entity
class PackagePrivateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(sequence = "EMP_ID")
  Integer id;

  String name;

  @Column(name = "SALARY", insertable = false, updatable = false)
  BigDecimal salary;

  @Version Integer version;

  PackagePrivateEntity() {}
}
