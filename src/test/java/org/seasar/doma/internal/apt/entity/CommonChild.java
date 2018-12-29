package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;

@Entity(listener = CommonChildListener.class)
public class CommonChild extends Common {}
