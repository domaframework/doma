package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.entity.NullEntityListener;

@SuppressWarnings("DefaultAnnotationParam")
@Entity(listener = NullEntityListener.class)
public class Child2NoInheritingEntity extends Parent2Entity {}
