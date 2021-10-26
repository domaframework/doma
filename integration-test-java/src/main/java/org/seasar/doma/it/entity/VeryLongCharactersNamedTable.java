package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;

@Entity
public class VeryLongCharactersNamedTable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer veryLongCharactersNamedTableId;

  String value;

  public Integer getVeryLongCharactersNamedTableId() {
    return veryLongCharactersNamedTableId;
  }

  public void setVeryLongCharactersNamedTableId(Integer veryLongCharactersNamedTableId) {
    this.veryLongCharactersNamedTableId = veryLongCharactersNamedTableId;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
