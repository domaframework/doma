package org.seasar.doma.jdbc;

/** @author nakamura-to */
public interface SingleResultParameter<BASIC, RESULT>
    extends ResultParameter<RESULT>, JdbcMappable<BASIC> {}
