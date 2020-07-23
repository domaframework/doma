package org.seasar.doma.internal.apt.processor.embeddable;

import org.seasar.doma.Embeddable;
import org.seasar.doma.internal.apt.lombok.AccessLevel;
import org.seasar.doma.internal.apt.lombok.AllArgsConstructor;

@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LombokAllArgsConstructorAccess_private {}
