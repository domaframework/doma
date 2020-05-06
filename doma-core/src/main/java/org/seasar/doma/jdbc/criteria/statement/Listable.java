package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Optional;

public interface Listable<ELEMENT> extends Statement<List<ELEMENT>> {

  default List<ELEMENT> getResultList() {
    return execute();
  }

  default Optional<ELEMENT> getSingleResult() {
    return execute().stream().findFirst();
  }
}
