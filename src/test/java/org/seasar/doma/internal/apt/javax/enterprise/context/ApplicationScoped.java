package org.seasar.doma.internal.apt.javax.enterprise.context;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Target;

@Target(value = {TYPE, METHOD, FIELD})
public @interface ApplicationScoped {}
