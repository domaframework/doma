package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.lombok.Value;

@Entity(immutable = true)
@Value(staticConstructor = "of")
public class LombokValueStaticConstructor {}
