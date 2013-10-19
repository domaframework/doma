package org.seasar.doma.internal.jdbc.scalar;

import java.util.Optional;

import org.seasar.doma.internal.jdbc.command.JdbcMappable;

public interface Scalar<BASIC, CONTAINER> extends JdbcMappable<BASIC> {

    Class<BASIC> getBasicClass();

    Optional<Class<?>> getDomainClass();

    boolean isOptional();

    CONTAINER cast(Object value);

    CONTAINER get();

    CONTAINER getDefault();

    void set(CONTAINER container);

}
