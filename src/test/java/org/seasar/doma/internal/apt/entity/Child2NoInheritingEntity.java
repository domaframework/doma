package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.entity.NullEntityListener;

@Entity(listener = NullEntityListener.class)
public class Child2NoInheritingEntity extends Parent2Entity {}
