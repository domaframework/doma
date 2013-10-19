package org.seasar.doma.internal.jdbc.scalar;

import org.seasar.doma.internal.jdbc.command.JdbcMappable;

public interface Scalar<BASIC, CONTAINER> extends JdbcMappable<BASIC> {

    CONTAINER cast(Object value);

    CONTAINER get();

    CONTAINER getDefault();

    void set(CONTAINER container);

}
