package org.seasar.doma.internal.apt.cdi;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Target;

@Target(value = {TYPE})
public @interface Dependent {}
