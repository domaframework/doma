package org.seasar.doma.jdbc;

import org.seasar.doma.jdbc.query.Query;

/** A list parameter. */
public interface ListParameter<ELEMENT> extends SqlParameter {

  /**
   * Returns the parameter name.
   *
   * @return the parameter name
   */
  String getName();

  /**
   * Creates the object provider that provides list elements.
   *
   * @param query the query
   * @return the object provider
   */
  ObjectProvider<ELEMENT> createObjectProvider(Query query);

  /**
   * Adds the list element.
   *
   * @param element the list element
   */
  void add(ELEMENT element);
}
