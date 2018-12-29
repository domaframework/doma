package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.lombok.Value;

/** @author nakamura-to */
@Entity(immutable = true)
@Value(staticConstructor = "of")
public class LombokValueStaticConstructor {}
