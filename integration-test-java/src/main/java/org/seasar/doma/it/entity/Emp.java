package org.seasar.doma.it.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;

@Entity(listener = EmpListener.class)
public class Emp {

  @Id Integer id;

  String name;

  BigDecimal salary;

  @Version Integer version;

  Timestamp insertTimestamp;

  Timestamp updateTimestamp;

  @Transient String temp;

  @Transient List<String> tempList;

  @OriginalStates Emp originalStates;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getSalary() {
    return salary;
  }

  public void setSalary(BigDecimal salary) {
    this.salary = salary;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public Timestamp getInsertTimestamp() {
    return insertTimestamp;
  }

  public void setInsertTimestamp(Timestamp insertTimestamp) {
    this.insertTimestamp = insertTimestamp;
  }

  public Timestamp getUpdateTimestamp() {
    return updateTimestamp;
  }

  public void setUpdateTimestamp(Timestamp updateTimestamp) {
    this.updateTimestamp = updateTimestamp;
  }

  public String getTemp() {
    return temp;
  }

  public void setTemp(String temp) {
    this.temp = temp;
  }

  public List<String> getTempList() {
    return tempList;
  }

  public void setTempList(List<String> tempList) {
    this.tempList = tempList;
  }
}
