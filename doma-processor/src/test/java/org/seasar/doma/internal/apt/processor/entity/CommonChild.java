package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;

@Entity(listener = CommonChildListener.class)
public class CommonChild extends Common {}
