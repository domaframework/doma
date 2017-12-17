package org.seasar.doma.jdbc;

/**
 * An output parameter.
 *
 * @param <BASIC>
 *            the basic type
 */
public interface OutParameter<BASIC> extends SqlParameter, JdbcMappable<BASIC> {

    /**
     * Updates the reference value.
     */
    void updateReference();

}
