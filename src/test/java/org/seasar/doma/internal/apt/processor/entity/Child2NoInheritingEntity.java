package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.entity.NullEntityListener;

/**
 * @author taedium
 * 
 */
@Entity(listener = NullEntityListener.class)
public class Child2NoInheritingEntity extends Parent2Entity {
}
