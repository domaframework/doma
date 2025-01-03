/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.processor.entity;

import java.math.BigDecimal;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Version;

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

  public ImmutableEmp(Integer id, String name, BigDecimal salary, Integer version) {
    this.id = id;
    this.name = name;
    this.salary = salary;
    this.version = version;
  }
}
