package org.seasar.doma.jdbc;

/** A single result parameter. */
public interface SingleResultParameter<BASIC, RESULT>
    extends ResultParameter<RESULT>, JdbcMappable<BASIC> {}
