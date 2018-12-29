package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;

/** @author taedium */
@Entity(listener = NoDefaultConstructorEntityListener.class)
public class NoDefaultConstructorEntityListenerEntity {}
