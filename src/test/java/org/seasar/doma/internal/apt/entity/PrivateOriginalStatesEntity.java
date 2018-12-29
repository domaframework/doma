package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.OriginalStates;

/** @author taedium */
@Entity
public class PrivateOriginalStatesEntity {

  private String name;

  @OriginalStates private PrivateOriginalStatesEntity originalStates;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
