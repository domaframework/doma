package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity
public class Room {

  @Id public Integer id;

  public String[] colors;
}
