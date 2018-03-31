package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;

@Entity(listener = NoDefaultConstructorEntityListener.class)
public class NoDefaultConstructorEntityListenerEntity {}
