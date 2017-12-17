package org.seasar.doma.jdbc;

/**
 * An input parameter.
 *
 * @param <BASIC>
 *            the basic value
 */
public interface InParameter<BASIC> extends SqlParameter, JdbcMappable<BASIC> {

}
