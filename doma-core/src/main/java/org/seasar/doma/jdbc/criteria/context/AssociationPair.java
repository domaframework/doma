package org.seasar.doma.jdbc.criteria.context;

import java.util.Objects;

public class AssociationPair<ENTITY1, ENTITY2> {

  private final ENTITY1 first;
  private final ENTITY2 second;

  public AssociationPair(ENTITY1 first, ENTITY2 second) {
    this.first = Objects.requireNonNull(first);
    this.second = Objects.requireNonNull(second);
  }

  public ENTITY1 getFirst() {
    return first;
  }

  public ENTITY2 getSecond() {
    return second;
  }
}
