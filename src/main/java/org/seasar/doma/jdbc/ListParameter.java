package org.seasar.doma.jdbc;

import org.seasar.doma.jdbc.query.Query;

/** @author taedium */
public interface ListParameter<ELEMENT> extends SqlParameter {

  String getName();

  ObjectProvider<ELEMENT> createObjectProvider(Query query);

  void add(ELEMENT element);
}
