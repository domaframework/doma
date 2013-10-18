package org.seasar.doma.internal.jdbc.scalar;

import java.util.Optional;

import org.seasar.doma.jdbc.JdbcMappingHint;
import org.seasar.doma.wrapper.Wrapper;

public interface Scalar<BASIC, CONTAINER> extends JdbcMappingHint {

    Class<BASIC> getBasicClass();

    Optional<Class<?>> getDomainClass();

    boolean isOptional();

    CONTAINER cast(Object value);

    CONTAINER get();

    CONTAINER getDefault();

    void set(CONTAINER container);

    Wrapper<BASIC> getWrapper();

}
