package org.seasar.doma.internal.apt.processor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;

@Entity(listener = EmpListener.class)
@Table(schema = "AAA")
public class Emp {

  public static String staticField = "hoge";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(sequence = "EMP_ID")
  Integer id;

  String name;

  @Column(name = "SALARY", insertable = false, updatable = false)
  BigDecimal salary;

  @Version Integer version;

  Object object;

  @Transient String temp;

  @OriginalStates Emp originalStates;

  @Transient List<String> names;

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

  public Object getObject() {
    return object;
  }

  public void setObject(Object object) {
    this.object = object;
  }

  public int add(int a, int b) {
    return a + b;
  }

  public int hoge(String arg) {
    return 0;
  }

  public int hoge(Serializable arg) {
    return 0;
  }

  public int hoge(Object arg) {
    return 0;
  }

  public <T> int foo(T arg) {
    return 0;
  }

  public static int staticMethod(String arg) {
    return 0;
  }
}
