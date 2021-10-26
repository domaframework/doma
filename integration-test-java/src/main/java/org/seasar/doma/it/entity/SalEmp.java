package org.seasar.doma.it.entity;

import java.sql.Array;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Table;

@Entity
@Table(name = "SAL_EMP")
public class SalEmp {

  @Id String name;

  Array payByQuarter;

  Array schedule;

  @OriginalStates SalEmp originalStates;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Array getPayByQuarter() {
    return payByQuarter;
  }

  public void setPayByQuarter(Array payByQuarter) {
    this.payByQuarter = payByQuarter;
  }

  public Array getSchedule() {
    return schedule;
  }

  public void setSchedule(Array schedule) {
    this.schedule = schedule;
  }
}
