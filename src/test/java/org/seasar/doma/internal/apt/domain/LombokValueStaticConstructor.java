package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.Domain;
import org.seasar.doma.internal.apt.lombok.Value;

@Domain(valueType = String.class)
@Value(staticConstructor = "of")
public class LombokValueStaticConstructor {}
